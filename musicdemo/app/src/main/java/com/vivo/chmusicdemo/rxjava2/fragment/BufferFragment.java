package com.vivo.chmusicdemo.rxjava2.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.vivo.chmusicdemo.R;

import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.PublishSubject;

public class BufferFragment extends Fragment {
    private static final String TAG = "BufferFragment";

    private TextView mTitle;
    private TextView mData;
    private Button mStart;
    private BufferHandler mHandler = new BufferHandler();
    private PublishSubject<Double> mSubject = PublishSubject.create();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rxjava_buffer, null, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mStart = view.findViewById(R.id.background_task);
        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTemp();
            }
        });
        mTitle = view.findViewById(R.id.title);
        mData = view.findViewById(R.id.tempure);
    }

    private void startTemp() {
        DisposableObserver<List<Double>> observer = new DisposableObserver<List<Double>>() {
            @Override
            public void onNext(List<Double> doubles) {
                double result = 0;
                if (doubles != null && doubles.size() != 0) {
                    for (double temp : doubles) {
                        result = result + temp;
                    }
                    result = result / doubles.size();
                }
                Log.i(TAG, "最近三面温度更新" + doubles.size() + "次， "
                        + " 平均温度为：" + result);
                mTitle.setText("过去三秒温度更新" + String.valueOf(doubles.size()) + "次， 平均温度为：");
                mData.setText(String.valueOf(result));
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        mSubject.buffer(3000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

        mHandler.sendEmptyMessage(0);

    }

    private void updateTemperature(double temp) {
        mSubject.onNext(temp);
    }

    private class BufferHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            double temp = Math.random() * 25 + 5;
            updateTemperature(temp);
            mHandler.sendEmptyMessageDelayed(0, 250 + (long) (Math.random() * 250));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
