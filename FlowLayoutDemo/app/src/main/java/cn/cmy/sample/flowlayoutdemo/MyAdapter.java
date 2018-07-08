package cn.cmy.sample.flowlayoutdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<String> imageUrls;

    private Context mContext;

    private List<Integer> heights;


    public MyAdapter(List<String> imageUrls, Context context) {
        this.imageUrls = imageUrls;
        this.mContext = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // holder.img.setImageResource(imageIds.get(position));
        if (imageUrls.size() != 0) {
            getRandomHeight();
        } else {
            return;
        }
        Log.i("*********", "onBindViewHolder: heights.size:" + heights.size() + ",imageUrls.size:" + imageUrls.size());


        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        params.height = heights.get(position);

        holder.itemView.setLayoutParams(params);
        Glide.with(mContext).
                load(imageUrls.get(position)).
                // 占位图
                //    placeholder(R.drawable.body1).
                // 加载错误图
                //  error(R.drawable.body1).
                        into(holder.img);
        holder.tv.setText("" + position);

    }


    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;

        TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.item_frag_recommend_image);
            tv = itemView.findViewById(R.id.item_frag_recommend_name);

        }
    }

    private void getRandomHeight() {//得到随机item的高度
        heights = new ArrayList<>();
        for (int i = 0; i < imageUrls.size(); i++) {
            heights.add((int) (300 + Math.random() * 400));
        }
    }


}
