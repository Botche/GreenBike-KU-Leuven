package com.example.greenbike.ui.bikes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BikesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BikesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is bikes fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}