package com.example.musicplayer.ui.vhbean;

import androidx.lifecycle.MutableLiveData;

import com.example.musicplayer.ui.holder.GeneralViewHolderFactory;
import com.example.musicplayer.entity.MusicBean;
import com.example.mylibrary.recycler.BaseViewHolderBean;

import java.util.List;

public class ViewPagerVHBean extends BaseViewHolderBean {

    private MutableLiveData<List<? extends BaseViewHolderBean>> vhBean;

    public ViewPagerVHBean(MusicBean musicBean) {
        super(GeneralViewHolderFactory.TYPE_VIEW_PAGER);
        this.vhBean = new MutableLiveData(musicBean);
    }

    public MutableLiveData<List<? extends BaseViewHolderBean>> getVhBean() {
        return vhBean;
    }
}
