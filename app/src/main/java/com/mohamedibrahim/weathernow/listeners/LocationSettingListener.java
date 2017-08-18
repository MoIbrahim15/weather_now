package com.mohamedibrahim.weathernow.listeners;

import android.content.Intent;

public interface LocationSettingListener {
    void onRequestResult(int requestCode, int resultCode, Intent Date);
}
