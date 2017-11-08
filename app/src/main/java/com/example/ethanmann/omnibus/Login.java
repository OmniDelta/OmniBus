package com.example.ethanmann.omnibus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class Login extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.omnibus.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }
    public void login(View view)
    {
        Intent student = new Intent(this, StudentHome.class);
        Intent driver = new Intent(this, DriverHome.class);
        //EditText editText = (EditText) findViewById(R.id.username);
        //String message = editText.getText().toString();
        //home.putExtra(EXTRA_MESSAGE, message);
        if(((EditText)findViewById(R.id.username)).getText().toString().equalsIgnoreCase("student")) //checks if user is student
            startActivity(student); //take user to student side of app
        else
            startActivity(driver); //take user to driver side of app
    }
}
