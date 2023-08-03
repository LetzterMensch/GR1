package com.example.gr1;

import static java.security.AccessController.getContext;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gr1.model.Goal;
import com.example.gr1.ui.helper.DatabaseHandler;

import java.io.Closeable;

public class AppViewModel extends ViewModel {
//    private final _database = MutableLiveData<DatabaseHandler>()

    DatabaseHandler dbHelper;
    Goal goal;
    public DatabaseHandler getDbHelper() {
        return dbHelper;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setDbHelper(DatabaseHandler dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
        this.dbHelper.insertGoal(goal);
    }
}
