package com.mohamedibrahim.weathernow.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.mohamedibrahim.weathernow.R;
import com.mohamedibrahim.weathernow.adapters.AnimationBaseAdapter;
import com.mohamedibrahim.weathernow.adapters.CitiesAdapter;
import com.mohamedibrahim.weathernow.listeners.LifecycleTabsListener;
import com.mohamedibrahim.weathernow.models.Item;
import com.mohamedibrahim.weathernow.models.ResponseObject;
import com.mohamedibrahim.weathernow.utils.SharedPreferencesManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CitiesFragment extends ParentFragment implements SwipeRefreshLayout.OnRefreshListener, LifecycleTabsListener, AnimationBaseAdapter.OnItemClickListener {

    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private CitiesAdapter mAdapter;

    public static CitiesFragment newInstance() {
        return new CitiesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_refresh, container, false);
        ButterKnife.bind(this, view);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        initRecycler();

        return view;
    }

    private void initRecycler() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new CitiesAdapter(getContext(), items);
        mAdapter.setOnItemClickListener(this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onRefresh() {
        SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager.getInstance(getContext());
        String lat = sharedPreferencesManager.getString(R.string.lat, "0");
        String lon = sharedPreferencesManager.getString(R.string.lon, "0");

        LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));

        fetchCitiesFromAPI(latLng);
    }

    @Override
    public void onItemClick(Object object) {
        showDialog((Item) object);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        super.onLoadFinished(loader, data);
        if (data != null) {
            ResponseObject responseObject = new Gson().fromJson(data, ResponseObject.class);
            mAdapter.notifyDataSetChanged(responseObject.getItems());
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onResumeFragment() {
        onRefresh();
    }
}
