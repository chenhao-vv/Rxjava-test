package com.vivo.chmusicdemo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vivo.chmusicdemo.R;
import com.vivo.chmusicdemo.utils.blog.Blog;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class AuthorHomeAdapter extends RecyclerView.Adapter<AuthorHomeAdapter.ViewHolder> implements View.OnClickListener{

    private static final String TAG = "AuthorHomeAdapter";

    private Context mContext;
    private List<Blog> mBlogList;

    public AuthorHomeAdapter(Context context, List<Blog> blogs) {
        mContext = context;
        mBlogList = blogs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup group, int typeView) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_article, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        if(mBlogList != null && position < mBlogList.size()) {
            viewHolder.mArticleTitle.setText(mBlogList.get(position).getBlogTitle());
            viewHolder.mArticleTitle.setOnClickListener(this);
            viewHolder.mArticleSummary.setText(mBlogList.get(position).getBlogSummary());
            viewHolder.mArticleSummary.setOnClickListener(this);
            viewHolder.mRecommend.setText(mContext.getString(R.string.tuijian) + mBlogList.get(position).getBlogDiggs());
            viewHolder.mComment.setText(mContext.getString(R.string.pinglun) + mBlogList.get(position).getBlogComments());
            viewHolder.mViewer.setText(mContext.getString(R.string.liulan) + mBlogList.get(position).getBlogViews());
        }
    }

    @Override
    public int getItemCount() {
        if(mBlogList != null) {
            return mBlogList.size();
        } else {
            return -1;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.article_title:
            case R.id.article_summary:
                Log.d(TAG, "jump to the artice detail page");
                break;
            default:
                break;
        }
    }



    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mArticleTitle;
        private TextView mArticleSummary;
        private TextView mRecommend;
        private TextView mComment;
        private TextView mViewer;
        private LinearLayout mUserInfo;

        public ViewHolder(View view) {
            super(view);
            mArticleTitle = (TextView) view.findViewById(R.id.article_title);
            mArticleSummary = (TextView) view.findViewById(R.id.article_summary);
            mRecommend = (TextView) view.findViewById(R.id.recommend);
            mComment = (TextView) view.findViewById(R.id.comment);
            mViewer = (TextView) view.findViewById(R.id.viewer);
            mUserInfo = (LinearLayout) view.findViewById(R.id.user_info);
        }
    }
}
