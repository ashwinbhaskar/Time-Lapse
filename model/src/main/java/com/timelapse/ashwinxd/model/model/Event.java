package com.timelapse.ashwinxd.model.model;

/**
 * Created by ashwinxd on 18/4/17.
 */

public interface Event<T> {

  T data();

  EventType eventType();

  enum EventType{
    EXCEPTION,ACTIVITY_LAUNCHER
  }

}
