package testdemo.sunyard.com;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

public class ImageSurfaceView extends android.support.v7.widget.AppCompatImageView {


    public Bitmap image_bitmap;
    public Paint paint = new Paint();
    public boolean flag;

    public ImageSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageSurfaceView(Context context, Bitmap bitmap) {
        super(context);
        //添加回调
        image_bitmap = bitmap;
    }


    public ImageSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setFanZhuanY(Bitmap bitmap){
        image_bitmap = bitmap;
        flag = !flag;
        Log.d("11111", "setFanZhuanY: "+flag);
        invalidate();
    }
    //界面渲染
    public void onDraw(Canvas canvas){
      //  canvas.drawBitmap(image_bitmap,0,0, null); //输出第一张图片
        //初始化Matrix 该类是针对图形做一些效果的工具类
        if(image_bitmap == null)return;
        if (flag) {

            Matrix m = new Matrix();
            m.setScale(-1, 1);
            m.postTranslate(image_bitmap.getWidth(), 0); //向右平移两个图片宽度的位置
            canvas.drawBitmap(image_bitmap, m, paint); //输出第二张图片
        }else{
            canvas.drawBitmap(image_bitmap,0,0, null);
        }
    }





}