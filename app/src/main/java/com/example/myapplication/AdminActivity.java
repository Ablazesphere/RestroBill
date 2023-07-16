package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private LinearLayout userListLayout;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userListLayout = findViewById(R.id.userListLayout);
        firestore = FirebaseFirestore.getInstance();

        fetchUserList();
    }

    private void fetchUserList() {
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        firestore.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> documentSnapshots = task.getResult().getDocuments();
                    for (DocumentSnapshot document : documentSnapshots) {
                        String email = document.getString("email");
                        String createdDate = document.getString("createdDate");
                        String uid = document.getString("id");

                        addUserCard(email, createdDate, uid);
                    }
                } else {
                    Toast.makeText(AdminActivity.this, "Error fetching user list", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addUserCard(String email, String createdDate, String uid) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View userCardView = inflater.inflate(R.layout.user_card_layout, userListLayout, false);

        TextView emailTextView = userCardView.findViewById(R.id.emailTextView);
        TextView createdDateTextView = userCardView.findViewById(R.id.createdDateTextView);
        TextView uidTextView = userCardView.findViewById(R.id.uidTextView);


        emailTextView.setText(email);
        createdDateTextView.setText(createdDate);
        uidTextView.setText(uid);



        userListLayout.addView(userCardView);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Handle back button click here
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
