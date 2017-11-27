package com.example.ethanmann.omnibus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
    public void login(View view)
    {
        Intent student = new Intent(this, StudentHome.class);
        Intent driver = new Intent(this, DriverHome.class);
        Intent dev = new Intent(this, Dev.class);
        if (((EditText)findViewById(R.id.username)).getText().toString().equalsIgnoreCase("dev")) //checks if user is a developer
            startActivity(dev); //opens dev page
        else if(((EditText)findViewById(R.id.username)).getText().toString().equalsIgnoreCase("s")) //checks if user is student
            startActivity(student); //take user to StudentHome
        else if (true) //checks if user is a driver
            startActivity(driver); //take user to DriverHome
    }
}
