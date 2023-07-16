package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MenuBasicAdapter extends RecyclerView.Adapter<MenuBasicAdapter.MenuViewHolder> {

    private List<FoodItem> foodItems;
    private List<FoodItem> filteredItems;

    public MenuBasicAdapter(List<FoodItem> foodItems) {
        this.foodItems = foodItems;
        this.filteredItems = new ArrayList<>(foodItems);
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        FoodItem foodItem = filteredItems.get(position);
        holder.bind(foodItem);
    }

    @Override
    public int getItemCount() {
        return filteredItems.size();
    }

    public void filterList(List<FoodItem> filteredList) {
        filteredItems.clear();
        filteredItems.addAll(filteredList);
        notifyDataSetChanged();
    }

    static class MenuViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView priceTextView;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.foodNameTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
        }

        public void bind(FoodItem foodItem) {
            nameTextView.setText(foodItem.getName());
            priceTextView.setText(foodItem.getPrice());
        }
    }
}
