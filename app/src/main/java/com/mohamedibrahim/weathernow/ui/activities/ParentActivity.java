package com.mohamedibrahim.weathernow.ui.activities;

import android.graphics.Typeface;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.mohamedibrahim.weathernow.R;
import com.mohamedibrahim.weathernow.listeners.FragmentToActivityListener;
import com.mohamedibrahim.weathernow.ui.views.CustomTextView;

import butterknife.BindView;

public class ParentActivity extends AppCompatActivity implements FragmentToActivityListener {

    @BindView(R.id.coordinator)
    CoordinatorLayout coordinator;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_toolbar_title)
    CustomTextView tvTitle;
    private Snackbar snackbar;
    private ActionBar actionbar;

    @Override
    public void showSnackbar(int messageRes) {
        showSnackbar(getString(messageRes));
    }

    @Override
    public void showSnackbar(String message) {
        hideSnackbar();
        snackbar = Snackbar.make(coordinator, message, Snackbar.LENGTH_LONG);
        View view = snackbar.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        Typeface font = Typeface.createFromAsset(getAssets(), getString(R.string.georgia_bold));
        tv.setTypeface(font);
        snackbar.show();
    }

    @Override
    public void hideSnackbar() {
        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
        }
    }

    @Override
    public void showHomeFragment() {

    }

    @Override
    public void changeTitle(int titleRes) {
        tvTitle.setText(titleRes);
    }

    protected void setupToolbar() {
        setSupportActionBar(toolbar);
        actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayHomeAsUpEnabled(false);
        actionbar.setHomeButtonEnabled(false);
        actionbar.setDisplayShowTitleEnabled(false);
    }
}
