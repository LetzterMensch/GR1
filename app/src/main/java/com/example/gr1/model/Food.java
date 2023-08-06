package com.example.gr1.model;

public class Food {
    int id;
    private String name;
    private float calories;
    private float serving;
    private float protein;
    private float fat;
    private float carb;

    public Food(){
        this.name = "";
        this.calories = 0.0F;
        this.serving = 0.0F;
        this.protein = 0.0F;
        this.fat = 0.0F;
        this.carb = 0.0F;
    }
    public Food(String name, float calories, float serving, float protein, float fat, float carb) {
        this.name = name;
        this.calories = calories;
        this.serving = serving;
        this.protein = protein;
        this.fat = fat;
        this.carb = carb;
    }
    public Food(int id, String name, float calories, float serving, float protein, float fat, float carb) {
        this.id = id;
        this.name = name;
        this.calories = calories;
        this.serving = serving;
        this.protein = protein;
        this.fat = fat;
        this.carb = carb;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getCalories() {
        return calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public float getServing() {
        return serving;
    }

    public void setServing(int  serving) {
        this.serving = serving;
    }

    public float getProtein() {
        return protein;
    }

    public void setProtein(float protein) {
        this.protein = protein;
    }

    public float getFat() {
        return fat;
    }

    public void setFat(float fat) {
        this.fat = fat;
    }

    public float getCarb() {
        return carb;
    }

    public void setCarb(float carb) {
        this.carb = carb;
    }
}
