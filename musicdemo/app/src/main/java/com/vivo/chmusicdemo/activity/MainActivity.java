package com.vivo.chmusicdemo.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.vivo.chmusicdemo.fragment.BlogListFragment;
import com.vivo.chmusicdemo.fragment.SearchImageFragment;
import com.vivo.chmusicdemo.fragment.SetThemeFragment;
import com.vivo.chmusicdemo.adapter.FragmentPageAdapter;
import com.vivo.chmusicdemo.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = "activity_main";

    private static final String NETWORK_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
    private static final int VIEWPAGER_MAIN = 0;
    private static final int VIEWPAGER_CXRS = 1;
    private static final int VIEWPAGER_VIDEO = 2;


   //检测网路
    private IntentFilter mIntentFilter;
    private NetworkChangeReceiver mNetworkChangeReceiver;

    private Toolbar mToolbar;
    private EditText mSearchEdit;
    private ImageButton mSearchButton;
    private TabLayout mTablayout;
    private ViewPager mViewpager;
    private FragmentPageAdapter mFmAdapter;
    private ArrayList<String> mTabTitles = new ArrayList<>();
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();

    private RadioGroup mRadioGroup;

    @Override
    public void onCreate(Bundle savedBundleState) {
        super.onCreate(savedBundleState);
        setContentView(R.layout.mainui_ui);

        init();
    }

    void init() {
//        mSearchEdit = (EditText)findViewById(R.id.edit_search);
//        mSearchButton = (ImageButton)findViewById(R.id.imagebutton_search);
//        mSearchButton.setOnClickListener(this);
//        mSearchEdit.setOnClickListener(this);
        mTablayout = (TabLayout)findViewById(R.id.tablayout);
        mViewpager = (ViewPager)findViewById(R.id.main_ui_fragmentpager);
        mFragmentList.add(new BlogListFragment());
        mFragmentList.add(new SetThemeFragment());
        mFragmentList.add(new SearchImageFragment());
        mTabTitles.add(getString(R.string.main_page));
        mTabTitles.add("深色模式");
        mTabTitles.add("Rxjava2-basic");
        mFmAdapter = new FragmentPageAdapter(getSupportFragmentManager(), mFragmentList, mTabTitles);
        mViewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTablayout));
        mViewpager.setAdapter(mFmAdapter);
        mTablayout.setupWithViewPager(mViewpager);
        mToolbar = (Toolbar)findViewById(R.id.activity_toolbar);
        setSupportActionBar(mToolbar);

        //广播初始化
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(NETWORK_CHANGE);
        mNetworkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(mNetworkChangeReceiver, mIntentFilter);



    }

//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.imagebutton_search:
//            case R.id.edit_search:
//                Intent FirstIntent = new Intent(this, SearchActivity.class);
//                try {
//                    startActivity(FirstIntent);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//            default:
//                break;
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_search:
                Intent FirstIntent = new Intent(this, SearchActivity.class);
                try {
                    startActivity(FirstIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.toolbar_setting:
                break;
            default:
                break;
        }
        return true;
    }

    class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo != null && networkInfo.isConnected()) {
                Toast.makeText(context, "Network is available", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Network is not available", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();        unregisterReceiver(mNetworkChangeReceiver);
    }
}
