package com.mohamedibrahim.weathernow.listeners;

public interface FragmentToActivityListener {

    void showSnackbar(int messageRes);

    void showSnackbar(String message);

    void hideSnackbar();

    void showHomeFragment();

    void changeTitle(int titleRes);
}
