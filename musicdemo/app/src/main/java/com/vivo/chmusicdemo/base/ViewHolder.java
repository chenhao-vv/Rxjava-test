package com.vivo.chmusicdemo.base;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "commonAdapter";

    private SparseArray<View> mViews;
    private Context mContext;
    private View mConvertView;

    public ViewHolder(Context context, View view) {
        super(view);
        mContext = context;
        mConvertView = view;
        mViews = new SparseArray<>();
    }

    public static ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int layoutId) {
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new ViewHolder(context, view);
    }

    public static ViewHolder onCreateViewHolder(Context context, View view) {
        return new ViewHolder(context, view);
    }

    /**
     * 根据ViewID获得控件
     */
    public <T extends View > T getView(int viewId) {
        View view = mViews.get(viewId);
        if(view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T)view;
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 以下均为辅助方法
     */
    public void setText(int viewId, String text) {
        TextView textView = getView(viewId);
        textView.setText(text);
    }

    public void setTextColor(int viewId, int textColor) {
        TextView textView = getView(viewId);
        textView.setTextColor(textColor);
    }

}
