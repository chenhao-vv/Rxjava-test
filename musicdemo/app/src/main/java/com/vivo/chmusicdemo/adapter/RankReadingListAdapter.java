package com.vivo.chmusicdemo.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vivo.chmusicdemo.R;
import com.vivo.chmusicdemo.activity.AuthorHomeActivity;
import com.vivo.chmusicdemo.utils.blog.Blog;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class RankReadingListAdapter extends RecyclerView.Adapter<RankReadingListAdapter.ViewHolder> implements View.OnClickListener{

    private static final String TAG = "RankReadingListAdapter";
    private Context mContext;
    private List<Blog> mBlogList;

    public RankReadingListAdapter(Context context, List<Blog> blogs) {
        mContext = context;
        mBlogList = blogs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_rankreading, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Log.d(TAG, "position = " + position);
        if(mBlogList != null && position <= mBlogList.size()) {
//            viewHolder.mUserImg.setImageBitmap();
            viewHolder.mAuthorName.setText(mBlogList.get(position).getAuthorName());
            //时间格式转换
            viewHolder.mPublishTime.setText(mBlogList.get(position).getBlogPublishedTime());
            viewHolder.mArticleTitle.setText(mBlogList.get(position).getBlogTitle());
            viewHolder.mArticleTitle.setOnClickListener(this);
            viewHolder.mArticleTitle.setTag(position);
            viewHolder.mArticleSummary.setText(mBlogList.get(position).getBlogSummary());
            viewHolder.mArticleSummary.setOnClickListener(this);
            viewHolder.mArticleSummary.setTag(position);
            viewHolder.mRecommend.setText(mContext.getString(R.string.tuijian) + mBlogList.get(position).getBlogDiggs());
            viewHolder.mComment.setText(mContext.getString(R.string.pinglun) + mBlogList.get(position).getBlogComments());
            viewHolder.mViewer.setText(mContext.getString(R.string.liulan) + mBlogList.get(position).getBlogViews());
            viewHolder.mUserInfo.setOnClickListener(this);
            viewHolder.mUserInfo.setTag(position);
        }

    }

    @Override
    public int getItemCount() {
        if(mBlogList != null) {
            return mBlogList.size();
        } else {
            Log.d(TAG, "RankReadingListAdapter blogs is null");
            return -1;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mUserImg;
        private TextView mAuthorName;
        private TextView mPublishTime;
        private TextView mArticleTitle;
        private TextView mArticleSummary;
        private TextView mRecommend;
        private TextView mComment;
        private TextView mViewer;
        private LinearLayout mUserInfo;

        public ViewHolder(View view) {
            super(view);
            mUserImg = (ImageView) view.findViewById(R.id.user_image);
            mAuthorName = (TextView) view.findViewById(R.id.author_name);
            mPublishTime = (TextView) view.findViewById(R.id.publish_time);
            mArticleTitle = (TextView) view.findViewById(R.id.article_title);
            mArticleSummary = (TextView) view.findViewById(R.id.article_summary);
            mRecommend = (TextView) view.findViewById(R.id.recommend);
            mComment = (TextView) view.findViewById(R.id.comment);
            mViewer = (TextView) view.findViewById(R.id.viewer);
            mUserInfo = (LinearLayout) view.findViewById(R.id.user_info);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_info:
                int position =(int) view.getTag();
                Intent homeIntent = new Intent(mContext, AuthorHomeActivity.class);
                homeIntent.putExtra(AuthorHomeActivity.BLOG_APP, mBlogList.get(position).getAuthorName());
                try{
                    mContext.startActivity(homeIntent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "jump to user home page");
                break;
            case R.id.article_title:
            case R.id.article_summary:
                Log.d(TAG, "jump to the article detail page");
                break;
            default:
                break;
        }
    }
}
