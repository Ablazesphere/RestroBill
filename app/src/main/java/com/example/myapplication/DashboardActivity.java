package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardActivity extends AppCompatActivity {
    FirebaseAuth auth;
    Button button;
    TextView textView;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        CardView adminCardView = findViewById(R.id.cardView2);
        adminCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the new activity or perform desired action
                Intent intent = new Intent(DashboardActivity.this, AdminActivity.class);
                startActivity(intent);

            }
        });

        CardView menuCardView = findViewById(R.id.cardView1);
        menuCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the new activity or perform desired action
                Intent intent = new Intent(DashboardActivity.this, MenuActivity.class);
                startActivity(intent);

            }
        });

        CardView menubasicCardView = findViewById(R.id.cardView3);
        menubasicCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the new activity or perform desired action
                Intent intent = new Intent(DashboardActivity.this, MenuBasicActivity.class);
                startActivity(intent);

            }
        });

        CardView DuplicateCardView = findViewById(R.id.cardView4);
        DuplicateCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the new activity or perform desired action
                Intent intent = new Intent(DashboardActivity.this, DuplicateActivity.class);
                startActivity(intent);

            }
        });

        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView2);
        user = auth.getCurrentUser();

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), AdminLoginActivity.class);
            startActivity(intent);
            finish();
        }

        else {
            textView.setText(user.getEmail());
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }
}