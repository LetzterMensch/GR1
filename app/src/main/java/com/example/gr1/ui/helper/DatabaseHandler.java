package com.example.gr1.ui.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import com.example.gr1.model.Diary;
import com.example.gr1.model.Food;
import com.example.gr1.model.Goal;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tracking";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME_1 = "diary";
    private static final String TABLE_NAME_2 = "goal";
    private static final String TABLE_NAME_3 = "food";

    private static final String DIARY_ID = "id";
    private static final String DIARY_TOTAL_CALO = "totalCalories";
    private static final String DIARY_CALO_IN = "caloriesIn";
    private static final String DIARY_CALO_OUT = "caloriesOut";
    private static final String DIARY_TOTAL_PROTEIN = "totalProtein";
    private static final String DIARY_TOTAL_FAT = "totalFat";
    private static final String DIARY_TOTAL_CARB = "totalCarb";
    private static final String DIARY_FOOD_LIST = "foodList";
    private static final String DIARY_CREATED_AT = "createdAt";

    private static final String GOAL_ID = "id";
    private static final String GOAL_BMR = "BMR";
    private static final String GOAL_WEIGHT = "weight";
    private static final String GOAL_HEIGHT = "height";
    private static final String GOAL_AGE = "age";
    private static final String GOAL_GENDER = "gender";
    private static final String GOAL_ACTIVITY_LEVEL = "activityLevel";
    private static final String GOAL_PURPOSE = "purpose";

    private static final String FOOD_ID = "id";
    private static final String FOOD_NAME = "name";
    private static final String FOOD_CALORIES = "calories";
    private static final String FOOD_SERVING = "serving";
    private static final String FOOD_FAT = "fat";
    private static final String FOOD_PROTEIN = "protein";
    private static final String FOOD_CARB = "carb";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_diary_table = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY" +
                        ", %s REAL, %s REAL, %s REAL" +
                        ", %s REAL, %s REAL, %s REAL" +
                        ", %s TEXT, %s TEXT)", TABLE_NAME_1, DIARY_ID,
                DIARY_TOTAL_CALO, DIARY_CALO_IN, DIARY_CALO_OUT,
                DIARY_TOTAL_PROTEIN, DIARY_TOTAL_FAT, DIARY_TOTAL_CARB,
                DIARY_FOOD_LIST,DIARY_CREATED_AT
        );
        db.execSQL(create_diary_table);

        String create_goal_table = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY" +
                        ", %s REAL, %s REAL, %s REAL" +
                        ", %s REAL, %s TEXT, %s TEXT, %s INT)", TABLE_NAME_2, GOAL_ID,
                GOAL_BMR, GOAL_WEIGHT, GOAL_HEIGHT,
                GOAL_AGE, GOAL_GENDER, GOAL_ACTIVITY_LEVEL, GOAL_PURPOSE);
        db.execSQL(create_goal_table);

        String create_food_table = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY" +
                        ", %s TEXT, %s REAL, %s INT" +
                        ", %s REAL, %s REAL, %s REAL)", TABLE_NAME_3, FOOD_ID,
                FOOD_NAME, FOOD_CALORIES, FOOD_SERVING,
                FOOD_FAT, FOOD_PROTEIN, FOOD_CARB);
        db.execSQL(create_food_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop_diary_table = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME_1);
        String drop_goal_table = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME_2);
        String drop_food_table = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME_3);
        db.execSQL(drop_diary_table);
        db.execSQL(drop_goal_table);
        db.execSQL(drop_food_table);
        onCreate(db);
    }

    public Goal getGoal() {
        SQLiteDatabase db = this.getReadableDatabase();
        Goal goal;
        Cursor cursor = db.rawQuery("select * from goal", null);
        if (!cursor.moveToNext()) {
            goal = new Goal(0, 0.0F,
                    0.0F, 0.0F, 0,
                    "", "VERY_LOW", -250);
        } else {
            cursor.moveToLast();
            goal = new Goal(cursor.getInt(0), cursor.getFloat(1),
                    cursor.getFloat(2), cursor.getFloat(3), cursor.getInt(4),
                    cursor.getString(5), cursor.getString(6), cursor.getInt(7));
        }
        cursor.close();
        return goal;
    }

    public void insertGoal(Goal goal) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_2, null, null);
        ContentValues values = new ContentValues();
        values.put(GOAL_BMR, goal.getBMR());
        values.put(GOAL_HEIGHT, goal.getHeight());
        values.put(GOAL_WEIGHT, goal.getWeight());
        values.put(GOAL_AGE, goal.getAge());
        values.put(GOAL_GENDER, goal.getGender());
        values.put(GOAL_ACTIVITY_LEVEL, goal.getActivityLevel());
        values.put(GOAL_PURPOSE, goal.getPurpose());
        db.insert(TABLE_NAME_2, null, values);
        db.close();
    }

    public int insertFood(Food food) {
        int a = 0; //0 - successful, 1 - failed
        SQLiteDatabase dbWrite = this.getWritableDatabase();
        SQLiteDatabase dbRead = this.getReadableDatabase();
        Cursor cursor = dbRead.rawQuery("select * from food where name = ? and serving = ?",
                new String[]{food.getName(), String.valueOf(food.getServing())});
        if(!cursor.moveToNext()){
            ContentValues values = new ContentValues();
            values.put(FOOD_NAME, food.getName());
            values.put(FOOD_SERVING, food.getServing());
            values.put(FOOD_CALORIES, food.getCalories());
            values.put(FOOD_PROTEIN, food.getProtein());
            values.put(FOOD_FAT, food.getFat());
            values.put(FOOD_CARB, food.getCarb());
            dbWrite.insert(TABLE_NAME_3, null, values);
        }else {
            a = 1;
        }
        dbRead.close();
        dbWrite.close();
        return a;
    }
    public String[] getFood(String id){
        String[] food = new String[6];
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from food where id = ?", new String[]{id});
        if(cursor.moveToNext()){
            food[0] = String.format("Name :       %21s",cursor.getString(1));
            food[1] = String.format("Calories:    %20s",cursor.getString(2));
            food[2] = String.format("Serving(g):  %17s",cursor.getString(3));
            food[3] = String.format("Protein(g):  %18s",cursor.getString(4));
            food[4] = String.format("Fat(g):      %21s",cursor.getString(5));
            food[5] = String.format("Carb(g):     %19s",cursor.getString(6));
        }else{
            System.out.println("Failed !");
        }
        return food;
    }
    public List<Food> getAllFood() {
        List<Food> foodList = new ArrayList<>();
        Food food;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from food", null);
        if (!cursor.moveToNext()) {
            food = new Food();
            foodList.add(food);
        } else {
            cursor.moveToLast();
            do {
                food = new Food(cursor.getInt(0), cursor.getString(1),
                        cursor.getFloat(2), cursor.getInt(3), cursor.getFloat(4),
                        cursor.getFloat(5), cursor.getFloat(6));
                foodList.add(food);
            } while (cursor.moveToPrevious());
        }
        cursor.close();
        return foodList;
    }

    public void insertDiary(Diary diary){
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase dbRead = this.getReadableDatabase();

        Cursor cursor = dbRead.rawQuery("select id from diary where createdAt = ?",
                new String[]{String.valueOf(diary.getCreatedAt())});
        if(!cursor.moveToNext()) {
            ContentValues values = new ContentValues();
            values.put(DIARY_TOTAL_CALO, diary.getTotalCalories());
            values.put(DIARY_CALO_IN, diary.getCaloriesIn());
            values.put(DIARY_CALO_OUT, diary.getCaloriesOut());
            values.put(DIARY_FOOD_LIST, diary.getFoodList());
            values.put(DIARY_TOTAL_FAT, diary.getTotalFat());
            values.put(DIARY_TOTAL_PROTEIN, diary.getTotalProtein());
            values.put(DIARY_TOTAL_CARB, diary.getTotalCarb());
            values.put(DIARY_CREATED_AT, diary.getCreatedAt().toString());
            db.insert(TABLE_NAME_1, null, values);
        }else{
            ContentValues values = new ContentValues();
            values.put(DIARY_TOTAL_CALO, diary.getTotalCalories());
            values.put(DIARY_CALO_IN, diary.getCaloriesIn());
            values.put(DIARY_CALO_OUT, diary.getCaloriesOut());
            values.put(DIARY_FOOD_LIST, diary.getFoodList());
            values.put(DIARY_TOTAL_FAT, diary.getTotalFat());
            values.put(DIARY_TOTAL_PROTEIN, diary.getTotalProtein());
            values.put(DIARY_TOTAL_CARB, diary.getTotalCarb());
            values.put(DIARY_CREATED_AT, diary.getCreatedAt().toString());
            db.update(TABLE_NAME_1,values," id = ?",new String[]{cursor.getString(0)});
        }
        dbRead.close();
        db.close();
    }
    public void appendFood(List<Food> foodList, Diary diary){
        SQLiteDatabase db = this.getWritableDatabase();
        for (Food food: foodList) {
            String query = "UPDATE diary SET foodList = foodList ||"
                    +food.getId()+ "WHERE id ="+diary.getId()+";";
            db.execSQL(query);
        }
        db.close();
    }
    public Diary getTodayDiary(LocalDate today){
        SQLiteDatabase db = this.getReadableDatabase();
        Diary diary = new Diary(getGoal().getBMR());
        Cursor cursor = db.rawQuery("select * from diary where createdAt = ?", new String[]{today.toString()});
        if (!cursor.moveToNext()) {
            insertDiary(diary);
        }else{
            if(cursor.getFloat(1) == 0.0F){
                insertDiary(diary);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                diary = new Diary(cursor.getInt(0),cursor.getFloat(1),cursor.getFloat(2)
                        ,cursor.getFloat(3),cursor.getFloat(4),cursor.getFloat(5),
                        cursor.getFloat(6),cursor.getString(7),LocalDate.parse(cursor.getString(8)));
            }
        }
        return diary;
    }
    public List<Food> getDiaryFood(String foodStr){
        List<Food> foodList = new ArrayList<>();
        List<String> foodNameList = Arrays.asList(foodStr.split("#"));
        Food food;
        SQLiteDatabase db = this.getReadableDatabase();
        for (String foodName: foodNameList
             ) {
            Cursor cursor = db.rawQuery("select * from food where name = ?", new String[]{foodName});
            if(cursor.moveToNext()){
                food = new Food(cursor.getInt(0), cursor.getString(1),
                        cursor.getFloat(2), cursor.getInt(3), cursor.getFloat(4),
                        cursor.getFloat(5), cursor.getFloat(6));
                foodList.add(food);
            }
            cursor.close();
        }
        return foodList;
    }
    public List<Diary> getAllDiary() {
        //For statistics.
        SQLiteDatabase db = this.getReadableDatabase();
        List<Diary> diaryList = new ArrayList<>();
        Diary diary = new Diary(getGoal().getBMR());
        Cursor cursor = db.rawQuery("select * from diary", null);
        if (!cursor.moveToNext()) {
            diaryList.add(diary);
        } else {
            do {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    diary = new Diary(cursor.getInt(0), cursor.getFloat(1),
                            cursor.getFloat(2), cursor.getFloat(3), cursor.getFloat(4),
                            cursor.getFloat(5), cursor.getFloat(6),cursor.getString(7),
                            LocalDate.parse(cursor.getString(8)));
                }
                diaryList.add(diary);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return diaryList;
    }

}
