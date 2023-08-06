package com.example.gr1;

import android.os.Build;

import androidx.lifecycle.ViewModel;

import com.example.gr1.model.Diary;
import com.example.gr1.model.Goal;
import com.example.gr1.ui.helper.DatabaseHandler;

import java.io.Closeable;
import java.time.LocalDate;

public class AppViewModel extends ViewModel {
    DatabaseHandler dbHelper;
    Goal goal;
    Diary diary;
    public DatabaseHandler getDbHelper() {
        return dbHelper;
    }
    public Goal getGoal() {
        return goal;
    }
    public Diary getDiary(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.diary = this.dbHelper.getTodayDiary(LocalDate.now());
        }
        return this.diary;
    }
    public void setDbHelper(DatabaseHandler dbHelper) {
        this.dbHelper = dbHelper;
    }
    public void setDiary(Diary diary){
        this.diary = diary;
        this.dbHelper.insertDiary(diary);
    }
    public void setGoal(Goal goal) {
        this.goal = goal;
        this.dbHelper.insertGoal(goal);
    }

}
