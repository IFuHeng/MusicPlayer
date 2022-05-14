package com.example.musicplayer.ui.two;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TwoViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public TwoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is two fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}