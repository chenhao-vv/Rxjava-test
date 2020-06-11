package com.vivo.chmusicdemo.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.vivo.chmusicdemo.R;
import com.vivo.chmusicdemo.utils.blog.Blog;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class BlogListAdapter extends RecyclerView.Adapter<BlogListAdapter.RecyclerViewViewHolder> {

    private Context mContext;
    private List<Blog> mBlogs;

    private RecyclerViewListener mRecyclerViewListener;

    public BlogListAdapter(Context context, List<Blog> blogs) {
        mContext = context;
        mBlogs = blogs;
    }

    /**
     * 设置新的数据源，提醒adapter更新
     */
    public void refreshData(List<Blog> blogs) {
        mBlogs = blogs;
        this.notifyDataSetChanged();
    }

    /**
     * 自定义ViewHolder
     */
    public static class RecyclerViewViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView mUserHead;
        private TextView mTitle;
        private TextView mDescription;
        private TextView mTime;
        private TextView mBest;
        private TextView mComment;
        private TextView mBrowse;
        private ImageButton mMore;

        public RecyclerViewViewHolder(View view) {
            super(view);
            mUserHead = (RoundedImageView) view.findViewById(R.id.imageview_userhead);
            mTitle = (TextView) view.findViewById(R.id.tv_title);
            mDescription = (TextView) view.findViewById(R.id.tv_description);
            mTime = (TextView) view.findViewById(R.id.tv_time);
            mBest = (TextView) view.findViewById(R.id.tv_best);
            mComment = (TextView) view.findViewById(R.id.tv_comment);
            mBrowse = (TextView) view.findViewById(R.id.tv_browse);
            mMore = (ImageButton) view.findViewById(R.id.imagebutton_more);
        }
    }

    /**
     * 创建viewholder
     */
    @Override
    public RecyclerViewViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_item_bloglist_content, viewGroup, false);
        return new RecyclerViewViewHolder(view);
    }

    /**
     * 根据资源ID返回Bitmap对象
     */
    public Bitmap getBitmapFromRes(int resId) {
        Resources res = mContext.getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(res, resId);
        return bitmap;
    }

    /**
     * 绑定数据
     */
    @Override
    public void onBindViewHolder(RecyclerViewViewHolder viewHolder, int position) {
        //设置头像
        if(mBlogs.get(position).getAuthorAvatar() != null &&!"".equals(mBlogs.get(position).getAuthorAvatar())) {
            //ImageCacheManager.loadImage(mBlogs.get(position).getAuthorAvatar(), viewHolder.mUserHead, getBitmapFromRes(R.mipmap.avatar_default), getBitmapFromRes(R.mipmap.avatar_default));
            viewHolder.mUserHead.setImageResource(R.mipmap.avatar_default);
        } else {
            viewHolder.mUserHead.setImageResource(R.mipmap.avatar_default);
        }
        viewHolder.mTitle.setText(mBlogs.get(position).getBlogTitle());
        viewHolder.mDescription.setText(mBlogs.get(position).getBlogSummary());
        viewHolder.mBest.setText(mBlogs.get(position).getBlogDiggs());
        viewHolder.mComment.setText(mBlogs.get(position).getBlogComments());
        viewHolder.mBrowse.setText(mBlogs.get(position).getBlogViews());
        //处理日期特殊格式
        //String date = TimeUtil.DateToChineseString(TimeUtil.ParseUTCDate(mBlogs.get(position).getBlogPublishedTime()));
        viewHolder.mTime.setText("date");

        //设置监听时间
        viewHolder.itemView.setTag(position);
        viewHolder.mMore.setTag(Integer.MAX_VALUE);
        viewHolder.itemView.setOnClickListener(new ItemClick());
        viewHolder.mMore.setOnClickListener(new ItemClick());

    }

    @Override
    public int getItemCount() {
        return mBlogs.size();
    }

    public interface RecyclerViewListener {
        void setOnClickListener(View view, int position);
    }

    public void setmRecyclerViewListener(RecyclerViewListener recyclerViewListener) {
        mRecyclerViewListener = recyclerViewListener;
    }

    public class ItemClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(mRecyclerViewListener != null) {
                mRecyclerViewListener.setOnClickListener(view, (int)view.getTag());
            }
        }
    }
}
