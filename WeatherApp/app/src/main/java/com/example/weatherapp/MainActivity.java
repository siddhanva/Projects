package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    EditText editTextText;
    ListView listView;
    Button button;
    TextView current;
    private String zipcode;
    ImageView imageView2;
    TextView quote;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextText = findViewById(R.id.editTextText);
        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);
        listView = findViewById(R.id.listView);
        current = findViewById(R.id.current);
        imageView2 = findViewById(R.id.imageView2);
        quote = findViewById(R.id.quote);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    zipcode = editTextText.getText().toString();
                    AsyncThread task = new AsyncThread();
                    task.execute(zipcode);
            }
        });


    }
    public class AsyncThread extends AsyncTask<String, Void, ArrayList>{
        ArrayList<JSONObject> list;
        ArrayList<Weather> dayList;
        JSONObject location;
        JSONArray l;
        double currenttemp;
        String currentconditions;


        @Override

        protected ArrayList<Weather> doInBackground(String... strings) {

            list = new ArrayList<JSONObject>();

            try {
                zipcode = strings[0];
                Log.d("ZIPCODE", zipcode);
                //locates longitude, latitude, and location
                String geolocatingcall = "https://api.openweathermap.org/geo/1.0/zip?zip="+zipcode+",US&appid=18935495ebcc99d0cf99ed51136f12c7";
                Log.d("URL", geolocatingcall);
                URL urllocation = new URL(geolocatingcall);
                URLConnection urlConnection = urllocation.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String locationdata ="";

                //adds location data to String
                String line;
                while((line = bufferedReader.readLine()) !=null) {
                    locationdata += line;
                }


                location = new JSONObject(locationdata);

                Log.d("Data: ",location.toString());

                //finds 5-day/3-Hour Forecast from lon and lat

                String url5day = "https://api.openweathermap.org/data/2.5/forecast?lat="+Double.toString(location.getDouble("lat"))+"&lon="+Double.toString(location.getDouble("lon"))+"&appid=18935495ebcc99d0cf99ed51136f12c7&units=imperial";
                Log.d("URL", url5day);
                URL urlforecast = new URL(url5day);
                URLConnection urlConnection1 = urlforecast.openConnection();
                InputStream inputStream1 = urlConnection1.getInputStream();
                BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(inputStream1));
                String forecastdata ="";

                //adds forecast data to String
                String line2="";

                while((line2 = bufferedReader1.readLine()) !=null) {
                    forecastdata += line2;
                }

                Log.d("Forecast", forecastdata);


                //puts info into object
                JSONObject weatherdata = new JSONObject(forecastdata);

                //converts information into a array
                l = weatherdata.getJSONArray("list");

                JSONObject finaldata = new JSONObject();

                ArrayList<JSONObject>  weathers = new ArrayList<JSONObject>();

                Log.d("weather data", l.toString());

                for (int i = 0; i<l.length(); i+=8){

                    weathers.add(l.getJSONObject(i));
                }


                dayList= new ArrayList<Weather>();
                currenttemp = l.getJSONObject(0).getJSONObject("main").getDouble("temp_max");
                currentconditions = l.getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("description");
                Log.d("currentconditions", currentconditions);


                Log.d("current temp", Double.toString(currenttemp));

                for(int i=0; i<weathers.size(); i++) {
                    JSONObject day = weathers.get(i);
                    String date = day.getString("dt_txt");
                    String conditions = day.getJSONArray("weather").getJSONObject(0).getString("description");
                    double min = day.getJSONObject("main").getDouble("temp_min");
                    double max = day.getJSONObject("main").getDouble("temp_max");
                    dayList.add(new Weather(date, conditions, min, max));
                }



                Log.d("day list", dayList.toString());

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }



            return dayList;
        }


        @Override
        protected void onPostExecute(ArrayList arrayList) {
            try {
                if (arrayList != null) {
                    super.onPostExecute(dayList);
                    textView.setText("                  Latitude: " + Double.toString(location.getDouble("lat")) + "        Longitude: " + Double.toString(location.getDouble("lon")) + "\n\n                                   Location: " + location.getString("name"));
                    CustomAdapter adapter = new CustomAdapter(MainActivity.this, R.layout.adapterlayout, dayList);
                    listView.setAdapter(adapter);
                    //sets current temperature
                    current.setText("Current Temperature: " + Double.toString(currenttemp));
                    //sets image for current conditions
                    if (currentconditions.contains("clear")) {
                        imageView2.setImageResource(R.drawable.clear);
                        quote.setText("Great Day To Take The Dog Out On A Walk!");
                    } else if (currentconditions.contains("clouds")) {
                        imageView2.setImageResource(R.drawable.cloudy);
                        quote.setText("Cloudy skies? No problem, your dog brings the sunshine wherever they go!");
                    } else if (currentconditions.contains("rain")) {
                        imageView2.setImageResource(R.drawable.rainy);
                        quote.setText("It's Raining Cats and Dogs Out There!");
                    } else if (currentconditions.contains("snow")) {
                        imageView2.setImageResource(R.drawable.snow);
                        quote.setText(" Snow Days Are Better With A Furry Playmate.\"");
                    } else if (currentconditions.contains("sunny")) {
                        imageView2.setImageResource(R.drawable.sunny);
                        quote.setText("Enjoy The Sunshine With Your Furry Friend By Your Side.");
                    }
                }
                else
                    Toast.makeText(MainActivity.this, "Please Re-Enter a Valid Zip Code", Toast.LENGTH_LONG).show();
                } catch(JSONException e){
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
        }
        }
    }


