package testdemo.sunyard.com.codeaty;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;

import testdemo.sunyard.com.ChoseCodeAty;
import testdemo.sunyard.com.OnItemClickListener;
import testdemo.sunyard.com.R;
import testdemo.sunyard.com.adapter.BitRecycleViewAdapter;
import testdemo.sunyard.com.adapter.ListRecycleViewAdapter;
import testdemo.sunyard.com.util.DividerItemDecoration;

public class RecycviewBitmapAty extends Activity implements OnItemClickListener {
    RecyclerView recyclerviewBitmap;
    int[] bitarr = {R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e
            , R.drawable.f, R.drawable.g, R.drawable.h, R.drawable.i, R.drawable.j
            , R.drawable.k, R.drawable.l, R.drawable.m, R.drawable.n};
    BitRecycleViewAdapter bitRecycleViewAdapter;
    List<Integer> integerList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_recycview_bitmap_aty);
        recyclerviewBitmap = findViewById(R.id.recyclerview_bitmap);
        integerList=new ArrayList<>();
        for (int i = 0; i <bitarr.length ; i++) {
            Log.d("bitarr", " : "+bitarr[i]);
            integerList.add(bitarr[i]);
        }
        for (int i = 0; i <integerList.size() ; i++) {
            Log.d("rtewtw", "onCreate: "+integerList.get(i));
        }
        recyclerviewBitmap.setLayoutManager(new LinearLayoutManager(this));
        recyclerviewBitmap.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        bitRecycleViewAdapter = new BitRecycleViewAdapter(RecycviewBitmapAty.this, integerList);
        bitRecycleViewAdapter.setOnItemClickListener(this);
        recyclerviewBitmap.setAdapter(bitRecycleViewAdapter);

    }

    @Override
    public void onItemClick(View view, int position) {
        switch (integerList.get(position)) {


        }
    }
}
