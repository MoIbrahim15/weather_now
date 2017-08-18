package com.mohamedibrahim.weathernow.models;

import com.google.gson.annotations.SerializedName;

public class Wind {
    @SerializedName("speed")
    private String speed;
    @SerializedName("deg")
    private String deg;

    public String getSpeed() {
        return speed;
    }

    public String getDeg() {
        return deg;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public void setDeg(String deg) {
        this.deg = deg;
    }
}
