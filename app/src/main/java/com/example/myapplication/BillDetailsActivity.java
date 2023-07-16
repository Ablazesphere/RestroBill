package com.example.myapplication;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

public class BillDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        TextView billIdTextView = findViewById(R.id.billIdTextView);

        // Get the selected bill from the intent
        Bill selectedBill = getIntent().getParcelableExtra("bill");
        if (selectedBill != null) {
            if (actionBar != null) {
                actionBar.setTitle(selectedBill.getBillId());
            }

            String address = "	                       23232, JAVA CITY,\n	                                NY, USA\n	                        TEL: 6363956838";
            String separator = "----------------------------------------------------------------------";
            String formattedDate = "Date: " + selectedBill.getDate();
            String formattedItems = formatFoodItems(selectedBill.getFoodItems());
            String subtotal = "Subtotal: \u20B9" + selectedBill.getSubtotal();
            String foodTax = "Food Tax: \u20B9" + selectedBill.getFoodTax();
            String localTax = "Local Tax: \u20B9" + selectedBill.getLocalTax();
            String totalAmount = "Total: \u20B9" + selectedBill.getTotalAmount();

            String billDetails = "		                            Restro Bill\n" +
                    address + "\n" +
                    separator + "\n" +
                    formattedDate + "\n" +
                    separator + "\n" +
                    formattedItems + "" +
                    separator + "\n" +
                    subtotal + "\n" +
                    foodTax + "\n" +
                    localTax + "\n\n" +
                    totalAmount + "\n" +
                    separator + "\n\n" +
                    "                             THANK YOU";

            billIdTextView.setText(billDetails);

        }
    }

    private String formatFoodItems(List<FoodItem> foodItems) {
        StringBuilder foodItemsBuilder = new StringBuilder();
        if (foodItems != null && !foodItems.isEmpty()) {
            for (FoodItem foodItem : foodItems) {
                String itemDetails = "Item " + foodItem.getName() + " - Price " + foodItem.getPrice();
                foodItemsBuilder.append(itemDetails).append("\n");
            }
        }
        return foodItemsBuilder.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
