package com.example.weatherapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Weather {
    private String date;
    private String conditions;
    private double min;
    private double max;

    public Weather(String date, String conditions, double min, double max){
        this.date = date;
        this.conditions = conditions;
        this.min = min;
        this.max = max;
    }

    public String getDate(){
        return date;
    }
    public String getConditions(){
        return conditions;
    }
    public double getmin(){
        return min;
    }
    public double getmax(){
        return max;
    }


}
