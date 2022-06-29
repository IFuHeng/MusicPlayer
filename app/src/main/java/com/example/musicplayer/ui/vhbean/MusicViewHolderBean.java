package com.example.musicplayer.ui.vhbean;

import com.example.musicplayer.entity.MusicBean;
import com.example.musicplayer.ui.holder.GeneralViewHolderFactory;
import com.example.mylibrary.recycler.BaseViewHolderBean;

@Deprecated
public class MusicViewHolderBean extends BaseViewHolderBean<MusicBean> {

    private boolean isPlaying;

    public MusicViewHolderBean(MusicBean musicBean) {
        super(GeneralViewHolderFactory.TYPE_MUSIC_ITEM,musicBean);
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}
