package testdemo.sunyard.com.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import testdemo.sunyard.com.OnItemClickListener;
import testdemo.sunyard.com.R;

/**
 * 简单列表视图适配器
 * Created by wangyuan on 16/9/26.
 */
public class BitRecycleViewAdapter extends RecyclerView.Adapter<BitRecycleViewAdapter.ListViewHolder> {

    private Context mContext;
    private List<Integer> mList;

    public BitRecycleViewAdapter(Context context, List<Integer> list) {
        this.mContext = context;
        this.mList = list;
    }


    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_sys_bitmap, parent,
                false));
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, int position) {
        Log.d("onBindViewHolder", "mList: "+mList.size());
        Glide.with(mContext).load(mList.get(position)).asBitmap().into(holder.imageView);
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, pos);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        Log.d("dgsds", "getItemCount: "+mList.size());
        return mList.size();
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ListViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img_list_item);
        }
    }

}
