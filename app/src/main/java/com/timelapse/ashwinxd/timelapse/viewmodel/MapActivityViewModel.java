package com.timelapse.ashwinxd.timelapse.viewmodel;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import com.timelapse.ashwinxd.model.model.GeoIPTime;
import com.timelapse.ashwinxd.repository.store.store.RestApi;
import com.timelapse.ashwinxd.timelapse.activity.MapActivity;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.inject.Inject;
import okhttp3.ResponseBody;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

/**
 * Created by ashwinxd on 19/4/17.
 */

public class MapActivityViewModel extends BaseTimeLapseViewModel<MapActivity> {
  private final String TAG = "MapActivityViewModel";
  @Inject RestApi restApi;
  public File file;
  private final String PLAY = "Play";
  private final String PAUSE = "Pause";
  private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  public ObservableField<String> playLabel = new ObservableField<>(PLAY);
  public ObservableField<String> pauseLabel = new ObservableField<>(PAUSE);
  public ObservableBoolean showPlay = new ObservableBoolean(true);
  public ObservableBoolean showLoading = new ObservableBoolean(true);
  public ObservableField<String> loadingMessage = new ObservableField<>("Fetching IPs..");

  @Override public void afterRegister() {
    super.afterRegister();
    file = new File(getApplication().DB_PATH + getApplication().DB_NAME);
  }

  @Inject MapActivityViewModel() {

  }

  public void play() {
    showPlay.set(false);
    getActivity().play();
  }

  public void pause(){
    showPlay.set(true);
    getActivity().pause();
  }

  public void geoIPTimes() {
    getCompositeSubscription().add(restApi.getTimeStampedIPs()
        .subscribe(subscriptionBuilder.builder().onNext((ResponseBody responseBody) -> {
          parseAndSampleIPs(responseBody);
        }).build()));
  }

  private void parseAndSampleIPs(ResponseBody responseBody) {
    setLoadingMessage("Sampling IPs..");
    ArrayList<GeoIPTime> listOfIps = new ArrayList<>(3600);
    Log.i(TAG, "starting parsing");
    try {
      String str = new String(responseBody.bytes());
      String[] geopIPs = str.split("\\n");
      for (int i = 0; i < geopIPs.length; i += 12) {
        String[] geoIPTimes = geopIPs[i].split(",");
        GeoIPTime geoIPTime = new GeoIPTime(geoIPTimes[0],
            LocalDateTime.parse(geoIPTimes[1].split("\\.")[0], formatter));
        listOfIps.add(geoIPTime);
      }
      Log.i(TAG, "parsing done. Size of list = " + listOfIps.size());
      Log.i(TAG, "first and last ip in list = "
          + listOfIps.get(0).getTimeStamp().toString()
          + " and "
          + listOfIps.get(listOfIps.size() - 1).getTimeStamp().toString());
      getActivity().geoIPsLoaded(listOfIps);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public LatLng findAverageLatLng(ArrayList<LatLng> list, int start, int end) {
    double latSum = 0.0, lngSum = 0.0;
    for (int i = start; i < end; i++) {
      latSum += list.get(i).latitude;
      lngSum += list.get(i).longitude;
    }
    return new LatLng(latSum/(end - start), lngSum/end-start);
  }

  public static int convertDpToPixel(Context context, int dp) {
    if (context == null) {
      return 1;
    }
    float screen_density = context.getResources().getDisplayMetrics().density;
    return dp <= 0 ? 0 : (int) (screen_density * dp + 0.5f);
  }

  public void setLoadingMessage(String message){
    getActivity().runOnUiThread(() -> loadingMessage.set(message));
  }

}
