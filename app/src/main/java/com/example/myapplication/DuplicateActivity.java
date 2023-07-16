package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DuplicateActivity extends AppCompatActivity {

    private RecyclerView billRecyclerView;
    private List<Bill> billList;
    private BillAdapter billAdapter;
    private DatabaseReference billsRef;
    private FirebaseUser currentUser;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duplicate);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE); // Show progress indicator

        billRecyclerView = findViewById(R.id.billRecyclerView);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing_between_cards);
        billRecyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        billList = new ArrayList<>();
        billAdapter = new BillAdapter(billList);

        billRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        billRecyclerView.setAdapter(billAdapter);

        // Get the current user
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // User is not authenticated, handle accordingly
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            // Get the bills reference for the current user
            billsRef = FirebaseDatabase.getInstance("https://restrobill-f91e9-default-rtdb.asia-southeast1.firebasedatabase.app").getReference()
                    .child(currentUser.getUid());

            // Listen for changes in the bills node

            billsRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                    // Retrieve the bill data and add it to the list
                    Bill bill = dataSnapshot.getValue(Bill.class);
                    if (bill != null) {
                        billList.add(bill);
                        billAdapter.notifyDataSetChanged();
                    }
                    progressBar.setVisibility(View.GONE); // Hide progress indicator
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                // Implement other ChildEventListener methods as needed

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(DuplicateActivity.this,
                            "Failed to load bills: " + databaseError.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE); // Hide progress indicator
                }
            });

            // Set click listener for bill cards
            billAdapter.setOnItemClickListener(new BillAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    // Open another screen and pass the selected bill
                    Bill selectedBill = billList.get(position);
                    Intent intent = new Intent(DuplicateActivity.this, BillDetailsActivity.class);
                    intent.putExtra("bill", selectedBill);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Handle back button click here
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
