package com.vivo.chmusicdemo.rxjava2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vivo.chmusicdemo.R;
import com.vivo.chmusicdemo.bean.GankNewsResultEntity;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GankNewsAdapter extends RecyclerView.Adapter {

    private List<GankNewsResultEntity> mNewsList = new ArrayList<>();
    private Context mContext;

    public GankNewsAdapter(Context context, List<GankNewsResultEntity> newsResultEntities) {
        mContext = context;
        mNewsList.clear();
        mNewsList.addAll(newsResultEntities);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.gank_news_viewholder, null, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof NewsViewHolder) {
            Glide.with(mContext).load(mNewsList.get(position).getUrl())
                    .dontAnimate().diskCacheStrategy(DiskCacheStrategy.NONE).into(((NewsViewHolder) holder).imageView);

            ((NewsViewHolder) holder).type.setText(mNewsList.get(position).getType());
            ((NewsViewHolder) holder).author.setText(mNewsList.get(position).getWho());
            ((NewsViewHolder) holder).desc.setText(mNewsList.get(position).getDesc());
        }
    }

    public void setNewsList(List<GankNewsResultEntity> list) {
        mNewsList.clear();
        mNewsList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    private class NewsViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView type;
        private TextView author;
        private TextView desc;

        public NewsViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.news_image);
            type = view.findViewById(R.id.type);
            author = view.findViewById(R.id.author);
            desc = view.findViewById(R.id.desc);
        }
    }
}
