package com.vivo.chmusicdemo.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vivo.chmusicdemo.R;
import com.vivo.chmusicdemo.adapter.SearchImageAdapter;
import com.vivo.chmusicdemo.api.NetWorkApi;
import com.vivo.chmusicdemo.bean.ZhuangBiImage;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SearchImageFragment extends Fragment {

    private EditText mEditText;
    private Button mSearch;
    private Button mQuestion;
    private SwipeRefreshLayout mSwipe;
    private View mView;
    private RecyclerView mRv;
    private SearchImageAdapter mAdapter = new SearchImageAdapter();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_search_image, container, false);
        initView();
        return mView;
    }

    private void initView() {
        mEditText = mView.findViewById(R.id.search_image);
        mSearch = mView.findViewById(R.id.search_confirm);
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               search(mEditText.getText().toString());
            }
        });
        mQuestion = mView.findViewById(R.id.question);
        mSwipe = mView.findViewById(R.id.swipe_refresh);
        mRv = mView.findViewById(R.id.search_result);
        mRv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mRv.setAdapter(mAdapter);
        mSwipe.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        mSwipe.setEnabled(false);
    }

    private void search(String key) {
        if (!TextUtils.isEmpty(key)) {
            Disposable disposable = NetWorkApi.getZhuangBiApi().search(key)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<ZhuangBiImage>>() {
                        @Override
                        public void accept(List<ZhuangBiImage> zhuangBiImages) throws Exception {
                            mSwipe.setRefreshing(false);
                            mAdapter.setDatas(zhuangBiImages);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            mSwipe.setRefreshing(false);
                            Toast.makeText(getActivity(), "数据加载失败", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getActivity(), "请输入搜索内容", Toast.LENGTH_SHORT).show();
        }
    }
}
