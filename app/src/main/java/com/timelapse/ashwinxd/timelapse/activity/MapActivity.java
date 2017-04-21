package com.timelapse.ashwinxd.timelapse.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.SeekBar;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.record.Location;
import com.timelapse.ashwinxd.model.model.GeoIPTime;
import com.timelapse.ashwinxd.timelapse.R;
import com.timelapse.ashwinxd.timelapse.databinding.ActivityMapBinding;
import com.timelapse.ashwinxd.timelapse.viewmodel.MapActivityViewModel;
import java.net.InetAddress;
import java.util.ArrayList;
import javax.inject.Inject;

/**
 * Created by ashwinxd on 13/4/17.
 */

public class MapActivity extends BaseTimeLapseActivity {
  private final String TAG = "Map";
  @Inject MapActivityViewModel viewModel;
  SupportMapFragment map;
  GoogleMap mGoogleMap;
  ArrayList<LatLng> locationList = new ArrayList<>();
  ActivityMapBinding mBinding;
  HeatmapTileProvider.Builder mBuilder = new HeatmapTileProvider.Builder();
  HeatmapTileProvider mProvider;
  TileOverlayOptions mTileOverlayOptions = new TileOverlayOptions();
  CountDownTimer mCountDownTimer;
  long pausedTime = 0;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_map);
    mBinding.setViewModel(viewModel);
    map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
    map.getMapAsync(googleMap -> {
      mGoogleMap = googleMap;
      mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
      mGoogleMap.setPadding(0, 0, 0, viewModel.convertDpToPixel(getApplicationContext(), 80));
    });
    viewModel.registerActivity(this);
    setUpListeners();
    viewModel.geoIPTimes();
  }

  @Override protected void initializeModuleComponent() {
    getTimeLapseApplicationComponent().inject(this);
  }

  private void setUpListeners() {
    mBinding.rangeSeekbar1.setMax(59);
    mBinding.rangeSeekbar1.incrementProgressBy(1);
    mBinding.rangeSeekbar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (locationList.size() == 0) return;
        Log.i(TAG, "number = " + i);
        ArrayList<LatLng> tempList = new ArrayList<LatLng>(60);
        tempList.addAll(locationList.subList(60 * i, 60 * (i + 1)));
        if (mProvider == null) {
          mProvider = mBuilder.data(tempList).build();
        } else {
          mProvider.setData(tempList);
        }
        mGoogleMap.clear();
        mGoogleMap.addTileOverlay(mTileOverlayOptions.tileProvider(mProvider).fadeIn(true));
      }

      @Override public void onStartTrackingTouch(SeekBar seekBar) {

      }

      @Override public void onStopTrackingTouch(SeekBar seekBar) {

      }
    });
  }

  public void geoIPsLoaded(ArrayList<GeoIPTime> list) {
    viewModel.setLoadingMessage("Getting GeoLocations..");
    try {
      DatabaseReader reader = new DatabaseReader.Builder(viewModel.file).build();
      for (GeoIPTime geoIPTime : list) {
        InetAddress ipAddress = InetAddress.getByName(geoIPTime.getGeoIp());
        Location location = null;
        try {
          location = reader.city(ipAddress).getLocation();
        } catch (AddressNotFoundException e) {
          continue;
        }
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        LatLng latLng = new LatLng(lat, lng);
        locationList.add(latLng);
      }
      Log.i(TAG, "locationList size = " + locationList.size());
      viewModel.showLoading.set(false);
      runOnUiThread(() -> {
        mGoogleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(viewModel.findAverageLatLng(locationList, 0, 60),
                4.0f));
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void play() {
    mCountDownTimer = new CountDownTimer(59000 - pausedTime, 1000) {
      @Override public void onTick(long l) {
        int position = (int) ((59000l - l) / 1000);
        mBinding.rangeSeekbar1.setProgress(position);
        Log.i(TAG, "coundown timeer value = " + position);
        pausedTime = l;
      }

      @Override public void onFinish() {

      }
    };

    mCountDownTimer.start();
  }

  public void pause() {
    mCountDownTimer.cancel();
  }
}
