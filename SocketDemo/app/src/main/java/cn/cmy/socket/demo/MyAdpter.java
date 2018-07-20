package cn.cmy.socket.demo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MyAdpter extends RecyclerView.Adapter<MyAdpter.ViewHolder> {

    private List<Info> mLists;

    private Context mContext;

    public MyAdpter(List<Info> lists, Context context) {
        this.mLists = lists;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).
                inflate(R.layout.item_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Info info = mLists.get(i);
        if (info.type == Info.TYPE_SEND){
            viewHolder.tv_right.setVisibility(View.VISIBLE);
            viewHolder.tv_right.setText(info.info);
            viewHolder.tv_left.setVisibility(View.GONE);
        }else {
            viewHolder.tv_left.setVisibility(View.VISIBLE);
            viewHolder.tv_left.setText(info.info);
            viewHolder.tv_right.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_left;

        TextView tv_right;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_left = itemView.findViewById(R.id.tv_left_infos);
            tv_right = itemView.findViewById(R.id.tv_right_infos);
        }
    }


}
