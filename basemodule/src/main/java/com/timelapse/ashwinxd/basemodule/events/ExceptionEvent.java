package com.timelapse.ashwinxd.basemodule.events;

import com.timelapse.ashwinxd.model.model.Event;

/**
 * Created by ashwinxd on 18/4/17.
 */

public class ExceptionEvent implements Event<Throwable> {

  private final Throwable exception;

  public ExceptionEvent(Throwable e){
    this.exception = e;
  }

  @Override public Throwable data() {
    return exception;
  }

  @Override public EventType eventType() {
    return Event.EventType.EXCEPTION;
  }
}

