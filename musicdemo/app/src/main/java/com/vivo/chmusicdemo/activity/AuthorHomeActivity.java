package com.vivo.chmusicdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vivo.chmusicdemo.R;
import com.vivo.chmusicdemo.utils.blog.Author;
import com.vivo.chmusicdemo.utils.blog.AuthorHomeXmlParser;
import com.vivo.chmusicdemo.utils.blog.AuthorXmlParser;
import com.vivo.chmusicdemo.utils.blog.Blog;
import com.vivo.chmusicdemo.adapter.AuthorHomeAdapter;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AuthorHomeActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "AuthorHomeActivity";
    //intent标识blogapp
    public static final String BLOG_APP = "blog_app";
    private static final int GET_BLOGAPP_BY_NAME = 1;
    private static final int GET_BLOG_BY_BLOGAPP = 2;

    private static final int INIT_PAGE = 1;
    private static final int PER_PAGE_NUMBER = 10;

    private RecyclerView mRecyclerView;
    private AuthorHomeAdapter mAdapter;
    private ImageView mUserImg;
    private TextView mAuthorName;
    private TextView mAuthorHomeUrl;
    private List<Blog> mBlogList = new ArrayList<>();
    private int mCurrentPage = INIT_PAGE;
    private Author mAuthor;
    private List<Author> mAuthors = new ArrayList<>();
    private RefreshHandler mRegreshHandler = new RefreshHandler(this);

    private ExecutorService mThreadPool = Executors.newCachedThreadPool();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_home);
        Intent homeInetent = getIntent();
        if(homeInetent != null) {
            String authorName = homeInetent.getStringExtra(BLOG_APP);
            getBlogAppFromName(authorName);
        }
        initView();
    }

    /**
     * 根据作者名字，去获取Author信息，得到blogAPP
     */
    private void getBlogAppFromName(final String name) {
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String uri =  "http://wcf.open.cnblogs.com/blog/bloggers/search?t=" + name;
                    OkHttpClient client = new OkHttpClient();
                    Request okRequest = new Request.Builder().url(uri).build();
                    Response response = client.newCall(okRequest).execute();
                    String xmlData = response.body().string();
                    List<Author> authors = AuthorXmlParser.getListAuthorOKHttp(xmlData);
                    if (authors.size() != 0) {
                        mAuthor = authors.get(0);
                        Log.d(TAG, "blog app = " + mAuthor.getBlogApp());
                        Message getBlogMsg = Message.obtain();
                        getBlogMsg.what = GET_BLOGAPP_BY_NAME;
                        mRegreshHandler.sendMessage(getBlogMsg);
                    } else {
                        //如果匹配不到关键字
                        Log.d(TAG, "can not get Blogapp from Name");
                    }
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * xml解析，获得文章列表以及其他相关信息
     */
    private void getBlogsFromBlogApp(final String blogApp, final int page) {
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try{
                    //http://wcf.open.cnblogs.com/blog/u/zhouyinhui/posts/1/10
                    String url = "http://wcf.open.cnblogs.com/blog/u/"+ blogApp + "/posts/" + page + "/" + PER_PAGE_NUMBER;
//                    String url = "http://wcf.open.cnblogs.com/blog/u/zhouyinhui/posts/1/10";
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request request = new Request.Builder().url(url).build();
                    Response response = okHttpClient.newCall(request).execute();
                    if(response != null){
                        String data = response.body().string();
                        List<Blog> blogs = AuthorHomeXmlParser.getAuthorHomeInfo(data);
                        mBlogList.clear();
                        mBlogList.addAll(blogs);
                    }
                    Message msg = Message.obtain();
                    msg.what = GET_BLOG_BY_BLOGAPP;
                    mRegreshHandler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch(XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void initView() {
        ImageView back = (ImageView) findViewById(R.id.home_back);
        back.setOnClickListener(this);
        mUserImg = (ImageView) findViewById(R.id.home_user_image);
        mAuthorName = (TextView) findViewById(R.id.home_title_after_image);
        mAuthorHomeUrl = (TextView)findViewById(R.id.homepage_url);

        mRecyclerView = findViewById(R.id.home_articles);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new AuthorHomeAdapter(this, mBlogList);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_back:
                onBackPressed();
                break;
            case R.id.homepage_url:
                break;
            default:
                break;
        }
    }

    public static class RefreshHandler extends Handler {
        private WeakReference<AuthorHomeActivity> mActivity;

        public RefreshHandler(AuthorHomeActivity authorHomeActivity) {
            mActivity = new WeakReference<>(authorHomeActivity);
        }

        private AuthorHomeActivity get() {
            return mActivity.get();
        }

        @Override
        public void handleMessage(Message message) {
            if(mActivity != null) {
                switch (message.what) {
                    case GET_BLOGAPP_BY_NAME:
                        get().getBlogsFromBlogApp(get().mAuthor.getBlogApp(), get().mCurrentPage);
                        break;
                    case GET_BLOG_BY_BLOGAPP:
                        get().mAuthorHomeUrl.setText("主页：" + get().mAuthor.getAuthorUri());
                        get().mAuthorName.setText(get().mAuthor.getAuthorName());
                        get().mAdapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
