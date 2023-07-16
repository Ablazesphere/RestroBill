package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private List<FoodItem> foodItems;
    private List<FoodItem> filteredList;
    private TotalPriceListener totalPriceListener;
    private double totalPrice;

    public MenuAdapter(List<FoodItem> foodItems, TotalPriceListener totalPriceListener) {
        this.foodItems = foodItems;
        this.filteredList = new ArrayList<>(foodItems);
        this.totalPriceListener = totalPriceListener;
        this.totalPrice = 0.0;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        final FoodItem foodItem = filteredList.get(position);
        holder.foodNameTextView.setText(foodItem.getName());
        holder.priceTextView.setText(foodItem.getPrice());
        holder.quantityTextView.setText(String.valueOf(foodItem.getQuantity()));

        // Set click listener for the add button
        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle add button click event
                foodItem.setQuantity(foodItem.getQuantity() + 1);
                double itemPrice = parsePrice(foodItem.getPrice());
                totalPrice += itemPrice;
                if (totalPriceListener != null) {
                    totalPriceListener.onTotalPriceUpdated(totalPrice);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void filterList(List<FoodItem> filteredList) {
        this.filteredList = filteredList;
        notifyDataSetChanged();
    }

    public List<FoodItem> getSelectedFoodItems() {
        List<FoodItem> selectedItems = new ArrayList<>();
        for (FoodItem foodItem : foodItems) {
            if (foodItem.getQuantity() > 0) {
                selectedItems.add(foodItem);
            }
        }
        return selectedItems;
    }

    private double parsePrice(String price) {
        // Assuming the price format is "\u20B9X.XX"
        return Double.parseDouble(price.substring(1));
    }

    static class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView foodNameTextView;
        TextView priceTextView;
        TextView quantityTextView;
        Button addButton;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            foodNameTextView = itemView.findViewById(R.id.foodNameTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            addButton = itemView.findViewById(R.id.addButton);
        }
    }

    public interface TotalPriceListener {
        void onTotalPriceUpdated(double totalPrice);
    }
}
