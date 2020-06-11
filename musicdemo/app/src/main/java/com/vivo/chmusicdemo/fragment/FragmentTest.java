package com.vivo.chmusicdemo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vivo.chmusicdemo.R;
import com.vivo.chmusicdemo.utils.blog.Blog;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentTest extends Fragment {

    private View mView;
    private List<Blog> mBlogs = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private FragmentAdapterTest mFragmentAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedBundleState) {
        mView = inflater.inflate(R.layout.fragment_bloglist, container, false);
        initBlogs();
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.rv_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFragmentAdapter = new FragmentAdapterTest(mBlogs);
        mRecyclerView.setAdapter(mFragmentAdapter);
        return mView;
    }

    public void initBlogs() {
        String summary ="浏览器类型判断方法有两种：根据浏览器特性来判断根据来检测具体使用哪种方法要看具体需求的场景场景一：为了让用户有较流畅完整的体验，";
        Blog blog = new Blog("1", "题目", summary,"time", "name", "Avatar", "uri", "link", "20", "15", "30");

        for(int i = 0; i < 20; i++) {
            mBlogs.add(blog);
        }
    }
}
