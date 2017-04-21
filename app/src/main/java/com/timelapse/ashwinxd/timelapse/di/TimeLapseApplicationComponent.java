package com.timelapse.ashwinxd.timelapse.di;

import android.content.Context;
import com.timelapse.ashwinxd.basemodule.di.BaseApplicationComponent;
import com.timelapse.ashwinxd.basemodule.di.BaseModule;
import com.timelapse.ashwinxd.basemodule.rx.SubscriptionBuilder;
import com.timelapse.ashwinxd.model.model.Event;
import com.timelapse.ashwinxd.repository.store.rx.HttpResponseStatusOperator;
import com.timelapse.ashwinxd.repository.store.store.RestApi;
import com.timelapse.ashwinxd.timelapse.activity.MapActivity;
import com.timelapse.ashwinxd.timelapse.application.TimeLapseApplication;
import dagger.Component;
import javax.inject.Named;
import javax.inject.Singleton;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

/**
 * Created by ashwinxd on 18/4/17.
 */

@Singleton// Constraints this component to one-per-application or unscoped bindings.
@Component(modules = {TimeLapseApplicationModule.class, BaseModule.class
})
public interface TimeLapseApplicationComponent extends BaseApplicationComponent {
  void inject(MapActivity mapActivity);
  void inject(TimeLapseApplication timeLapseApplication);
  //Exposed to sub-graphs.
  Context context();

  // ThreadExecutor threadExecutor();
  // PostExecutionThread postExecutionThread();
  RestApi restApi();

  SubscriptionBuilder defaultActivitySubscription();

  TimeLapseApplication timeLapseApplication();

  BehaviorSubject<Event> systemBehaviorSubject();

  //HttpResponseStatusOperator httpResponseStatusOperator();

  @Named("appPublishBus") PublishSubject<Event> systemPublishBus();
}
