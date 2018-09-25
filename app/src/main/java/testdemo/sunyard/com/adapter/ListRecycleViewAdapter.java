package testdemo.sunyard.com.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

import testdemo.sunyard.com.OnItemClickListener;
import testdemo.sunyard.com.R;

/**
 * 简单列表视图适配器
 * Created by wangyuan on 16/9/26.
 */
public class ListRecycleViewAdapter extends RecyclerView.Adapter<ListRecycleViewAdapter.ListViewHolder> {

    private Context mContext;
    private List<String> mList;

    public ListRecycleViewAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.mList = list;
    }


    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_sys_setting, parent,
                false));
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, int position) {
        holder.tv.setText(mList.get(position));

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
        Log.d("4242234", "getItemCount: "+mList.size());
        return mList.size();
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        public ListViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv_list_item);
        }
    }

}
