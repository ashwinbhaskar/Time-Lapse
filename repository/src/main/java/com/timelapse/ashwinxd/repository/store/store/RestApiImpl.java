package com.timelapse.ashwinxd.repository.store.store;

import com.timelapse.ashwinxd.model.model.Event;
import com.timelapse.ashwinxd.model.model.serializer.CustomJacksonConverterFactory;
import com.timelapse.ashwinxd.model.model.serializer.CustomObjectMapper;
import com.timelapse.ashwinxd.repository.store.rx.HttpResponseStatusOperator;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by ashwinxd on 15/4/17.
 */
@Singleton
public class RestApiImpl implements RestApi {

  private RestApi restApi;
  private final String host = "https://s3-us-west-2.amazonaws.com";
  int CONNECT_TIMEOUT_MILLIS = 10000;
  int READ_TIMEOUT_MILLIS = 10000;
  HttpResponseStatusOperator httpResponseStatusOperator;

  @Inject public RestApiImpl(CustomObjectMapper customObjectMapper, @Named("appPublishBus")
      PublishSubject<Event> publishSystemBus){
    final OkHttpClient.Builder builder =
        new OkHttpClient.Builder().connectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            .readTimeout(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
    Retrofit retrofit = new Retrofit.Builder().client(builder.build())
        .baseUrl(host)
        .addConverterFactory(CustomJacksonConverterFactory.create(customObjectMapper))
        .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
        .build();
    restApi = retrofit.create(RestApi.class);
    httpResponseStatusOperator = new HttpResponseStatusOperator(customObjectMapper,publishSystemBus);
  }


  @Override public Observable<Response<ResponseBody>> getTimeStampedIPs() {
    return restApi.getTimeStampedIPs().lift(httpResponseStatusOperator);
  }
}
