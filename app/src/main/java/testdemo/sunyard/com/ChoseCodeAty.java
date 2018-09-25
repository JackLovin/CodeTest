package testdemo.sunyard.com;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import testdemo.sunyard.com.adapter.ListRecycleViewAdapter;
import testdemo.sunyard.com.codeaty.CodeActivity;
import testdemo.sunyard.com.util.DividerItemDecoration;

public class ChoseCodeAty extends Activity implements OnItemClickListener {
    RecyclerView mRecyclerView;
    List<String> stringList;
    Intent intent;
    private ListRecycleViewAdapter listRecycleViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
//        Window window = getWindow();
//        window.setFlags(flag, flag);
        setContentView(R.layout.activity_chose_code_aty);
        stringList = new ArrayList<>();
        stringList.add("CODABAR");
        stringList.add("CODE_39");
        stringList.add("CODE_93");
        stringList.add("CODE_128");
       // stringList.add("DATA_MATRIX");
        stringList.add("EAN_8");
        stringList.add("EAN_13");
        stringList.add("ITF");
       // stringList.add("MAXICODE");
        stringList.add("PDF_417");
        stringList.add("QR_CODE");
      //  stringList.add("RSS_14");
      //  stringList.add("RSS_EXPANDED");
        stringList.add("UPC_A");
        stringList.add("UPC_E");
      //  stringList.add("UPC_EAN_EXTENSION");
        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        listRecycleViewAdapter = new ListRecycleViewAdapter(ChoseCodeAty.this, stringList);
        listRecycleViewAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(listRecycleViewAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (stringList.get(position)) {
            case "CODABAR":
                intent = new Intent(this, CodeActivity.class);
                intent.putExtra("choseCode", "CODABAR");
                startActivity(intent);
                break;
            case "CODE_39":
                intent = new Intent(this, CodeActivity.class);
                intent.putExtra("choseCode", "CODE_39");
                startActivity(intent);
                break;
            case "CODE_93":
                intent = new Intent(this, CodeActivity.class);
                intent.putExtra("choseCode", "CODE_93");
                startActivity(intent);
                break;
            case "CODE_128":
                intent = new Intent(this, CodeActivity.class);
                intent.putExtra("choseCode", "CODE_128");
                startActivity(intent);
                break;
            case "DATA_MATRIX":
                intent = new Intent(this, CodeActivity.class);
                intent.putExtra("choseCode", "DATA_MATRIX");
                startActivity(intent);
                break;
            case "EAN_8":
                intent = new Intent(this, CodeActivity.class);
                intent.putExtra("choseCode", "EAN_8");
                startActivity(intent);
                break;
            case "EAN_13":
                intent = new Intent(this, CodeActivity.class);
                intent.putExtra("choseCode", "EAN_13");
                startActivity(intent);
                break;
            case "ITF":
                intent = new Intent(this, CodeActivity.class);
                intent.putExtra("choseCode", "ITF");
                startActivity(intent);
                break;
            case "MAXICODE":

                break;
            case "PDF_417":
                intent = new Intent(this, CodeActivity.class);
                intent.putExtra("choseCode", "PDF_417");
                startActivity(intent);
                break;
            case "QR_CODE":
                 intent = new Intent(this, CodeActivity.class);
                intent.putExtra("choseCode", "QR_CODE");
                startActivity(intent);
                break;
            case "RSS_14":

                break;
            case "RSS_EXPANDED":

                break;
            case "UPC_A":
                intent = new Intent(this, CodeActivity.class);
                intent.putExtra("choseCode", "UPC_A");
                startActivity(intent);
                break;
            case "UPC_E":
                intent = new Intent(this, CodeActivity.class);
                intent.putExtra("choseCode", "UPC_E");
                startActivity(intent);
                break;
            case "UPC_EAN_EXTENSION":

                break;

        }
    }

    class MyAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return stringList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            return null;
        }
    }
}
