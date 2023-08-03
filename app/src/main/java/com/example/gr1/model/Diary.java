package com.example.gr1.model;

import java.time.LocalDate;
import java.util.List;

public class Diary {
    int id;
    private List<Food> foodList;
    private float totalCalories;
    private float caloriesIn;
    private float caloriesOut;
    private float totalProtein;
    private float totalFat;
    private float totalCarb;
    private LocalDate createdAt;

    public Diary(int id, List<Food> foodList, float totalCalories, float caloriesIn, float caloriesOut, float totalProtein, float totalFat, float totalCarb, LocalDate createdAt) {
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
}
