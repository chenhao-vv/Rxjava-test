package com.vivo.chmusicdemo.api;

import com.vivo.chmusicdemo.bean.GankBeautyResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GankApi {

    @GET("data/福利/{number}/{page}")
    Observable<GankBeautyResult> getBeauty(@Path("number")int number, @Path("page")int page);


}
