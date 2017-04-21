package com.timelapse.ashwinxd.model.model;

import java.util.Date;
import org.threeten.bp.LocalDateTime;

/**
 * Created by ashwinxd on 17/4/17.
 */

public class GeoIPTime {
  private String geoIp;
  private LocalDateTime timeStamp;

  public GeoIPTime(String geoIp, LocalDateTime timeStamp) {
    this.geoIp = geoIp;
    this.timeStamp = timeStamp;
  }

  public String getGeoIp() {
    return geoIp;
  }

  public void setGeoIp(String geoIp) {
    this.geoIp = geoIp;
  }

  public LocalDateTime getTimeStamp() {
    return timeStamp;
  }

  public void setTimeStamp(LocalDateTime timeStamp) {
    this.timeStamp = timeStamp;
  }
}
