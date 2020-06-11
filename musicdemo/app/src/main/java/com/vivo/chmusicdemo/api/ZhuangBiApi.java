package com.vivo.chmusicdemo.api;


import com.vivo.chmusicdemo.bean.ZhuangBiImage;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ZhuangBiApi {

    @GET("search")
    Observable<List<ZhuangBiImage>> search(@Query("q")String query);
}
