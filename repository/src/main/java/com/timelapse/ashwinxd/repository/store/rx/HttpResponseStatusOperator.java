package com.timelapse.ashwinxd.repository.store.rx;

import android.util.Log;
import com.timelapse.ashwinxd.model.model.serializer.CustomObjectMapper;
import com.timelapse.ashwinxd.model.model.Event;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.subjects.PublishSubject;

/**
 * Created by ashwinxd on 18/4/17.
 */

public class HttpResponseStatusOperator<T>
    implements Observable.Operator<T, Response<T>> {
  private final String TAG = this.getClass().getSimpleName();
  private CustomObjectMapper objectMapper;
  private PublishSubject<Event> publishBus;

  public HttpResponseStatusOperator(CustomObjectMapper objectMapper,
      PublishSubject<Event> publishSystemBus) {
    this.objectMapper = objectMapper;
    this.publishBus = publishBus;
  }

  private static final String LOG_TAG = "HttpResponseStatusOpr";

  @Override public Subscriber<? super Response<T>> call(final Subscriber<? super T> subscriber) {
    return new Subscriber<Response<T>>(subscriber) {
      @Override public void onCompleted() {
        if (!subscriber.isUnsubscribed()) subscriber.onCompleted();
      }

      @Override public void onError(Throwable e) {
        System.out.println("Exception class" + e.getClass());
        if (subscriber.isUnsubscribed()) return;
        Log.e(LOG_TAG, "Exception in rest api call", e);
        subscriber.onError(e);
        subscriber.onCompleted();
      }

      @Override public void onNext(Response<T> response) {
        if (subscriber.isUnsubscribed()) return;
        if (response.isSuccessful()) {
          subscriber.onNext(response.body());
        }
      }
    };
  }
}
