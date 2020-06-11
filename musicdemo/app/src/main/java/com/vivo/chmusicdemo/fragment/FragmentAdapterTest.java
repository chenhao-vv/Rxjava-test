package com.vivo.chmusicdemo.fragment;

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

public class FragmentAdapterTest extends RecyclerView.Adapter<FragmentAdapterTest.ViewHolder> {

    private List<Blog> mBlogs;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView mUserHead;
        private TextView mTitle;
        private TextView mDescription;
        private TextView mTime;
        private TextView mBest;
        private TextView mComment;
        private TextView mBrowse;
        private ImageButton mMore;

        public ViewHolder(View view) {
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

    public FragmentAdapterTest(List<Blog> blogs) {
        mBlogs = blogs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup container, int type) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.recyclerview_item_bloglist_content, container, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position){
        viewHolder.mUserHead.setImageResource(R.mipmap.avatar_default);
        viewHolder.mTitle.setText(mBlogs.get(position).getBlogTitle());
        viewHolder.mDescription.setText(mBlogs.get(position).getBlogSummary());
        viewHolder.mBest.setText(mBlogs.get(position).getBlogDiggs());
        viewHolder.mComment.setText(mBlogs.get(position).getBlogComments());
        viewHolder.mBrowse.setText(mBlogs.get(position).getBlogViews());
        //处理日期特殊格式
        //String date = TimeUtil.DateToChineseString(TimeUtil.ParseUTCDate(mBlogs.get(position).getBlogPublishedTime()));
        viewHolder.mTime.setText("date");
    }

    @Override
    public int getItemCount() {
        return mBlogs.size();
    }

}
