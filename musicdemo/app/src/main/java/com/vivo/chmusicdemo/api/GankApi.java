package com.vivo.chmusicdemo.api;

import com.vivo.chmusicdemo.bean.GankBeautyResult;
import com.vivo.chmusicdemo.bean.GankNewsEntity;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GankApi {

    @GET("data/福利/{number}/{page}")
    Observable<GankBeautyResult> getBeauty(@Path("number")int number, @Path("page")int page);

    @GET("data/{category}/{count}/{page}")
    Observable<GankNewsEntity> getNews(@Path("category")String type, @Path("count") int count, @Path("page")int page);


}
