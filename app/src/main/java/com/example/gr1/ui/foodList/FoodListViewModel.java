package com.example.gr1.ui.foodList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FoodListViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public FoodListViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is foodList fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}