package testdemo.sunyard.com.qrcod.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Toast;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.io.IOException;
import java.util.Vector;

import testdemo.sunyard.com.R;
import testdemo.sunyard.com.qrcod.camera.CameraManager;
import testdemo.sunyard.com.qrcod.decoding.CaptureActivityHandler;
import testdemo.sunyard.com.qrcod.decoding.InactivityTimer;
import testdemo.sunyard.com.qrcod.view.ViewfinderView;

/**
 * Initial the camera
 *
 */
public class CaptureActivity extends Activity implements Callback {

    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
//    private ProgressDialog mProgress;
//    private String photo_path;
//    private Bitmap scanBitmap;
    //	private Button cancelScanButton;
//    public static final int RESULT_CODE_QR_SCAN = 0xA1;
//    public static final String INTENT_EXTRA_KEY_QR_SCAN = "qr_scan_result";
    private boolean isExit = false;
    /**
     * 是否是切换摄像头，控制因两次切换摄像头会经历两次onPause和onResume
     */
    private boolean isSwitch = false;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_scanner);
        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_content);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<60;i++){
                    if(isExit){
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(!isExit) {
                    finish();
                }
            }
        }).start();
        findViewById(R.id.change_camera).bringToFront();
        findViewById(R.id.change_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                int cameraPosition = intent.getIntExtra("cameraposition", 0);
                if(cameraPosition == 0) {
                    intent.putExtra("cameraposition", 1);
                } else {
                    intent.putExtra("cameraposition", 0);
                }
                isSwitch = true;
                startActivityForResult(intent, 10);
            }
        });
        registSreenStatusReceiver();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("WY", "onResume");
        if (isSwitch) {
            isSwitch = false;
            return;
        }
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.scanner_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("WY", "onPause");
        if (isSwitch) {
            return;
        }
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onStart() {
        Log.d("WY", "onStart");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.d("WY", "onRestart");
        super.onRestart();
    }

    @Override
    protected void onStop() {
        Log.d("WY", "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("WY", "onDestroy");
        unregisterReceiver(mScreenStatusReceiver);
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * Handler scan result
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        if (TextUtils.isEmpty(resultString)) {
            Toast.makeText(CaptureActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_CANCELED);
        } else {
            Log.d("CaptureActivity", "handleDecode: "+resultString);
            Intent resultIntent = new Intent();
            //134644571163967125
            resultIntent.putExtra("result",resultString);
            setResult(RESULT_OK, resultIntent);
        }
        isExit = true;
        CaptureActivity.this.finish();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        int cameraPosition = getIntent().getIntExtra("cameraposition", 0);
        try {
            CameraManager.get().openDriver(surfaceHolder, cameraPosition);
        } catch (IOException | RuntimeException ioe) {
            ioe.printStackTrace();
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    private ScreenStatusReceiver mScreenStatusReceiver;

    private void registSreenStatusReceiver() {
        mScreenStatusReceiver = new ScreenStatusReceiver();
        IntentFilter screenStatusIF = new IntentFilter();
        screenStatusIF.addAction(Intent.ACTION_SCREEN_ON);
        screenStatusIF.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mScreenStatusReceiver, screenStatusIF);
    }

    class ScreenStatusReceiver extends BroadcastReceiver {
        String SCREEN_ON = "android.intent.action.SCREEN_ON";
        String SCREEN_OFF = "android.intent.action.SCREEN_OFF";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (SCREEN_ON.equals(intent.getAction())) {
                Log.d("WY", "SCREEN_ON");
                SurfaceView surfaceView = (SurfaceView) findViewById(R.id.scanner_view);
                SurfaceHolder surfaceHolder = surfaceView.getHolder();
                if (hasSurface) {
                    initCamera(surfaceHolder);
                } else {
                    surfaceHolder.addCallback(CaptureActivity.this);
                    surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                }
                decodeFormats = null;
                characterSet = null;

                playBeep = true;
                AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
                if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
                    playBeep = false;
                }
                initBeepSound();
                vibrate = false;
            }
            else if (SCREEN_OFF.equals(intent.getAction())) {
                Log.d("WY", "SCREEN_OFF");
                if (handler != null) {
                    handler.quitSynchronously();
                    handler = null;
                }
                CameraManager.get().closeDriver();
            }
        }
    }
}