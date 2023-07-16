package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class FoodItem implements Parcelable {

    private String name;
    private String price;
    private boolean selected;
    private int quantity;

    public FoodItem(String name, String price) {
        this.name = name;
        this.price = price;
        this.selected = false; // Initialize selected as false by default
        this.quantity = 0; // Initialize quantity as 0 by default
    }

    public FoodItem() {

    }

    protected FoodItem(Parcel in) {
        name = in.readString();
        price = in.readString();
        selected = in.readByte() != 0;
        quantity = in.readInt();
    }

    public static final Creator<FoodItem> CREATOR = new Creator<FoodItem>() {
        @Override
        public FoodItem createFromParcel(Parcel in) {
            return new FoodItem(in);
        }

        @Override
        public FoodItem[] newArray(int size) {
            return new FoodItem[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Implement the Parcelable methods
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(price);
        dest.writeByte((byte) (selected ? 1 : 0));
        dest.writeInt(quantity);
    }
}
