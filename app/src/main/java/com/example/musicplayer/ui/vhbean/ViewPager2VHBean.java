package com.example.musicplayer.ui.vhbean;

import androidx.lifecycle.MutableLiveData;

import com.example.musicplayer.entity.MusicBean;
import com.example.musicplayer.ui.holder.GeneralViewHolderFactory;
import com.example.mylibrary.recycler.BaseViewHolderBean;

import java.util.List;

public class ViewPager2VHBean extends BaseViewHolderBean {

    private MutableLiveData<List<? extends BaseViewHolderBean>> vhBean;

    public ViewPager2VHBean(MusicBean musicBean) {
        super(GeneralViewHolderFactory.TYPE_VIEW_PAGER2);
        this.vhBean = new MutableLiveData(musicBean);
    }

    public MutableLiveData<List<? extends BaseViewHolderBean>> getVhBean() {
        return vhBean;
    }
}
