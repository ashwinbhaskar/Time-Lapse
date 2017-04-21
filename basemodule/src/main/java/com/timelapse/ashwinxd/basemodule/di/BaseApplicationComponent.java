package com.timelapse.ashwinxd.basemodule.di;

import com.timelapse.ashwinxd.basemodule.BaseActivity;
import dagger.Component;
import javax.inject.Singleton;

/**
 * Created by ashwinxd on 18/4/17.
 */

@Singleton // Constraints this component to one-per-application or unscoped bindings.
@Component(modules = { BaseModule.class}) public interface BaseApplicationComponent {


}

