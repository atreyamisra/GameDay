package com.example.pratyushsingh.scoreapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HomeScreen extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        RetrieveData hello = new RetrieveData();
        hello.populateTeam();

        RetrieveData retrieveData = new RetrieveData();
        retrieveData.execute();


    }


}
