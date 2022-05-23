package com.example.greenbike.ui.brands;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BrandsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BrandsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is brands fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}