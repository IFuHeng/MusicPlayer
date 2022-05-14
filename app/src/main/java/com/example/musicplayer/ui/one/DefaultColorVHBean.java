package com.example.musicplayer.ui.one;

import androidx.lifecycle.LiveData;

import com.example.musicplayer.ui.holder.GeneralViewHolderFactory;
import com.example.mylibrary.recycler.BaseViewHolderBean;

import java.util.Objects;

public class DefaultColorVHBean extends BaseViewHolderBean {
    final LiveData<Integer> color;

    public DefaultColorVHBean(LiveData<Integer> color) {
        super(GeneralViewHolderFactory.TYPE_SIMPLE_COLOR);
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultColorVHBean that = (DefaultColorVHBean) o;
        return color == that.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color);
    }
}
