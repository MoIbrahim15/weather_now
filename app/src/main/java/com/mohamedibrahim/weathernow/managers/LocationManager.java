package com.mohamedibrahim.weathernow.managers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.mohamedibrahim.weathernow.R;
import com.mohamedibrahim.weathernow.ui.activities.HomeActivity;
import com.mohamedibrahim.weathernow.listeners.LocationSettingListener;
import com.mohamedibrahim.weathernow.listeners.OperationListener;

public class LocationManager implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationSettingListener, LocationListener {

    private MapView mMapView;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private OperationListener operationListener;
    private Context mContext;
    private static LocationManager mManger;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    public static final int FIRST_LOCATION_CALL = 1;

    public static LocationManager getInstance(Context mComingContext) {
        if (mManger == null) {
            mManger = new LocationManager(mComingContext);
        }
        mManger.mContext = mComingContext;
        return mManger;
    }

    private LocationManager(Context mComingContext) {
        this.mContext = mComingContext;
    }

    public LocationManager buildGoogleMapApiClient(OperationListener mComingOperationListener) {              //*************1st called
        if (checkPlayServices()) {
            mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        operationListener = mComingOperationListener;
        return mManger;
    }

    private boolean checkPlayServices() {
        if (isInternetAvailable()) {
            int resultCode = GoogleApiAvailability.getInstance()
                    .isGooglePlayServicesAvailable(mContext);
            if (resultCode != ConnectionResult.SUCCESS) {
                if (GoogleApiAvailability.getInstance().isUserResolvableError(resultCode)) {
                    GoogleApiAvailability.getInstance().getErrorDialog((Activity) mContext, resultCode,
                            PLAY_SERVICES_RESOLUTION_REQUEST).show();
                } else {
                    Toast.makeText(mContext,
                            mContext.getString(R.string.not_supported), Toast.LENGTH_LONG)
                            .show();
                }
                return false;
            }
        } else {
            Toast.makeText(mContext, mContext.getString(R.string.make_sure_with_internet), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    @Override
    public void onConnected(Bundle bundle) {        //***************2nd called
        updateLocation(null);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), (Activity) mContext
                , PLAY_SERVICES_RESOLUTION_REQUEST).show();
    }

    public void onDestroy() {
        onPause();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }

    private void onPause() {
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(
                        mGoogleApiClient, this);
            }
        }
    }

    /**
     * 4th called multiple time, when GoogleApiClient connected,
     * when LocationSettings updated or when Requesting LocationSetting update
     * and when location update change
     */

    private void updateLocation(Location mLocation) {

        if (mLocation == null) {
            mLastLocation = requestLastLocation();
        } else {
            mLastLocation = mLocation;
        }

        if (mLastLocation != null) {
            mMapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    if (operationListener != null) {
                        operationListener.onOperationCompleted(FIRST_LOCATION_CALL, mLastLocation);
                    } else {
                        googleMap.setMyLocationEnabled(true);
                    }
                }
            });
        } else {
            CheckLocationSettingsAPI();
        }
    }


    private Location requestLastLocation() {
        Location mLocation = null;
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);
            }
        } else {
            mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }
        return mLocation;
    }


    private void CheckLocationSettingsAPI() {           //**********3rd called when settings Not available
        createLocationRequest();
        LocationSettingsRequest.Builder mBuilder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, mBuilder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Toast.makeText(mContext, mContext.getString(R.string.get_location), Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= 23) {
                            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                    || ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                                        mGoogleApiClient);
                            } else {
                                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                                        mGoogleApiClient);
                            }
                        }
                        startLocationUpdate();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(
                                    (Activity) mContext,
                                    HomeActivity.LOCATION_SETTING_REQUEST);
                        } catch (IntentSender.SendIntentException e) {
                            Toast.makeText(mContext, mContext.getString(R.string.resultion_reqierd), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Toast.makeText(mContext, mContext.getString(R.string.gps_off), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

    }

    private void startLocationUpdate() {
        if (mLocationRequest == null) {
            createLocationRequest();
        }
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    private void createLocationRequest() {       //********** Called When No LocationRequests
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(3000);
        mLocationRequest.setFastestInterval(3000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

    }

    @Override
    public void onRequestResult(int requestCode, int resultCode, Intent Date) {      //Called When LocationSettings Update
        switch (resultCode) {
            case Activity.RESULT_OK:
                updateLocation(null);
                mMapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        googleMap.setMyLocationEnabled(true);
                        if (getLastLocation() != null)
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(getLastLocation().getLatitude(), getLastLocation().getLongitude()), 17.0f));

                    }
                });
                break;
            case Activity.RESULT_CANCELED:
                Toast.makeText(mContext, mContext.getResources().getString(R.string.gps_not_enabled), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        updateLocation(location);
    }

    public LocationManager setMapView(MapView mComingMapView) {
        mMapView = mComingMapView;
        return mManger;
    }

    private Location getLastLocation() {
        return mLastLocation;
    }

    private boolean isInternetAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null && cm.getActiveNetworkInfo() != null) {
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        } else {
            return false;
        }
    }
}