package com.example.ethanmann.omnibus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;

public class Settings extends AppCompatActivity {
    public static String user_name = "";
    public static String user_email = "";
    public static String user_img_url = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
    }
    public static void initSettings(FirebaseUser user){ //sets appropriate variables and toggles state of settings on settings page
        user_name = user.getDisplayName();
        user_email = user.getEmail();
        user_img_url = user.getPhotoUrl().toString();

    }
}
