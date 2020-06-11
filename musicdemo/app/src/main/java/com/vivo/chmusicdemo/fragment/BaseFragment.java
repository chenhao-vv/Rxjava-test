package com.vivo.chmusicdemo.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.View;

public abstract class BaseFragment extends Fragment {

    private static final String TAG = "BaseFragment";

    private boolean mIsFragmentVisible;
    private boolean mIsReuseView;
    private boolean mIsFirstVisible;
    private View mRootView;

    private void initVariable() {
        mIsFragmentVisible = false;
        mIsFirstVisible = true;
        mRootView = null;
        mIsReuseView = true;
    }

    /**
     * 该方法在fragment创建时首先被调用一次，此时不可见（false）
     * 如果可见，该方法会再次调用，（true）
     * 可见-->不可见，调用，（false）
     * 总结：该方法除了fragment 的可见状态发生变化时会回调，new Fragment也会回调
     * 因此重新封装一下
     * @param userVisibleHint
     */
    @Override
    public void setUserVisibleHint(boolean userVisibleHint) {
        super.setUserVisibleHint(userVisibleHint);

        if(mRootView == null) {
            return;
        }
        //首次可见
        if(mIsFirstVisible && userVisibleHint) {
            onFragmentFirstVisible();
            mIsFirstVisible = false;
        }
        if(userVisibleHint) {
            onFragmentVisibleChanged(true);
            mIsFragmentVisible = true;
            return;
        }
        if(mIsFragmentVisible) {
            mIsFragmentVisible = false;
            onFragmentVisibleChanged(false);
        }
    }

    /**
     * 在 fragment首次可见时回调，可在这里加载数据，保证只有第一次打开fragment时才会加载数据
     * 防止多次加载
     * 该方法在onFragmentChanged之前调用
     */
    public void onFragmentFirstVisible() {

    }

    /**
     * 去除setUserVisibleHint()多余的回调，保证只有当前fragment可见状态发生变化才回调
     * 回调实际在View创建后，此时支持UI操作
     * 可在该方法添加UI显示与隐藏，比如加载框的显示与隐藏
     * @param isVisible  true   不可见-->可见
     *                   false   可见--->不可见
     */
    public void onFragmentVisibleChanged(boolean isVisible) {

    }

    protected boolean isFragmentVisible() {
        return mIsFragmentVisible;
    }

    /**
     * 设置View是否复用，默认开启
     * view复用是指，viewpager在销毁和创建fragment时会不断调用onCreateView()-->onDestroyView（）
     * 之间的生命函数，这样可能会出现重复创建View的情况，导致界面上显示多个相同的fragment
     * View的复用就是保存第一次创建的view，后面在onCreateView时直接返回第一次创建的View
     * @param isReuse
     */
    protected void reuseView(boolean isReuse) {
        mIsReuseView = isReuse;
    }

    @Override
    public void onViewCreated(View view, Bundle savedBundleStated) {
        Log.d(TAG, " onCreateView()");

        //如果setUserVisibleHint在rootview创建之前调用时，那么
        //就等到rootview创建完才回调onFragmentVisibleChanged
        //保证onFragmentVisibleChanged的回调发生在rootview创建完成之后，以便支持UI
        if(mRootView == null) {
            mRootView = view;
            if(getUserVisibleHint()) {
                if(mIsFirstVisible) {
                    onFragmentFirstVisible();
                    mIsFirstVisible = false;
                }
                onFragmentVisibleChanged(true);
                mIsFragmentVisible = true;
            }
        }
        super.onViewCreated(mIsReuseView ? mRootView : view, savedBundleStated);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        Log.d(TAG, " onAttach()");
    }

    @Override
    public void onCreate(Bundle savedBundleState) {
        super.onCreate(savedBundleState);
        initVariable();//初始化变量
        Log.d(TAG, " onCreate()");
    }

    @Override
    public void onActivityCreated(Bundle savedBundleState) {
        super.onActivityCreated(savedBundleState);
        Log.d(TAG, " onActivityCreated()");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, " onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, " onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, " onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, " onStop()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, " onDestroyView()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        initVariable();//重置变量
        Log.d(TAG, " onDestroy()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, " onDetach()");
    }


}
