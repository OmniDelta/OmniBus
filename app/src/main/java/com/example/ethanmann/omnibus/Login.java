package com.example.ethanmann.omnibus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class Login extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
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
