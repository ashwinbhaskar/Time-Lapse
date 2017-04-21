package com.timelapse.ashwinxd.basemodule.events;

import com.timelapse.ashwinxd.basemodule.BaseActivity;
import com.timelapse.ashwinxd.model.model.Event;

/**
 * Created by ashwinxd on 18/4/17.
 */

public class ActivityLauncherEvent implements Event<Event> {

  private final Event event;
  private final BaseActivity activity;

  public ActivityLauncherEvent(Event event, BaseActivity activity){
    this.event = event;
    this.activity = activity;
  }

  public BaseActivity getActivity(){
    return activity;
  }

  public boolean matchEventClass(Class<? extends Event> eventClass){
    return  eventClass.isAssignableFrom(event.getClass());
  }

  public boolean matchActivityClass(Class<? extends BaseActivity> activity){
    return activity.isAssignableFrom(this.activity.getClass());
  }

  @Override public Event data() {
    return event;
  }

  @Override public EventType eventType() {
    return Event.EventType.ACTIVITY_LAUNCHER;
  }
}