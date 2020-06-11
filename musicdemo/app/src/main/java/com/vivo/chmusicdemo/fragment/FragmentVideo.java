package com.vivo.chmusicdemo.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vivo.chmusicdemo.R;

public class FragmentVideo extends Fragment {

    private static final String TAG = "Fragment Video";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedBundleState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        Log.d(TAG, " onCreateView()");
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean userVisibleHint) {
        super.setUserVisibleHint(userVisibleHint);
        Log.d(TAG,"是否可见 ：" + getUserVisibleHint());
    }

}
