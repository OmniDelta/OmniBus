package com.example.ethanmann.omnibus;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BusInfo extends AppCompatActivity{
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference info = database.getReference("busses/A/info");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_info);
        info.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<String> Userlist = new ArrayList<String>();
                // Result will be held Here
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    Userlist.add(String.valueOf(dsp.getValue())); //add result into array list
                }
                ((TextView) findViewById(R.id.info_company)).setText(Userlist.get(0));
                ((TextView) findViewById(R.id.info_busNumber)).setText(Userlist.get(1));
                ((TextView) findViewById(R.id.info_district)).setText(Userlist.get(2));
                ((TextView) findViewById(R.id.info_school)).setText(Userlist.get(3));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        }
    }