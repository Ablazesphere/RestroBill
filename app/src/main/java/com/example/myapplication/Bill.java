package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Bill implements Parcelable {
    private String billId;
    private String date;
    private List<FoodItem> foodItems;
    private double subtotal;
    private double foodTax;
    private double localTax;
    private double totalAmount;

    public Bill() {
        // Empty constructor needed for Firebase Realtime Database
    }

    public Bill(String billId, String date, List<FoodItem> foodItems, double subtotal, double foodTax, double localTax, double totalAmount) {
        this.billId = billId;
        this.date = date;
        this.foodItems = foodItems;
        this.subtotal = subtotal;
        this.foodTax = foodTax;
        this.localTax = localTax;
        this.totalAmount = totalAmount;
    }

    protected Bill(Parcel in) {
        billId = in.readString();
        date = in.readString();
        foodItems = in.createTypedArrayList(FoodItem.CREATOR);
        subtotal = in.readDouble();
        foodTax = in.readDouble();
        localTax = in.readDouble();
        totalAmount = in.readDouble();
    }

    public static final Creator<Bill> CREATOR = new Creator<Bill>() {
        @Override
        public Bill createFromParcel(Parcel in) {
            return new Bill(in);
        }

        @Override
        public Bill[] newArray(int size) {
            return new Bill[size];
        }
    };

    public String getBillId() {
        return billId;
    }

    public String getDate() {
        return date;
    }

    public List<FoodItem> getFoodItems() {
        return foodItems;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getFoodTax() {
        return foodTax;
    }

    public double getLocalTax() {
        return localTax;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(billId);
        dest.writeString(date);
        dest.writeTypedList(foodItems);
        dest.writeDouble(subtotal);
        dest.writeDouble(foodTax);
        dest.writeDouble(localTax);
        dest.writeDouble(totalAmount);
    }
}
