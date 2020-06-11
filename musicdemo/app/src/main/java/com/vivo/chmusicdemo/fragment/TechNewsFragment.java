package com.vivo.chmusicdemo.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vivo.chmusicdemo.R;
import com.vivo.chmusicdemo.utils.blog.Blog;
import com.vivo.chmusicdemo.utils.blog.TechNewsXmlParser;
import com.vivo.chmusicdemo.adapter.RankReadingListAdapter;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TechNewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private static final String TAG = "RankReadingFragment";

    private static final int DISTANCE_TO_REFRESH = 300;
    private static final int DATA_PER_TIME = 10;
    private static final int GET_DATA = 1;
    private static final int LOAD_MORE = 2;

    private RecyclerView mRecyclerView;
    private RankReadingListAdapter mAdapter;
    private SwipeRefreshLayout mUpRefresh;
    private View mView;
    private Context mContext;
    private List<Blog> mBlogList = new ArrayList<>();
    private ExecutorService mThreadPool = Executors.newCachedThreadPool();
    private RefreshHandler mRefreshHandler = new RefreshHandler(this);
    private int mLoadNumber = DATA_PER_TIME;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_rankreading, container, false);
        getBlogs();
        initView();
        return mView;
    }

    private void initView() {
        mContext = getActivity();
        //上拉刷新
        mUpRefresh = (SwipeRefreshLayout) mView.findViewById(R.id.rank_swipe_refresh);
        mUpRefresh.setColorSchemeColors(Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW);
        //下拉多少距离进行刷新（dp）
        mUpRefresh.setDistanceToTriggerSync(DISTANCE_TO_REFRESH);
        mUpRefresh.setOnRefreshListener(this);

        mRecyclerView = mView.findViewById(R.id.recycler_rankreading);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Log.d(TAG, "scroll to botom");
                    loadMore();
                }
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new RankReadingListAdapter(mContext, mBlogList);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void getBlogs() {
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try{
                    String url = "http://wcf.open.cnblogs.com/news/hot/" + DATA_PER_TIME;
                    OkHttpClient client = new OkHttpClient();
                    Request okRequest = new Request.Builder().url(url).build();
                    Response response = client.newCall(okRequest).execute();
                    if(response != null) {
                        String responseData = response.body().string();
                        List<Blog> blogs = TechNewsXmlParser.getTechNews(responseData);
                        mBlogList.clear();
                        mBlogList.addAll(blogs);
                        Message msg = Message.obtain();
                        msg.what = GET_DATA;
                        mRefreshHandler.sendMessage(msg);
                        mUpRefresh.setRefreshing(false);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void loadMore() {
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try{
                    mLoadNumber = mLoadNumber + 10;
                    String url = "http://wcf.open.cnblogs.com/news/hot/" + mLoadNumber;
                    OkHttpClient client = new OkHttpClient();
                    Request okRequest = new Request.Builder().url(url).build();
                    Response response = client.newCall(okRequest).execute();
                    if(response != null) {
                        String responseData = response.body().string();
                        List<Blog> blogs = TechNewsXmlParser.getTechNews(responseData);
                        mBlogList.clear();
                        mBlogList.addAll(blogs);
                        Message msg = Message.obtain();
                        msg.what = LOAD_MORE;
                        mRefreshHandler.sendMessage(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    //上拉更新的监听器
    @Override
    public void onRefresh() {
        getBlogs();
    }

    private static class RefreshHandler extends Handler {
        private WeakReference<TechNewsFragment> mFragment;

        public RefreshHandler(TechNewsFragment fragment) {
            mFragment = new WeakReference<>(fragment);
        }
        private TechNewsFragment get() {
            return mFragment.get();
        }

        @Override
        public void handleMessage(Message msg) {
            if(mFragment != null) {
                get().mAdapter.notifyDataSetChanged();
            }
        }

    }
}
