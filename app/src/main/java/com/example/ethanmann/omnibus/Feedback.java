package com.example.ethanmann.omnibus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;

public class Feedback extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        final Button sendFeedback = (Button) findViewById(R.id.sendFeedback);
        sendFeedback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


            }
        });
    }
}
