package com.example.ethanmann.omnibus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Settings extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
    }
    public static void initSettings(){
        //reads users settings from DB, sets appropriate variables and toggles state of settings on settings page
    }
}
