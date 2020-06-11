package com.vivo.chmusicdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vivo.chmusicdemo.R;
import com.vivo.chmusicdemo.bean.ZhuangBiImage;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchImageAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<ZhuangBiImage> mDatas;

//    public SearchImageAdapter(Context context, List<ZhuangBiImage> datas) {
//        mContext = context;
//        mDatas = datas;
//    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_image_grid_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ImageViewHolder) {
            ZhuangBiImage image = mDatas.get(position);
            Glide.with(holder.itemView.getContext()).load(image.getImage_url()).into(((ImageViewHolder) holder).imageView);
            ((ImageViewHolder) holder).textView.setText(image.getDescripation());
        }
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public void setDatas(List<ZhuangBiImage> data) {
        mDatas = data;
        notifyDataSetChanged();
    }

    private static class ImageViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textView;

        ImageViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.image_search);
            textView = view.findViewById(R.id.descripation);
        }
    }
}
