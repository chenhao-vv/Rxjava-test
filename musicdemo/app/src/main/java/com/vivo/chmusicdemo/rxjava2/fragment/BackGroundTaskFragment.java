package com.vivo.chmusicdemo.rxjava2.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.vivo.chmusicdemo.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class BackGroundTaskFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "BackGroundTaskFragment";

    private Button mBackGroundTask;
    private TextView mResult;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rxjava_test, null, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mBackGroundTask = view.findViewById(R.id.background_task);
        mBackGroundTask.setOnClickListener(this);
        mResult = view.findViewById(R.id.result);
    }

    private void startDownLoad() {
        //后台操作
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                //模拟下载流程
                for (int i = 0; i < 100; i++) {
                    if(i%10 ==0){
                        try{
                            Thread.sleep(500);
                        } catch (Exception e){
                            if(!emitter.isDisposed()) {
                                emitter.onError(e);
                            }
                        }
                        emitter.onNext(i);
                    }
                }
                emitter.onComplete();
            }
        });

        //前台显示
        DisposableObserver<Integer> observer = new DisposableObserver<Integer>() {
            @Override
            public void onNext(Integer integer) {
                Log.i(TAG, "下载进度 = " + integer);
                mResult.setText("下载进度：" + integer.toString() + "%");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "throwable = " + e);
            }

            @Override
            public void onComplete() {
                mResult.setText("下载完成");
            }
        };

        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
        mCompositeDisposable.add(observer);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCompositeDisposable.clear();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.background_task:
                Toast.makeText(getActivity(), "开始下载", Toast.LENGTH_SHORT).show();
                startDownLoad();
                break;
        }
    }
}
