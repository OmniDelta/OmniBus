package com.example.ethanmann.omnibus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class NewUser extends AppCompatActivity {
    boolean studentBool = true;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userType = database.getReference("users/"+Settings.UID+"/accountType");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_user);

        final Button student = findViewById(R.id.student);
        student.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                userType.setValue("Student");
                start(true);
            }
        });

        final Button driver = findViewById(R.id.driver);
        driver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                userType.setValue("Driver");
                start(false);
            }
        });

    }
    private void start(boolean b) {
        Intent Istudent = new Intent(this, StudentHome.class);
        Intent Idriver = new Intent(this, DriverHome.class);
        if(b)
            startActivity(Istudent);
        else
            startActivity(Idriver);
    }

}
