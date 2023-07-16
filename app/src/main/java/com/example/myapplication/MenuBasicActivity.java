package com.example.myapplication;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MenuBasicActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MenuBasicAdapter adapter;
    private List<FoodItem> foodItems;
    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_basic);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Enable back navigation

        // Initialize the RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create the list of food items
        foodItems = new ArrayList<>();
        foodItems.add(new FoodItem("Pizza", "\u20B9140.99"));
        foodItems.add(new FoodItem("Burger", "\u20B980.99"));
        foodItems.add(new FoodItem("Salad", "\u20B960.99"));
        foodItems.add(new FoodItem("Masala Dosa", "\u20B950.99"));
        foodItems.add(new FoodItem("Caesar Salad", "\u20B9100.99"));
        foodItems.add(new FoodItem("BBQ Ribs", "\u20B9310.99"));
        foodItems.add(new FoodItem("Chocolate Lava Cake", "\u20B975.99"));

        // Initialize and set the adapter for the RecyclerView
        adapter = new MenuBasicAdapter(foodItems);
        recyclerView.setAdapter(adapter);

        // Initialize the search bar
        searchEditText = findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Filter the food items based on the search query
                filterFoodItems(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // Initialize the total price text view
//        totalPriceTextView = findViewById(R.id.totalPriceTextView);
//        updateTotalPrice(); // Update the total price text view initially
    }

    private void filterFoodItems(String query) {
        List<FoodItem> filteredList = new ArrayList<>();
        for (FoodItem foodItem : foodItems) {
            if (foodItem.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(foodItem);
            }
        }
        adapter.filterList(filteredList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Handle back button click here
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Commented out or removed methods related to total price
    // ...
}
