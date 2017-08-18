package com.mohamedibrahim.weathernow.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;

import com.google.android.gms.maps.model.LatLng;
import com.mohamedibrahim.weathernow.R;
import com.mohamedibrahim.weathernow.listeners.FragmentToActivityListener;
import com.mohamedibrahim.weathernow.models.Item;
import com.mohamedibrahim.weathernow.ui.views.CustomTextView;
import com.mohamedibrahim.weathernow.utils.DBUtils;
import com.mohamedibrahim.weathernow.utils.NetworkUtils;
import com.mohamedibrahim.weathernow.utils.SoftKeyboardUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class ParentFragment extends Fragment implements LoaderManager.LoaderCallbacks<String> {

    protected FragmentToActivityListener fragmentToActivityListener;
    protected int titleRes;
    protected ArrayList<Item> items;
    protected static final int LOADER_ID = 1;
    protected static final String URL_EXTRA = "URL";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (items == null || items.isEmpty()) {
            items = DBUtils.getAllCities(getContext());
        }
    }

    public void setFragmentToActivityListener(FragmentToActivityListener fragmentToActivityListener) {
        this.fragmentToActivityListener = fragmentToActivityListener;
    }


    protected void fetchCitiesFromAPI(LatLng latLng) {
        Bundle queryBundle = new Bundle();
        queryBundle.putString(URL_EXTRA, String.valueOf(NetworkUtils.buildUrl(getContext(), latLng)));
        LoaderManager loaderManager = getActivity().getSupportLoaderManager();
        Loader<String> moviesLoader = loaderManager.getLoader(LOADER_ID);
        if (moviesLoader == null) {
            loaderManager.initLoader(LOADER_ID, queryBundle, this);
        } else {
            loaderManager.restartLoader(LOADER_ID, queryBundle, this);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        SoftKeyboardUtils.hideSoftKeyboard(getActivity().getWindow());
        if (fragmentToActivityListener != null) {
            fragmentToActivityListener.hideSnackbar();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (fragmentToActivityListener != null &&
                !(getFragmentManager().findFragmentById(R.id.container) instanceof HomeFragment)) {
            fragmentToActivityListener.changeTitle(titleRes);
        }
    }


    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(getActivity()) {
            String mJsonResult;

            @Override
            protected void onStartLoading() {
                if (args == null) {
                    return;
                }
                if (mJsonResult != null) {
                    deliverResult(mJsonResult);
                } else {
                    forceLoad();
                }
            }

            @Override
            public String loadInBackground() {
                try {
                    String urlString = args.getString(URL_EXTRA);
                    if (urlString == null || TextUtils.isEmpty(urlString)) {
                        return null;
                    } else {
                        URL url = new URL(urlString);
                        return NetworkUtils.getResponseFromHttpUrl(url);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(String jsonResult) {
                mJsonResult = jsonResult;
                super.deliverResult(mJsonResult);
            }

        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        /*
         * We aren't using this method in application, but i required to Override
         * it to implement the LoaderCallbacks<String> interface
         */
    }

    protected void showDialog(Item item) {
        Dialog dialog = new Dialog(getContext(), R.style.DialogStyle);
        dialog.setContentView(R.layout.dialog_content);

        CustomTextView tvCity = (CustomTextView) dialog.findViewById(R.id.tv_city);
        CustomTextView tvTemp = (CustomTextView) dialog.findViewById(R.id.tv_temp);
        CustomTextView tvMaxTemp = (CustomTextView) dialog.findViewById(R.id.tv_max_temp);
        CustomTextView tvMinTemp = (CustomTextView) dialog.findViewById(R.id.tv_min_temp);
        CustomTextView tvHum = (CustomTextView) dialog.findViewById(R.id.tv_humidity);
        CustomTextView tvPres = (CustomTextView) dialog.findViewById(R.id.tv_pressure);
        CustomTextView tvWind = (CustomTextView) dialog.findViewById(R.id.tv_wind_speed);
        CustomTextView tvDesc = (CustomTextView) dialog.findViewById(R.id.tv_desc);

        if (item.getName() != null) {
            tvCity.setText(item.getName());
        }
        if (item.getMain().getTemp() != null) {
            tvTemp.setText(getString(R.string.current_temp).concat(" ").concat(item.getMain().getTemp()));
        }
        if (item.getMain().getTemp_max() != null) {
            tvMaxTemp.setText(getString(R.string.max_temp).concat(" ").concat(item.getMain().getTemp_max()));
        }

        if (item.getMain().getTemp_min() != null) {
            tvMinTemp.setText(getString(R.string.min_temp).concat(" ").concat(item.getMain().getTemp_min()));
        }

        if (item.getMain().getHumidity() != null) {
            tvHum.setText(getString(R.string.humidity).concat(" ").concat(item.getMain().getHumidity()));
        }

        if (item.getMain().getPressure() != null) {
            tvPres.setText(getString(R.string.pressure).concat(" ").concat(item.getMain().getPressure()));
        }

        if (item.getWind().getDeg() != null && item.getWind().getSpeed() != null) {
            tvWind.setText(getString(R.string.wind_degree_speed).concat(" ").concat(item.getWind().getDeg()
                    .concat(" - ").concat(item.getWind().getSpeed())));
        }

        if (!item.getWeather().isEmpty() && item.getWeather().get(0).getDescription() != null) {
            tvDesc.setText(getString(R.string.weather_desc).concat(" ").concat(item.getWeather().get(0).getDescription()));
        }
        dialog.show();
    }
}
