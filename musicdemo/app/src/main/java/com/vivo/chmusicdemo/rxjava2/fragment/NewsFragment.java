package com.vivo.chmusicdemo.rxjava2.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vivo.chmusicdemo.R;
import com.vivo.chmusicdemo.api.NetWorkApi;
import com.vivo.chmusicdemo.bean.GankNewsEntity;
import com.vivo.chmusicdemo.bean.GankNewsResultEntity;
import com.vivo.chmusicdemo.rxjava2.adapter.GankNewsAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class NewsFragment extends Fragment implements View.OnClickListener{

    private RecyclerView mNewsRv;
    private TextView mRefresh;
    private List<GankNewsResultEntity> mNewsList = new ArrayList<>();
    private GankNewsAdapter mNewsAdapter;

    private static final int NEWS_PER_PAGE = 10;
    private static final String IOS_NEWS = "ios";
    private static final String ANDROID_NEWS = "android";
    private int mCurrentPage = 1;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rxjava_news, null, false);
        initView(view);
        loadData();
        return view;
    }

    private void initView(View view) {
        mRefresh = view.findViewById(R.id.refresh);
        mRefresh.setOnClickListener(this);
        mNewsRv = view.findViewById(R.id.news_rv);
        mNewsRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mNewsAdapter = new GankNewsAdapter(getActivity(), mNewsList);
        mNewsRv.setAdapter(mNewsAdapter);
    }

    private Observable<GankNewsEntity> getGankNews(String type, int count, int page) {
        return NetWorkApi.getGankApi().getNews(type, count, page);
    }

    private void loadData(int page) {
        Observable<List<GankNewsResultEntity>> observable = Observable.just(page)
                .subscribeOn(Schedulers.io())
                .
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.refresh:
                break;
        }
    }
}
