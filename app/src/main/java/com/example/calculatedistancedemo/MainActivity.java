package com.example.calculatedistancedemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    EditText etSource,etDestination;
    TextView textView;
    String sType;
    double Lat1 = 0,Long1 = 0,Lat2 = 0,Long2 = 0;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etSource = findViewById(R.id.et_source);
        etDestination = findViewById(R.id.et_destination);
        textView = findViewById(R.id.text_view);

        Places.initialize(getApplicationContext(),"AIzaSyCYd9DNtP8fAnic_H5XwgCef7dmqj_7vB0");

        etSource.setFocusable(false);
        etSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sType = "source";
                List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG);
                System.out.println(fields);
                Intent intent = new IntentBuilder(AutocompleteActivityMode.OVERLAY,fields).build(MainActivity.this);
                startActivityForResult(intent,100);
            }
        });

        etDestination.setFocusable(false);
        etDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sType="destination";
                List<Place.Field> fields1 = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG);
                Intent intent = new IntentBuilder(
                        AutocompleteActivityMode.OVERLAY.fields1
                ).build(MainActivity.this);
                startActivityForResult(intent,100);
            }
        });

        textView.setText("0.0 Kilometers");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK){
            Place place = Autocomplete.getPlaceFromIntent(data);
            if(sType.equals("source")){
                flag++;
                etSource.setText(place.getAddress());
                String sSource = String.valueOf(place.getLatLng());
                sSource = sSource.replaceAll("lat/lng","");
                sSource = sSource.replace("(","");
                sSource =sSource.replace(")","");
                String[] split = sSource.split(",");
                Lat1 = Double.parseDouble(split[0]);
                Long1 = Double.parseDouble(split[1]);

            }else{
                flag++;
                etDestination.setText(place.getAddress());
                String sDestination = String.valueOf(place.getLatLng());
                sDestination = sDestination.replaceAll("lat/lng","");
                sDestination =sDestination.replace("(","");
                sDestination = sDestination.replace(")","");
                String[] split = sDestination.split(",");
                Lat2 = Double.parseDouble(split[0]);
                Long2 =  Double.parseDouble(split[1]);
            }
                if(flag >= 2){
                    distance(Lat1,Long1,Lat2,Long2);
                }
        }else if (requestCode == AutocompleteActivity.RESULT_ERROR){
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getApplicationContext(),status.getStatusMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void distance(double lat1, double long1, double lat2, double long2) {
        double longDiff = long1 - long2;
        double distance = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) +Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(longDiff));
        distance = Math.acos(distance);
        distance = rad2deg(distance);
        distance = distance * 60 * 1.1515;
        distance =distance * 1.609344;
        textView.setText(String.format(Locale.US,"%2f Kilometers",distance));
    }

    private double rad2deg(double distance) {
        return (distance * 180.0 / Math.PI);
    }

    private double deg2rad(double lat1) {
        return (lat1*Math.PI/180.0);
    }

    private class IntentBuilder {
        public IntentBuilder(AutocompleteActivityMode overlay, List<Place.Field> fields) {
        }


    }
}