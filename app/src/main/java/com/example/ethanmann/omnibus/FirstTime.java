package com.example.ethanmann.omnibus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class FirstTime extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_user);

        final Button student = findViewById(R.id.student);
        student.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Settings.setUserType("Student");
                start(true);
            }
        });

        final Button driver = findViewById(R.id.driver);
        driver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Settings.setUserType("Driver");
                start(false);
            }
        });

    }
    private void start(boolean b) {
        Intent studentLogin = new Intent(this, StudentLogin.class);
        Intent Idriver = new Intent(this, DriverHome.class);
        if(b)
            startActivity(studentLogin);
        else
            startActivity(Idriver);
    }

}
