package com.example.ethanmann.omnibus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Settings extends AppCompatActivity {
    public static String UID = "";
    public static String user_name = "";
    public static String user_email = "";
    public static String user_img_url = "";
    public static String user_type = "";
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
    }
    public static void setUserType(String type){
        user_type = type;
    }
    public static void initSettings(FirebaseUser user){ //sets appropriate variables and toggles state of settings on settings page
        UID = user.getUid();
        user_name = user.getDisplayName();
        user_email = user.getEmail();
        user_img_url = user.getPhotoUrl().toString();

    }
}
