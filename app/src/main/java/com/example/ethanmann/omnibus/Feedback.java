package com.example.ethanmann.omnibus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

public class Feedback extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        final Toast toast = Toast.makeText(this, "Feedback Sent! Thank you!", Toast.LENGTH_SHORT);
        final Button sendFeedback = (Button) findViewById(R.id.sendFeedback);
        sendFeedback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((EditText) findViewById(R.id.subject)).setText("");
                ((EditText) findViewById(R.id.feedbackMsg)).setText("");
                if(toast != null) {
                    toast.show();
                }
            }
        });
    }
}
