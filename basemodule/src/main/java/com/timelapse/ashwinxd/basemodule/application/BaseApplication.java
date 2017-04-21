package com.timelapse.ashwinxd.basemodule.application;

import android.support.multidex.MultiDexApplication;
import com.timelapse.ashwinxd.basemodule.di.BaseApplicationComponent;

/**
 * Created by ashwinxd on 16/4/17.
 */

public abstract class BaseApplication extends MultiDexApplication {

  @Override public void onCreate() {
    super.onCreate();
    initDaggerInjector();
    initOther();
    initFirebase();
  }

  public abstract void initDaggerInjector();
  public abstract void initOther();
  public abstract void initFirebase();
  public abstract BaseApplicationComponent getApplicationComponent();
}

