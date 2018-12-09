package com.wangkeke.goomvpdemo.net;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("top250")
    Observable<Object> getTop250(@Query("start") int start, @Query("count")int count);
}
