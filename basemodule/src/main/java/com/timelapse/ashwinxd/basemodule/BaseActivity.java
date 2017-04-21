package com.timelapse.ashwinxd.basemodule;

import android.content.pm.ActivityInfo;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.timelapse.ashwinxd.basemodule.application.BaseApplication;
import com.timelapse.ashwinxd.basemodule.di.BaseApplicationComponent;
import com.timelapse.ashwinxd.basemodule.events.ActivityLauncherEvent;
import com.timelapse.ashwinxd.basemodule.rx.SubscriptionBuilder;
import com.timelapse.ashwinxd.model.model.Event;
import java.util.HashSet;
import javax.inject.Inject;
import javax.inject.Named;
import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

public abstract class BaseActivity extends AppCompatActivity {

  private final String TAG = this.getClass().getSimpleName();
  @Inject SubscriptionBuilder subscriptionBuilder;
  protected PublishSubject<Event> localPublishBus = PublishSubject.create();
  private BehaviorSubject<Event> localBehaviorBus = BehaviorSubject.create();
  private int orientation = -100;;
  @Inject @Named("appBehaviorObservable") Observable<Event> appBehaviorObservable;
  @Inject @Named("appPublishObservable") Observable<Event> appPublishObservable;
  @Inject BehaviorSubject<Event> appBehaviourBus;
  @Inject @Named("analyticsBus") public PublishSubject<Event> analyticsBus;
  @Inject @Named("activityLauncherBus") protected PublishSubject<ActivityLauncherEvent>
      activityLauncherBus;


  //@Inject protected PermissionManager permissionManager;

  private boolean activityResumed = false;


  public Observable<Event> getSystemBus() {
    return appBehaviorObservable;
  }

  public Observable<Event> getAppPublishBus() {
    return appPublishObservable;
  }

  public CompositeSubscription getCompositeSubscription() {
    return compositeSubscription;
  }

  protected CompositeSubscription compositeSubscription = new CompositeSubscription();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate()");

    initializeModuleComponent();
    initializeEventLogger();
    registerSystemBusSubscriptionIfNecessary();
    initializeAnalyticsLogger();
    initializeActivityLauncher();
    initializeLocalBuses();
    showTooltipIfNecessary();
  }



  @Override protected void onDestroy() {
    super.onDestroy();
    Log.v(TAG, "onDestroy()");
    //if (getViewModel() != null) getViewModel().viewDestroyed();
    compositeSubscription.unsubscribe();
    if (orientation != -100) {
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
    }
  }

  @Override protected void onStop() {
    super.onStop();
    Log.v(TAG, "onStop()");
  }

  @Override protected void onResume() {
    super.onResume();
    activityResumed = true;
    Log.v(TAG, "onResume()");
  }

  @Override protected void onPause() {
    super.onPause();
    activityResumed = false;
    Log.v(TAG, "onPause()");
  }

  @Override protected void onStart() {
    super.onStart();
    Log.v(TAG, "onStart()");
  }

  public void showTooltipIfNecessary() {

  }

  private void initializeEventLogger() {
    compositeSubscription.add(
        getSystemBus().subscribe(subscriptionBuilder.builder().onNext(event -> {
          Log.d(getClass().getName(), "Event fired @ SystemBus " + event.getClass().getName());
        }).build()));
    compositeSubscription.add(localPublishBus.subscribe(subscriptionBuilder.builder().onNext(e -> {
      Log.d(getClass().getName(), "Event fired @ LocalPublish bus" + e.getClass().getName());
    }).build()));
  }


  private void initializeLocalBuses() {
    compositeSubscription.add(appPublishObservable.subscribe(
        subscriptionBuilder.builder().onNext((Event e) -> localPublishBus.onNext(e)).build()));
  }

  protected void initializeAnalyticsLogger() {
    compositeSubscription.add(
        localPublishBus.subscribe(subscriptionBuilder.builder().onNext((Event event) -> {
          analyticsBus.onNext((Event) event);
        }).build()));
  }

  protected void initializeActivityLauncher() {
    compositeSubscription.add(
        localPublishBus.subscribe(subscriptionBuilder.builder().onNext((Event event) -> {
          activityLauncherBus.onNext(new ActivityLauncherEvent(event, this));
        }).build()));
  }

  protected void registerSystemBusSubscriptionIfNecessary() {
    if (shouldPublishOnSystemBehaviourBus()) {
      compositeSubscription.add(localPublishBus.filter(
          event -> getEventsToPublishOnSystemBus().contains(event.eventType()))
          .subscribe(subscriptionBuilder.builder().onNext((Event event) -> {
            appBehaviourBus.onNext(event);
          }).build()));
    }
  }

  /*
  Used to decide if events (from local publish bus) should be pushed on
  System behaviour bus also. Override and return true if you want it to be
  published on system behaviour bus
   */
  protected boolean shouldPublishOnSystemBehaviourBus() {
    return false;
  }

  /*
  Used if publishOnSystemBehaviorBus() returns true
   */
  protected HashSet<Event.EventType> getEventsToPublishOnSystemBus() {
    return null;
  }



  public BaseApplicationComponent getBaseApplicationComponent() {
    return getBaseApplication().getApplicationComponent();
  }

  protected abstract void initializeModuleComponent();




  private BaseApplication getBaseApplication() {
    return (BaseApplication) super.getApplication();
  }


  @Override public void onRequestPermissionsResult(int requestCode, String[] permissions,
      int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    Log.d(TAG, "Granted: " + android.text.TextUtils.join(",", permissions));
    //permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }


  public PublishSubject<Event> getLocalPublishBus() {
    return localPublishBus;
  }

  public Observable<Event> getLocalBehaviorBus() {
    return localBehaviorBus.asObservable();
  }

  public PublishSubject<Event> getAnalyticsBus() {
    return analyticsBus;
  }



  public SubscriptionBuilder getSubscriptionBuilder() {
    return subscriptionBuilder;
  }




  public boolean isActivityResumed() {
    return activityResumed;
  }



  protected void lockOrientation(int orientation){
    setRequestedOrientation(orientation);
  }
}
