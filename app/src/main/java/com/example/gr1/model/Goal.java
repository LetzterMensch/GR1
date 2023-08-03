package com.example.gr1.model;

import java.util.HashMap;
import java.util.Map;

public class Goal {
    public enum Level {
        VERY_LOW("Little/no exercise",1.2),
        LOW("Light exercise (2-3 days/wk)",1.375),
        MEDIUM("Moderate exercise(3-5 days/wk)",1.55),
        HIGH("Very active(6-7 days/wk)",1.725),
        VERY_HIGH("Extra active (athletes/physical job)",1.9);
        final String label;
        //Harris Benedict's Constant
        final double HBConstant;
        private static final Map<String, Level> BY_LABEL = new HashMap<>();
        private static final Map<Level, String> BY_VALUE = new HashMap<>();

        private static final Map<Level, Double> BY_HB_CONSTANTS = new HashMap<>();
        static {
            for (Level e : values()) {
                BY_LABEL.put(e.label, e);
                BY_HB_CONSTANTS.put(e,e.HBConstant);
                BY_VALUE.put(e,e.label);
            }
        }
        Level(String label, double hbConstant) {
            this.label = label;
            HBConstant = hbConstant;
        }


        public static Level valueOfLabel(String label) {
            return BY_LABEL.get(label);
        }
        public static Double valueOfLevel(Level level) {
            return BY_HB_CONSTANTS.get(level);
        }
        public static String valueOfValue(Level level) {
            return BY_VALUE.get(level);
        }
    }
    int id;
    private float BMR;
    private float weight;
    private float height;
    private int age;
    private String gender;
    private Level activityLevel;

    public Goal(){}
    public Goal(int id, float BMR, float weight, float height, int age, String gender, String activityLevel) {
        this.id = id;
        this.BMR = BMR;
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.gender = gender;
        this.activityLevel = Level.valueOf(activityLevel);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getBMR() {
        return BMR;
    }

    public void setBMR(float BMR) {
        this.BMR = BMR;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getActivityLevel() {
        return activityLevel.toString();
    }

    public void setActivityLevel(Level activityLevel) {
        this.activityLevel = activityLevel;
    }
}
