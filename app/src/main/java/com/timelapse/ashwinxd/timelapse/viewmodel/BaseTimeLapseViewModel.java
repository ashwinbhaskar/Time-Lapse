package com.timelapse.ashwinxd.timelapse.viewmodel;

import com.timelapse.ashwinxd.basemodule.BaseActivity;
import com.timelapse.ashwinxd.basemodule.viewmodel.BaseActivityViewModel;
import com.timelapse.ashwinxd.timelapse.activity.BaseTimeLapseActivity;
import com.timelapse.ashwinxd.timelapse.application.TimeLapseApplication;

/**
 * Created by ashwinxd on 19/4/17.
 */

public class BaseTimeLapseViewModel<T> extends BaseActivityViewModel<T> {

  @Override public void viewDestroyed() {

  }

  public TimeLapseApplication getApplication(){
    return ((BaseTimeLapseActivity)getActivity()).getTimeLapseApplication();
  }

}
