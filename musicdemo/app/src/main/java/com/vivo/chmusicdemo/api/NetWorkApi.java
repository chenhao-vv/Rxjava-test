package com.vivo.chmusicdemo.api;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetWorkApi {

    private static String ZHUANGBI_URL_BASE = "http://www.zhuangbi.info/";
    private static String GANK_URL_BASE = "http://gank.io/api/";

    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static ZhuangBiApi zhuangBiApi;
    private static GankApi gankApi;
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJava2CallAdapterFactory.create();

    public static GankApi getGankApi() {
        if(gankApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .baseUrl(GANK_URL_BASE)
                    .build();
            gankApi = retrofit.create(GankApi.class);
        }
        return gankApi;
    }

    public static ZhuangBiApi getZhuangBiApi() {
        if (zhuangBiApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ZHUANGBI_URL_BASE)
                    .client(okHttpClient)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            zhuangBiApi = retrofit.create(ZhuangBiApi.class);
        }
        return zhuangBiApi;
    }

}
