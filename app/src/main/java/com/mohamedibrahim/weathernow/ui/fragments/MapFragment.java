package com.mohamedibrahim.weathernow.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.mohamedibrahim.weathernow.R;
import com.mohamedibrahim.weathernow.listeners.LocationSettingListener;
import com.mohamedibrahim.weathernow.listeners.OperationListener;
import com.mohamedibrahim.weathernow.managers.LocationManager;
import com.mohamedibrahim.weathernow.models.Item;
import com.mohamedibrahim.weathernow.models.ResponseObject;
import com.mohamedibrahim.weathernow.ui.activities.HomeActivity;
import com.mohamedibrahim.weathernow.ui.views.CustomButton;
import com.mohamedibrahim.weathernow.utils.DBUtils;
import com.mohamedibrahim.weathernow.utils.SharedPreferencesManager;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapFragment extends ParentFragment implements OperationListener {


    @BindView(R.id.progress)
    ProgressBar progressBar;

    private static MapView mapView;
    private static CustomButton btnFindOnMap;

    LocationSettingListener mLocationSettingRequestInterface;
    private HashMap<String, Item> allPlaces;
    private GoogleMap mGoogleMap;
    private View mView;
    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 20005;
    private Location mComingLocation;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_map, null);
        mapView = (MapView) mView.findViewById(R.id.map);
        btnFindOnMap = (CustomButton) mView.findViewById(R.id.btn_find_on_map);
        ButterKnife.bind(this, mView);
        mapView.onCreate(savedInstanceState);
        mLocationSettingRequestInterface = LocationManager.getInstance(getActivity()).setMapView(mapView).buildGoogleMapApiClient(this);
        allPlaces = new HashMap<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((HomeActivity) getActivity()).setLocationSettingListener(mLocationSettingRequestInterface);
        return mView;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        progressBar.setVisibility(View.GONE);

        if (null != data) {
            Log.v("response", data);
            ResponseObject responseObject = new Gson().fromJson(data, ResponseObject.class);
            items.clear();
            items.addAll(responseObject.getItems());
            if (!items.isEmpty()) {
                DBUtils.deleteCities(getContext());
                DBUtils.addCities(items, getContext());
            }
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                String itemLatLng = item.getCoord().getLat() +
                        "," + item.getCoord().getLon();

                MarkerOptions newMarker = new MarkerOptions();
                newMarker.position(new LatLng(item.getCoord().getLat(),
                        item.getCoord().getLon()));
                newMarker.snippet(itemLatLng);
                allPlaces.put(itemLatLng, item);
                mGoogleMap.addMarker(newMarker);
            }
            if (!responseObject.getCod().equalsIgnoreCase("200") && fragmentToActivityListener != null) {
                fragmentToActivityListener.showSnackbar(R.string.sry_msg);
            }
        }
    }


    @OnClick(R.id.btn_find_on_map)
    void onClickFindOnMap() {
        try {
            Intent intent = new PlaceAutocomplete
                    .IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .setFilter(new AutocompleteFilter.Builder().setTypeFilter(com.google.android.gms.location.places.Place.TYPE_COUNTRY).build())
                    .build(getActivity());
            getActivity().startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
            GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), e.getConnectionStatusCode(),
                    0 /* requestCode */).show();

        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Log.e("Exception", message);
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    public static void getChosenPlaceOnMap(Intent data, Context context) {
        final com.google.android.gms.location.places.Place place = PlaceAutocomplete.
                getPlace(context, data);
        if (place != null) {
            if (place.getAddress() != null) {
                btnFindOnMap.setText(place.getAddress().toString());
                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(place.getLatLng())      // Sets the center of the map to Mountain View
                                .zoom(7.0f)                   // Sets the zoom
                                .build();                   // Creates a CameraPosition from the builder
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }
                });
            }
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        LocationManager.getInstance(getActivity()).onDestroy();
    }


    @Override
    public void onOperationCompleted(int resultCode, Object mComingValue) {
        if (mComingValue instanceof Location) {
            mComingLocation = (Location) mComingValue;

            if (resultCode == LocationManager.FIRST_LOCATION_CALL) {
                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        mGoogleMap = googleMap;
                        googleMap.setMyLocationEnabled(true);
                        changeFindLocationPosition();
                        LatLng latLng = new LatLng(mComingLocation.getLatitude(), mComingLocation.getLongitude());
                        SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager.getInstance(getContext());
                        sharedPreferencesManager.putString(R.string.lon, mComingLocation.getLongitude() + "");
                        sharedPreferencesManager.putString(R.string.lat, mComingLocation.getLatitude() + "");
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 7.0f));

                        progressBar.setVisibility(View.VISIBLE);
                        fetchCitiesFromAPI(latLng);

                        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                showDialog(allPlaces.get(marker.getSnippet()));
                                return true;
                            }
                        });

                    }
                });
            }
        }
    }

    private void changeFindLocationPosition() {
        if (mapView != null &&
                mapView.findViewById(Integer.parseInt("1")) != null) {
            // Get the button view
            View btn = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    btn.getLayoutParams();
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 30, 30);
        }
    }

}

