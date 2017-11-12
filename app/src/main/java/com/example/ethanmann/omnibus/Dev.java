package com.example.ethanmann.omnibus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Dev extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dev);
    }
    public void devOpenPage(String page) {
        Intent dev = new Intent(this, Dev.class);
        Intent studentHome = new Intent(this, StudentHome.class);
        Intent driverHome = new Intent(this, DriverHome.class);
        if(page.equalsIgnoreCase("studentHome")) //checks if user is student
            startActivity(studentHome); //take user to StudentHome
        else if (page.equalsIgnoreCase("driverHome")) //user is a driver
            startActivity(driverHome); //take user to DriverHome
        else if (page.equalsIgnoreCase("studentSettings"))//user is a developer
           startActivity(dev);
        else if (page.equalsIgnoreCase("driverSettings")) //user is a developer
            startActivity(dev);
    }
}
