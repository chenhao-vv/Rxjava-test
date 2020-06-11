package com.vivo.chmusicdemo.utils.picture_save_volley;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public  class VolleyRequestQueueManager {
    // 获取请求队列类
    public static RequestQueue mRequestQueue = Volley.newRequestQueue(MyApplication.newInstance());

    //添加任务进任务队列
    public static void addRequest(Request<?> request, Object tag) {
        if (tag != null) {
            request.setTag(tag);
        }
        mRequestQueue.add(request);
    }

    //取消任务
    public static void cancelRequest(Object tag){
        mRequestQueue.cancelAll(tag);
    }
}
