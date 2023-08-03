package com.example.gr1.model;

import java.util.List;

public class Food {
    int id;
    private String name;
    private float calories;
    private float serving;
    private float totalProtein;
    private float totalFat;
    private float totalCarb;
    private float sugar;
    private float salt;

    public Food(int id, String name, float calories, float serving, float totalProtein, float totalFat, float totalCarb, float sugar, float salt) {
        this.id = id;
        this.name = name;
        this.calories = calories;
        this.serving = serving;
        this.totalProtein = totalProtein;
        this.totalFat = totalFat;
        this.totalCarb = totalCarb;
        this.sugar = sugar;
        this.salt = salt;
    }
}
