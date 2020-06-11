package com.vivo.chmusicdemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.vivo.chmusicdemo.activity.AuthorActivity;
import com.vivo.chmusicdemo.R;
import com.vivo.chmusicdemo.utils.blog.Author;
import com.vivo.chmusicdemo.utils.blog.Blog;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class AuthorListAdapter extends RecyclerView.Adapter<AuthorListAdapter.ViewHolder> {

    private static final String TAG = "SearchActivity recycleview";

    private List<Author> mAuthorList;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView mUserHead;
        private TextView mName;
        private TextView mUri;
        private TextView mTime;
        private TextView mSum;
        private ImageButton mMore;

        public ViewHolder(View view) {
            super(view);
            mUserHead = (RoundedImageView) view.findViewById(R.id.imageview_userhead);
            mName = (TextView)view.findViewById(R.id.tv_title);
            mUri = (TextView)view.findViewById(R.id.tv_description);
            mTime = (TextView)view.findViewById(R.id.tv_time);
            mSum = (TextView)view.findViewById(R.id.tv_sum);
            mMore = (ImageButton) view.findViewById(R.id.imagebutton_more);
        }
    }

    public AuthorListAdapter(List<Author> authors, Context context) {
        mAuthorList = authors;
        mContext = context;
    }

    /**
     * 设置新的数据，提醒Adapter更新
     * @param authors
     */
    public void refreshData(List<Author> authors) {
        mAuthorList = authors;
        this.notifyDataSetChanged();
//        Log.d(TAG, "refreshData: after handler, authors =  " + authors.toString());
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_item_authorlist_content, viewGroup, false);
        return new ViewHolder(view);
    }

    /**
     * 根据资源ID返回bitmap图像
     */
    public Bitmap getBitmapFromRes(int resId) {
        Resources res = mContext.getResources();
        return BitmapFactory.decodeResource(res, resId);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        if(mAuthorList.get(position).getAuthorPic() != null && !"".equals(mAuthorList.get(position).getAuthorPic())) {
//            ImageCacheManager.loadImage(mAuthorList.get(position).getAuthorPic(), viewHolder.mUserHead,
//                    getBitmapFromRes(R.mipmap.avatar_default), getBitmapFromRes(R.mipmap.avatar_default));
            viewHolder.mUserHead.setImageResource(R.mipmap.avatar_default);
        } else {
            viewHolder.mUserHead.setImageResource(R.mipmap.avatar_default);
        }
        viewHolder.mName.setText(mAuthorList.get(position).getAuthorName());
        viewHolder.mUri.setText(mAuthorList.get(position).getAuthorUri());
        viewHolder.mSum.setText(mAuthorList.get(position).getBolgCount());
        viewHolder.mTime.setText(mAuthorList.get(position).getUpdate());
        viewHolder.mName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToBlogAuthor(position);
            }
        });
        viewHolder.mUri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToBlogAuthor(position);
            }
        });
        viewHolder.mUserHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToBlogAuthor(position);
            }
        });
        viewHolder.mMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "mMore", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "mMore");
            }
        });

        //处理日期
        //String date = TimeUtil.DateToChineseString(TimeUtil.ParseUTCDate(mAuthorList.get(position).getUpdate()));
        viewHolder.mTime.setText("date");

//        //设置点击监听
//        viewHolder.itemView.setTag(position);
//        viewHolder.mMore.setTag(position);
//        viewHolder.itemView.setOnClickListener(new ItemClick());
//        viewHolder.mMore.setOnClickListener(new ItemClick());
    }


    public void jumpToBlogAuthor(int position) {
//        Toast.makeText(mContext, "jumpToBlogAuthor", Toast.LENGTH_SHORT).show();
        //封装Blog对象传递
        Blog blog = new Blog();
        //这里注意一下，博客不一样
        blog.setBlogLink(mAuthorList.get(position).getBlogApp());
        blog.setAuthorName(mAuthorList.get(position).getAuthorName());
        Intent intent = new Intent(mContext, AuthorActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("blog", blog);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
//        Toast.makeText(mContext, "Author", Toast.LENGTH_SHORT).show();
//        try {
//            mContext.startActivity(intent);
//            Toast.makeText(mContext, "Author", Toast.LENGTH_SHORT).show();
//        } catch (ActivityNotFoundException e) {
//            e.printStackTrace();
//        }

    }



    @Override
    public int getItemCount() {
        return mAuthorList.size();
    }

    public class ItemClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if(mRecyclerViewListener != null) {
                mRecyclerViewListener.setOnClickListener(view, String.valueOf(view.getTag()));
            }
        }
    }

    private RecyclerViewListener mRecyclerViewListener;
    /**
     * 自定义回调接口
     */
    public interface RecyclerViewListener {
        void setOnClickListener(View view, String value);
    }

    public void setmRecyclerViewListener (RecyclerViewListener recyclerViewListener) {
        mRecyclerViewListener = recyclerViewListener;
    }
}
