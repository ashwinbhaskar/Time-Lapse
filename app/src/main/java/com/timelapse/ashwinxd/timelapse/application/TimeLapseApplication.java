package com.timelapse.ashwinxd.timelapse.application;

import com.timelapse.ashwinxd.basemodule.application.BaseApplication;
import com.timelapse.ashwinxd.basemodule.di.BaseApplicationComponent;
import com.timelapse.ashwinxd.basemodule.di.BaseModule;
import com.timelapse.ashwinxd.timelapse.BuildConfig;
import com.timelapse.ashwinxd.timelapse.di.DaggerTimeLapseApplicationComponent;
import com.timelapse.ashwinxd.timelapse.di.TimeLapseApplicationComponent;
import com.timelapse.ashwinxd.timelapse.di.TimeLapseApplicationModule;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by ashwinxd on 18/4/17.
 */

public class TimeLapseApplication extends BaseApplication {

  public String DB_NAME = "GeoLite2.mmdb";
  public String DB_PATH = "/data/data/" + BuildConfig.APPLICATION_ID + "/";
  TimeLapseApplicationComponent timeLapseApplicationComponent;

  @Override public void initDaggerInjector() {

    this.timeLapseApplicationComponent = DaggerTimeLapseApplicationComponent.builder()
        .timeLapseApplicationModule(new TimeLapseApplicationModule(this))
        .baseModule(new BaseModule(this))
        .build();
    timeLapseApplicationComponent.inject(this);

  }

  @Override public void initOther() {
    copyDB();
  }

  @Override public void initFirebase() {

  }

  @Override public TimeLapseApplicationComponent getApplicationComponent() {
     return timeLapseApplicationComponent;
  }

  private void copyDB() {
    try {
      InputStream is = getAssets().open(DB_NAME);
      String outfilename = DB_PATH + DB_NAME;
      OutputStream myoutput = new FileOutputStream(outfilename);

      // transfer byte to inputfile to outputfile
      byte[] buffer = new byte[1024];
      int length;
      while ((length = is.read(buffer)) > 0) {
        myoutput.write(buffer, 0, length);
      }

      //Close the streams
      myoutput.flush();
      myoutput.close();
      is.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
