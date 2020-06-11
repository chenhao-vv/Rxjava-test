package com.vivo.chmusicdemo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
import com.mugen.Mugen;
import com.mugen.MugenCallbacks;
import com.mugen.attachers.BaseAttacher;
import com.vivo.chmusicdemo.R;
import com.vivo.chmusicdemo.adapter.AuthorListAdapter;
import com.vivo.chmusicdemo.utils.blog.Author;
import com.vivo.chmusicdemo.utils.blog.AuthorXmlParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";

    private EditText mEditText;
    private ImageButton mImageButton;
    private RecyclerView mRecyclerView;
    private MaterialDialog mMaterialDialog;

    //无限滚动
    private BaseAttacher mBaseAttacher;

    private AuthorListAdapter mAuthorListAdapter;
    private List<Author> mAuthors;

    private boolean mIsLoading = false;
    private int mCurrentPage = INIT_PAGR;
    private static final int INIT_PAGR = 1;
    private static final int PAGE_NUM = 10;

    private static final int FIRST_THREAD = 1;
    private static final int SECOND_THREAD = 2;
    private static final int THIRD_THREAD = 3;

    private MyHandler myHandlerThread = new MyHandler(this);

    @Override
    public void onCreate(Bundle savedBundelState) {
        super.onCreate(savedBundelState);
        setContentView(R.layout.activity_search);
        initView();
        initData();
        initAction();
    }

    public void initView() {
        mEditText = (EditText)findViewById(R.id.edit_search);
        mImageButton = (ImageButton) findViewById(R.id.imagebutton_search);
        mRecyclerView = (RecyclerView)findViewById(R.id.recycle_search);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //固定高度，提高效率
        mRecyclerView.setHasFixedSize(true);
    }

    public void initData() {
        mAuthors = new ArrayList<Author>();
        mAuthorListAdapter = new AuthorListAdapter(mAuthors, this);
        mRecyclerView.setAdapter(mAuthorListAdapter);
        getData(mCurrentPage, PAGE_NUM);
    }

    public void initAction() {
        //无线滚动，上拉加载
        mBaseAttacher = Mugen.with(mRecyclerView, new MugenCallbacks() {
            @Override
            public void onLoadMore() {
                //加载更多
                mIsLoading = false;
                mBaseAttacher.setLoadMoreEnabled(false);
                getData(mCurrentPage + 1, PAGE_NUM);
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
        //离底部一项的时候加载更多
        mBaseAttacher.setLoadMoreOffset(INIT_PAGR);

        //点击搜索
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mEditText.getText().toString();
                if(!TextUtils.isEmpty(name)) {
                    mMaterialDialog = new MaterialDialog.Builder(SearchActivity.this).content(getString(R.string.content_searching)).progress(true, 0).show();
                    getDataByName(name.trim());
                } else {

                }

            }
        });

//        //recyclerview点击监听
//        mAuthorListAdapter.setmRecyclerViewListener(new AuthorListAdapter.RecyclerViewListener() {
//            @Override
//            public void setOnClickListener(View view, String value) {
//                //封装Blog对象传递
//                Blog blog = new Blog();
//                //这里注意一下，博客不一样
//                blog.setBlogLink(mAuthors.get(Integer.parseInt(value)).getBlogApp());
//                blog.setAuthorName(mAuthors.get(Integer.parseInt(value)).getAuthorName());
//                Intent intent = new Intent(SearchActivity.this, AuthorActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("blog", blog);
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//        });
    }

    private static class MyHandler extends Handler {
        private WeakReference<SearchActivity> mSearchActivity;

        public MyHandler(SearchActivity searchActivity) {
            mSearchActivity = new WeakReference<SearchActivity>(searchActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            SearchActivity searchActivity = mSearchActivity.get();
            Log.d(TAG, "handler Message");
            switch (msg.what) {
                case FIRST_THREAD:
                    Log.d(TAG, "handler Message refresh Data");
                    searchActivity.mAuthorListAdapter.refreshData(searchActivity.mAuthors);
                    break;
                case SECOND_THREAD:
                case THIRD_THREAD:
                    Log.d(TAG, "handler Message refresh Data  dismiss");
                    searchActivity.mMaterialDialog.dismiss();
                    break;
                default:
                    break;

            }
        }


    }
    //根据博主昵搜索
    public void getDataByName(final String name) {
        Log.d(TAG,"getDataByName");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG,"getDataByName  Thread");
                try {
                    String uri =  "http://wcf.open.cnblogs.com/blog/bloggers/search?t=" + name;
                    OkHttpClient client = new OkHttpClient();
                    Request okRequest = new Request.Builder().url(uri).build();
                    Response response = client.newCall(okRequest).execute();
                    String xmlData = response.body().string();
                    List<Author> authors = AuthorXmlParser.getListAuthorOKHttp(xmlData);
                    if (authors.size() != 0) {
                        //清空之前的数据防止重复加载
                        mAuthors.clear();
                        mAuthors.addAll(authors);
                        for (Author author : authors) {
                            Log.d(TAG, "blog app = " + author.getBlogApp());
                        }

                        if (mAuthorListAdapter == null) {
                            mAuthorListAdapter = new AuthorListAdapter(mAuthors, SearchActivity.this);
                            mRecyclerView.setAdapter(mAuthorListAdapter);
                        } else {
//                            Log.d(TAG, "before Handler, authors = " + mAuthors );
                            Message firstMsg = Message.obtain();
                            firstMsg.what = FIRST_THREAD;
                            myHandlerThread.sendMessage(firstMsg);
                        }
                        Message secondMsg = Message.obtain();
                        secondMsg.what = SECOND_THREAD;
                        myHandlerThread.sendMessage(secondMsg);
                    } else {
                        //如果匹配不到关键字
                        Toast.makeText(SearchActivity.this, "找不到相关用户，请重新搜索", Toast.LENGTH_SHORT).show();
                        Message thirdMsg = Message.obtain();
                        thirdMsg.what = THIRD_THREAD;
                        myHandlerThread.sendMessage(thirdMsg);
                    }
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //获取推荐数据
    public void getData(final int page, final int num) {
        Log.d(TAG, "getdata");
        this.mCurrentPage = page;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "getDtat thread");
                try {
                    String uri = "http://wcf.open.cnblogs.com/blog/bloggers/recommend/" + page + "/" + num;
                    OkHttpClient client = new OkHttpClient();
                    Request okRequest = new Request.Builder().url(uri).build();
                    Response response = client.newCall(okRequest).execute();
                    String xmlData = response.body().string();
                    List<Author> authors = AuthorXmlParser.getListAuthorOKHttp(xmlData);
                    if (page == INIT_PAGR) {
                        mAuthors.clear();
                    }
                    for (Author author : authors) {
                        mAuthors.add(author);
                    }

                    if (mAuthorListAdapter == null) {
                        mAuthorListAdapter = new AuthorListAdapter(mAuthors, SearchActivity.this);
                        mRecyclerView.setAdapter(mAuthorListAdapter);
                    } else {
                        Message firstMsg = Message.obtain();
                        firstMsg.what = FIRST_THREAD;
                        myHandlerThread.sendMessage(firstMsg);
                    }
                    mIsLoading = false;
                    mBaseAttacher.setLoadMoreEnabled(true);
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
