package com.example.musicplayer;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicplayer.entity.MusicBean;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel {
    private final MutableLiveData<List<MusicBean>> arrMusic;

    public MainViewModel() {
        arrMusic = new MutableLiveData<>(new ArrayList<>());
    }

    public MutableLiveData<List<MusicBean>> getArrMusic() {
        return arrMusic;
    }
}
