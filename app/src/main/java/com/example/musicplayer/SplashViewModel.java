package com.example.musicplayer;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SplashViewModel extends ViewModel {
    private MutableLiveData<Integer> remaining = new MutableLiveData<>(100);

    public MutableLiveData<Integer> getRemaining() {
        return remaining;
    }
}
