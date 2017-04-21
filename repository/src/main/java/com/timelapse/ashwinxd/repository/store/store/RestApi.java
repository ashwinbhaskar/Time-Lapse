package com.timelapse.ashwinxd.repository.store.store;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by ashwinxd on 15/4/17.
 */

public interface RestApi {

  @GET("greedygamemedia/test/test_ip_ts.txt") Observable<Response<ResponseBody>> getTimeStampedIPs();
}
