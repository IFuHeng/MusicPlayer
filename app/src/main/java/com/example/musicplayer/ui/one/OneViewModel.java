package com.example.musicplayer.ui.one;

import android.graphics.Color;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OneViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<List<MutableLiveData<Integer>>> mColors;

    public OneViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is one fragment");
        mColors = new MutableLiveData(new ArrayList<Integer>());
        List<Integer> list = Arrays.asList(Color.RED, Color.BLUE, Color.GREEN, Color.DKGRAY, Color.WHITE, Color.YELLOW, Color.BLACK, Color.CYAN, Color.LTGRAY, Color.MAGENTA, Color.TRANSPARENT);
        for (Integer integer : list) {
            getColors().getValue().add(new MutableLiveData<>(integer));
        }
    }

    public LiveData<String> getText() {
        return mText;
    }

    public MutableLiveData<List<MutableLiveData<Integer>>> getColors() {
        return mColors;
    }
}