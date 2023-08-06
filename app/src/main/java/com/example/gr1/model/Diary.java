package com.example.gr1.model;

import android.os.Build;

import java.time.LocalDate;
import java.util.List;

public class Diary {
    int id;
    private float totalCalories;
    private float caloriesIn;
    private float caloriesOut;
    private float totalProtein;
    private float totalFat;
    private float totalCarb;
    private String foodList;
    private LocalDate createdAt;

    public Diary(float totalCalories){
        this.totalCalories = totalCalories;
        this.caloriesIn = 0.0F;
        this.caloriesOut = 0.0F;
        this.totalProtein = 0.0F;
        this.totalFat = 0.0F;
        this.totalCarb = 0.0F;
        this.foodList = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.createdAt = LocalDate.now();
        }
    }
    public Diary(int id, float totalCalories, float caloriesIn, float caloriesOut,
                 float totalProtein, float totalFat, float totalCarb,
                 String foodList, LocalDate createdAt) {
        this.id = id;
        this.foodList = foodList;
        this.totalCalories = totalCalories;
        this.caloriesIn = caloriesIn;
        this.caloriesOut = caloriesOut;
        this.totalProtein = totalProtein;
        this.totalFat = totalFat;
        this.totalCarb = totalCarb;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(float totalCalories) {
        this.totalCalories = totalCalories;
    }

    public float getCaloriesIn() {
        return caloriesIn;
    }

    public void setCaloriesIn(float caloriesIn) {
        this.caloriesIn = caloriesIn;
    }

    public float getCaloriesOut() {
        return caloriesOut;
    }

    public void setCaloriesOut(float caloriesOut) {
        this.caloriesOut = caloriesOut;
    }

    public float getTotalProtein() {
        return totalProtein;
    }

    public void setTotalProtein(float totalProtein) {
        this.totalProtein = totalProtein;
    }

    public float getTotalFat() {
        return totalFat;
    }

    public void setTotalFat(float totalFat) {
        this.totalFat = totalFat;
    }

    public float getTotalCarb() {
        return totalCarb;
    }

    public void setTotalCarb(float totalCarb) {
        this.totalCarb = totalCarb;
    }

    public String getFoodList() {
        return foodList;
    }

    public void setFoodList(String foodList) {
        this.foodList = this.foodList +'#'+ foodList;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
}
