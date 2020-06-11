package com.vivo.chmusicdemo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
import com.mugen.Mugen;
import com.mugen.MugenCallbacks;
import com.mugen.attachers.BaseAttacher;
import com.vivo.chmusicdemo.activity.BlogContentActivity;
import com.vivo.chmusicdemo.R;
import com.vivo.chmusicdemo.utils.blog.Blog;
import com.vivo.chmusicdemo.utils.blog.BlogContentXmlParser;
import com.vivo.chmusicdemo.utils.blog.BlogsListXmlParser;
import com.vivo.chmusicdemo.adapter.BlogListAdapter;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//import static com.android.volley.Request.*;

public class BlogListFragment extends Fragment {
    private static final String TAG = "BlogListFragment";

    private static final int INIT_PAGE = 1;
    private static final int ONEPAGE_BLOG_NUMBER = 20;

    private View mView;
    //下拉刷新
    private SwipeRefreshLayout mRefreshLayout;
    //无线滚动
    private BaseAttacher mBaseAttacher;

    private RecyclerView mRecyclerView;
    private BlogListAdapter mBlogListAdapter;

    //数据源
    private List<Blog> mBlogs;

    //是否正在加载
    private boolean mIsLoading = false;
    //当前页数
    private int mCurrentPage = INIT_PAGE;
    //博客详情页
    private String mBlogContentUrl = "";

    private static class RefreshHandler extends Handler {
        private final WeakReference<BlogListFragment> mFragment;

        public RefreshHandler(BlogListFragment fragment) {
            mFragment = new WeakReference<BlogListFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            BlogListFragment fragment = mFragment.get();
            fragment.mBlogListAdapter.refreshData(fragment.mBlogs);
        }
    }

    private final RefreshHandler myRefreashHandler = new RefreshHandler(this);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_bloglist, container, false);//我自己做的修改
        initView();
        initData();
        initAction();
        return mView;
    }

    /**
     * 初始化控件监听
     */
    private void initAction() {
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(INIT_PAGE, ONEPAGE_BLOG_NUMBER);
//                Log.d(TAG, "mRefreshLayout page :" + mCurrentPage);
            }
        });
        //设置无限滚动，上啦加载
        mBaseAttacher = Mugen.with(mRecyclerView, new MugenCallbacks() {
            @Override
            public void onLoadMore() {
                //加载更多
                mIsLoading = false;
                mCurrentPage = mCurrentPage + 1;
//                Log.d(TAG, "BaseAttacher page :" + mCurrentPage);
                getData(mCurrentPage, ONEPAGE_BLOG_NUMBER);
            }

            @Override
            public boolean isLoading() {
                return mIsLoading;
            }

            @Override
            public boolean hasLoadedAllItems() {
                return false;
            }
        }).start();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mBlogs = new ArrayList<Blog>();
        //设置空数据给recyclerView
        mBlogListAdapter = new BlogListAdapter(getActivity(), mBlogs);
        mRecyclerView.setAdapter(mBlogListAdapter);
        //显示下拉刷新样式
        mRefreshLayout.setRefreshing(true);
        getData(INIT_PAGE, ONEPAGE_BLOG_NUMBER);

        mBlogListAdapter.setmRecyclerViewListener(new BlogListAdapter.RecyclerViewListener() {
            @Override
            public void setOnClickListener(View view, int position) {
                if(view.getId() == R.id.imagebutton_more) {

                } else {
                    String blogID = mBlogs.get(position).getBlogId();
                    Log.d(TAG, "BlogID: " + blogID);
                    getBlogUrl(blogID);
//                    Log.d(TAG, "BlogContentUrl: " + mBlogContentUrl);
//                    Intent intent = new Intent(getActivity(), BlogContentActivity.class);
//                    intent.putExtra("blogcontent",mBlogContentUrl);
//                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipe_refresh);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.rv_view);

        //设置下拉刷新滚动条颜色
        mRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);
        //设置RecyclerView显示样式
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public void getBlogUrl(final String position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String url = " http://wcf.open.cnblogs.com/blog/post/body/" + position;
                    OkHttpClient client = new OkHttpClient();
                    Request okRequest = new Request.Builder().url(url).build();
                    Response okResponse = client.newCall(okRequest).execute();
                    String responseData = okResponse.body().string();
                    mBlogContentUrl = BlogContentXmlParser.getBlogContent(responseData);
                    Log.d(TAG, "getBlogURl: " );
                    Intent intent = new Intent(getActivity(), BlogContentActivity.class);
                    intent.putExtra("blogcontent",mBlogContentUrl);
                    startActivity(intent);
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void getData(final int page, final int num) {
        this.mCurrentPage = page;
        Log.d(TAG, "getData page :" + mCurrentPage);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try{
//                    String url = " http://wcf.open.cnblogs.com/blog/sitehome/paged/" + page + "/" + num;
//                    OkHttpClient client = new OkHttpClient();
//                    Request okRequest = new Request.Builder().url(url).build();
//                    Response okResponse = client.newCall(okRequest).execute();
//                    String responseData = okResponse.body().string();
//                    List<Blog> blogs = BlogsListXmlParser.getListBlogsOKHttp(responseData);
//
//                    if (page == INIT_PAGE) {
//                        //清空之前的數據防止重複加載
//                        mBlogs.clear();
//                    }
//                    for (Blog blog : blogs) {
//                        mBlogs.add(blog);
//                    }
//
//                    if (mBlogListAdapter == null) {
//                        //如果Adapter不存在
//                        mBlogListAdapter = new BlogListAdapter(getActivity(), mBlogs);
//                        mRecyclerView.setAdapter(mBlogListAdapter);
//                    } else {
//                        Message msg = Message.obtain();
//                        myRefreashHandler.sendMessage(msg);
//                    }
//
//                    //關閉下拉刷新
//                    mRefreshLayout.setRefreshing(false);
//                } catch (XmlPullParserException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }
}
