package com.timelapse.ashwinxd.basemodule.viewmodel;

import com.timelapse.ashwinxd.basemodule.BaseActivity;
import com.timelapse.ashwinxd.basemodule.rx.SubscriptionBuilder;
import com.timelapse.ashwinxd.model.model.Event;
import java.lang.ref.WeakReference;
import javax.inject.Inject;
import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ashwinxd on 19/4/17.
 */

public abstract class BaseActivityViewModel<T> {
  @Inject public SubscriptionBuilder subscriptionBuilder;
  public T getActivity() {
    return activity.get();
  }

  protected WeakReference<T> activity;

  public abstract void viewDestroyed();

  public PublishSubject<Event> getLocalPublishBus() {
    return ((BaseActivity) getActivity()).getLocalPublishBus();
  }

  public Observable<Event> getLocalBehaviourBus() {
    return ((BaseActivity) getActivity()).getLocalBehaviorBus();
  }

  public Observable<Event> getAppBehaviourBus() {
    return ((BaseActivity) getActivity()).getSystemBus();
  }

  public Observable<Event> getAppPublishBus() {
    return ((BaseActivity) getActivity()).getAppPublishBus();
  }

  public void registerActivity(T activity) {
    this.activity = new WeakReference<T>(activity);
    afterRegister();
  }


  public void afterRegister() {

  }

  public CompositeSubscription getCompositeSubscription() {
    return ((BaseActivity) getActivity()).getCompositeSubscription();
  }
}
