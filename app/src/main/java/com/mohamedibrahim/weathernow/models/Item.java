package com.mohamedibrahim.weathernow.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Item {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("coord")
    private Coord coord;
    @SerializedName("main")
    private Main main;
    @SerializedName("dt")
    private int dt;
    @SerializedName("wind")
    private Wind wind;
    @SerializedName("sys")
    private Sys sys;
    @SerializedName("rain")
    private String rain;
    @SerializedName("snow")
    private String snow;
    @SerializedName("clouds")
    private Clouds clouds;
    @SerializedName("weather")
    private ArrayList<Weather> weather;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coord getCoord() {
        return coord;
    }

    public Main getMain() {
        return main;
    }

    public int getDt() {
        return dt;
    }

    public Wind getWind() {
        return wind;
    }

    public Sys getSys() {
        return sys;
    }

    public String getRain() {
        return rain;
    }

    public String getSnow() {
        return snow;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public ArrayList<Weather> getWeather() {
        return weather;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public void setWeather(ArrayList<Weather> weather) {
        this.weather = weather;
    }
}
