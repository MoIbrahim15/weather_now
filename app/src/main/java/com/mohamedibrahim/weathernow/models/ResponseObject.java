package com.mohamedibrahim.weathernow.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseObject {

    @SerializedName("message")
    private String message;
    @SerializedName("cod")
    private String cod;
    @SerializedName("count")
    private int count;
    @SerializedName("list")
    private ArrayList<Item> item;

    public String getMessage() {
        return message;
    }

    public String getCod() {
        return cod;
    }

    public int getCount() {
        return count;
    }

    public ArrayList<Item> getItems() {
        return item;
    }
}
