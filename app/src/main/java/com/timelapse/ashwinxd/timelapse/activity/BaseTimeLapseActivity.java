package com.timelapse.ashwinxd.timelapse.activity;

import com.timelapse.ashwinxd.basemodule.BaseActivity;
import com.timelapse.ashwinxd.timelapse.application.TimeLapseApplication;
import com.timelapse.ashwinxd.timelapse.di.TimeLapseApplicationComponent;

/**
 * Created by ashwinxd on 18/4/17.
 */

public abstract class BaseTimeLapseActivity extends BaseActivity {

  /**
   * Get the Main Application component for dependency injection.
   */

  public TimeLapseApplication getTimeLapseApplication() {
    return (TimeLapseApplication)super.getApplication();
  }

  public TimeLapseApplicationComponent getTimeLapseApplicationComponent(){
    return (TimeLapseApplicationComponent) getTimeLapseApplication().getApplicationComponent();
  }

}
