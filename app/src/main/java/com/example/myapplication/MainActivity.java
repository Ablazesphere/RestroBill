package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen_layout);

        Button adminLoginButton = findViewById(R.id.adminLoginButton);
        Button signUpButton = findViewById(R.id.signUpButton);

        adminLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Admin Login button click
                // Add your logic here
                Intent intent = new Intent(MainActivity.this, AdminLoginActivity.class);
                startActivity(intent);

            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Sign up button click
                // Add your logic here
                Intent intent = new Intent(MainActivity.this, AdminSignupActivity.class);
                startActivity(intent);

            }
        });
    }
}
