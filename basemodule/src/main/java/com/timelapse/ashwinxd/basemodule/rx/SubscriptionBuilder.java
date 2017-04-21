package com.timelapse.ashwinxd.basemodule.rx;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.timelapse.ashwinxd.basemodule.events.ExceptionEvent;
import com.timelapse.ashwinxd.model.model.Event;
import java.net.UnknownHostException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;

/**
 * Created by ashwinxd on 18/4/17.
 */

@Singleton
public class SubscriptionBuilder implements ISubscriptionBuilder {
  @Inject Context context;
  @Inject
  @Named("analyticsBus") PublishSubject<Event> analyticsBus;

  @Inject
  public SubscriptionBuilder() {

  }

  public Builder builder() {
    return new Builder();
  }

  public class Builder {
    private RestApiSubscription subscription = new RestApiSubscription();

    public Builder onComplete(OnComplete onComplete) {
      subscription.onComplete = onComplete;
      return this;
    }

    public Builder onError(OnError onError) {
      subscription.onError = onError;
      return this;
    }

    public Builder onNext(OnNext<?> onNext) {
      subscription.onNext = onNext;
      return this;
    }

    public Builder onStart(OnStart onStart) {
      subscription.onStart = onStart;
      return this;
    }

    public Builder setFinishOnComplete() {
      subscription.finishOnComplete = true;
      return this;
    }

    public RestApiSubscription build() {
      return (RestApiSubscription) subscription;
    }
  }

  private abstract class DefaultRestApiSubscription<T> extends Subscriber<T> {
    protected boolean finishOnComplete = true;

    @Override
    public void onCompleted() {
      if (finishOnComplete) {
        unsubscribe();
      }
    }

    @Override
    public void onError(Throwable e) {
      Log.d("DefaultSubscription", "Exception in subscription", e);
      if (e instanceof java.net.SocketException) {
        AndroidSchedulers.mainThread()
            .createWorker()
            .schedule(
                () -> Toast.makeText(context, "Something went wrong, Please check your network",
                    Toast.LENGTH_LONG).show());
      }
      if (e instanceof UnknownHostException) {
        AndroidSchedulers.mainThread()
            .createWorker()
            .schedule(() -> Toast.makeText(context, "Something went wrong, Please try again later",
                Toast.LENGTH_LONG).show());
      }
      analyticsBus.onNext(new ExceptionEvent(e));
    }
  }

  private class RestApiSubscription extends DefaultRestApiSubscription {
    private OnComplete onComplete;
    private OnError onError;
    private OnNext onNext;
    private OnStart onStart;

    protected RestApiSubscription() {

    }

    public RestApiSubscription(OnComplete onComplete, OnError onError, OnNext onNext) {
      this.onComplete = onComplete;
      this.onError = onError;
      this.onNext = onNext;
    }

    @Override
    public void onStart() {
      super.onStart();
      if (onStart != null) onStart.onStart();
    }

    @Override
    public void onCompleted() {
      super.onCompleted();
      if (onComplete != null) onComplete.OnComplete();
    }

    @Override
    public void onError(Throwable e) {
      super.onError(e);
      if (onError != null) onError.onError(e);
    }

    @Override
    public void onNext(Object o) {
      if (onNext != null) onNext.onNext(o);
    }
  }
}
