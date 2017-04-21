package com.timelapse.ashwinxd.basemodule.di;

import android.app.Application;
import android.content.Context;
import com.timelapse.ashwinxd.basemodule.events.ActivityLauncherEvent;
import com.timelapse.ashwinxd.basemodule.rx.ISubscriptionBuilder;
import com.timelapse.ashwinxd.basemodule.rx.SubscriptionBuilder;
import com.timelapse.ashwinxd.model.model.Event;
import com.timelapse.ashwinxd.repository.store.store.RestApi;
import com.timelapse.ashwinxd.repository.store.store.RestApiImpl;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;
import rx.Observable;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

/**
 * Created by ashwinxd on 16/4/17.
 */
@Module
public class BaseModule {

  Application application;

  public BaseModule(Application application){
    this.application = application;
  }
  @Provides
  @Singleton RestApi getRestStore(RestApiImpl restApiImpl){
    return restApiImpl;
  }

  BehaviorSubject<Event> behaviorSubject = BehaviorSubject.create();
  PublishSubject<Event> appPublishSubject = PublishSubject.create();
  PublishSubject<ActivityLauncherEvent> activityLauncher = PublishSubject.create();
  PublishSubject<Event> analyticsLogger = PublishSubject.create();

  //@Provides
  //@Singleton
  //@Named("RestApiImpl")
  //RestApi provideRestApiImpl(
  //    RestApiImpl restApi) {
  //  return restApi;
  //}

  @Provides
  @Singleton
  BehaviorSubject<Event> provideBehaviorSubject() {
    return behaviorSubject;
  }

  @Provides
  @Singleton
  @Named("appBehaviorObservable") Observable<Event> provideEventObservable() {
    return behaviorSubject.asObservable();
  }

  @Provides
  @Singleton
  @Named("appPublishBus")
  PublishSubject<Event> providePublishEventObservable() {
    return appPublishSubject;
  }

  @Provides
  @Singleton
  @Named("appPublishObservable")
  Observable<Event> provideAppPublishObservable() {
    return appPublishSubject.asObservable();
  }

  @Provides
  @Singleton
  @Named("analyticsObservable")
  Observable<Event> provideAnalyticsPublishObservable() {
    return analyticsLogger.asObservable();
  }

  @Provides
  @Singleton
  @Named("activityLauncherObservable")
  Observable<ActivityLauncherEvent> provideActivityLauncherObservable() {
    return activityLauncher.asObservable();
  }

  @Provides
  @Singleton
  @Named("analyticsBus")
  PublishSubject<Event> provideAnalyticsBus() {
    return analyticsLogger;
  }

  @Provides
  @Singleton
  @Named("activityLauncherBus")
  PublishSubject<ActivityLauncherEvent> provideActivityLauncherBus() {
    return activityLauncher;
  }

  //@Provides
  //@Singleton ISubscriptionBuilder provideDefaultActivitySubscription(
  //    SubscriptionBuilder subscriptionBuilder) {
  //  return subscriptionBuilder;
  //}

  @Provides
  @Singleton Context provideApplicationContext() {
    return this.application;
  }


}
