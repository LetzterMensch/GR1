package com.example.gr1.ui.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.gr1.model.Goal;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tracking";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME_1 = "diary";
    private static final String TABLE_NAME_2 = "goal";

    private static final String DIARY_ID = "id";
    private static final String DIARY_TOTAL_CALO = "totalCalories";
    private static final String DIARY_CALO_IN = "caloriesIn";
    private static final String DIARY_CALO_OUT = "caloriesOut";
    private static final String DIARY_TOTAL_PROTEIN = "totalProtein";
    private static final String DIARY_TOTAL_FAT = "totalFat";
    private static final String DIARY_TOTAL_CARB = "totalCarb";
    private static final String DIARY_CREATED_AT = "createdAt";

    private static final String GOAL_ID = "id";
    private static final String GOAL_BMR = "BMR";
    private static final String GOAL_WEIGHT = "weight";
    private static final String GOAL_HEIGHT = "height";
    private static final String GOAL_AGE = "age";
    private static final String GOAL_GENDER = "gender";
    private static final String GOAL_ACTIVITY_LEVEL = "activityLevel";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_diary_table = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY" +
                        ", %s TEXT, %s TEXT, %s TEXT" +
                        ", %s TEXT, %s TEXT, %s TEXT" +
                        ", %s TEXT)", TABLE_NAME_1,DIARY_ID,
                DIARY_TOTAL_CALO, DIARY_CALO_IN, DIARY_CALO_OUT,
                DIARY_TOTAL_PROTEIN, DIARY_TOTAL_FAT, DIARY_TOTAL_CARB,
                DIARY_CREATED_AT
        );
        db.execSQL(create_diary_table);

        String create_goal_table = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY" +
                        ", %s REAL, %s REAL, %s REAL" +
                        ", %s REAL, %s TEXT, %s TEXT)", TABLE_NAME_2, GOAL_ID, GOAL_BMR,
                GOAL_WEIGHT, GOAL_HEIGHT, GOAL_AGE, GOAL_GENDER, GOAL_ACTIVITY_LEVEL);
        db.execSQL(create_goal_table);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop_diary_table = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME_1);
        String drop_goal_table = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME_2);
        db.execSQL(drop_diary_table);
        db.execSQL(drop_goal_table);
        onCreate(db);
    }

    public Goal getGoal() {
        SQLiteDatabase db = this.getReadableDatabase();
        Goal goal;
        Cursor cursor = db.rawQuery("select * from goal",null);
        if (!cursor.moveToNext()) {
            goal = new Goal(0, 0.0F,
                    0.0F, 0.0F, 0,
                    "", "VERY_LOW");
        } else {
            cursor.moveToLast();
            System.out.println("col 1 : "+cursor.getInt(0));
            System.out.println("col 2 : "+cursor.getInt(1));

            goal = new Goal(cursor.getInt(0), cursor.getFloat(1),
                    cursor.getFloat(2), cursor.getFloat(3), cursor.getInt(4),
                    cursor.getString(5), cursor.getString(6));
            System.out.println("GOAL : "+goal.getBMR());
        }
        cursor.close();
        return goal;
    }

    public void insertGoal(Goal goal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GOAL_BMR, goal.getBMR());
        values.put(GOAL_HEIGHT, goal.getHeight());
        values.put(GOAL_WEIGHT, goal.getWeight());
        values.put(GOAL_AGE, goal.getAge());
        values.put(GOAL_GENDER, goal.getGender());
        values.put(GOAL_ACTIVITY_LEVEL, goal.getActivityLevel());
        db.insert(TABLE_NAME_2, null, values);
        System.out.println("Insert successfully !" + goal.getBMR());
        db.close();
    }
}
