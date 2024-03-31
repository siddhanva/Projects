package com.example.weatherapp;
import android.app.Activity;
import android.content.Context;
import android.media.Rating;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
public class CustomAdapter extends ArrayAdapter<Weather>{
    Context context;
    ArrayList<Weather> dayList;
    int resource;


    public CustomAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Weather> dayList) {
        super(context, resource, dayList);
        this.context = context;
        this.resource = resource;
        this.dayList = dayList;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View adapterLayout = layoutInflater.inflate(resource, null);

        ImageView imageView = adapterLayout.findViewById(R.id.imageView);
        TextView date = (TextView) adapterLayout.findViewById(R.id.date);
        TextView conditions = (TextView) adapterLayout.findViewById(R.id.conditions);
        TextView min = (TextView) adapterLayout.findViewById(R.id.min);
        TextView max = (TextView) adapterLayout.findViewById(R.id.max);

        date.setText(dayList.get(position).getDate().substring(0,10));

        //sunny, clear, rainy, cloudy, snow
        if(dayList.get(position).getConditions().contains("clear"))
            imageView.setImageResource(R.drawable.clear);
        else if(dayList.get(position).getConditions().contains("clouds"))
            imageView.setImageResource(R.drawable.cloudy);
        else if(dayList.get(position).getConditions().contains("rain"))
            imageView.setImageResource(R.drawable.rainy);
        else if(dayList.get(position).getConditions().contains("snow"))
            imageView.setImageResource(R.drawable.snow);
        else if(dayList.get(position).getConditions().contains("sunny"))
            imageView.setImageResource(R.drawable.sunny);
        conditions.setText(dayList.get(position).getConditions());
        min.setText("Min: "+Double.toString(dayList.get(position).getmin()));
        max.setText("Max: "+Double.toString(dayList.get(position).getmax()));

        return adapterLayout;
    }

}