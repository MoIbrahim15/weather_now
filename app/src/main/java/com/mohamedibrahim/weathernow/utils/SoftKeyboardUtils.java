package com.mohamedibrahim.weathernow.utils;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class SoftKeyboardUtils {
    private SoftKeyboardUtils() {
    }

    public static void hideSoftKeyboard(Window window) {
        try {
            InputMethodManager imm = (InputMethodManager) window.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(window.getDecorView().getRootView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        } catch (NullPointerException mException) {
            mException.printStackTrace();
        }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public static void showKeyboard(View view, Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getApplicationContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }
}
