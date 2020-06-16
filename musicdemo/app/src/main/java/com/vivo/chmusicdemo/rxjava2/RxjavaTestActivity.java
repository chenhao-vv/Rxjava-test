package com.vivo.chmusicdemo.rxjava2;

import android.os.Bundle;

import com.vivo.chmusicdemo.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RxjavaTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
