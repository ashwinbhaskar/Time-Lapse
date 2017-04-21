package com.timelapse.ashwinxd.timelapse.di;

import com.timelapse.ashwinxd.timelapse.application.TimeLapseApplication;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * Created by ashwinxd on 18/4/17.
 */
@Module
public class TimeLapseApplicationModule {
  private final TimeLapseApplication application;

  public TimeLapseApplicationModule(TimeLapseApplication application) {
    this.application = application;
  }
  @Provides @Singleton TimeLapseApplication provideTimeLapseApplication() {
    return this.application;
  }
}
