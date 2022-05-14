package com.example.musicplayer.ui.vhbean;

import com.example.musicplayer.entity.MusicBean;
import com.example.musicplayer.ui.holder.GeneralViewHolderFactory;
import com.example.mylibrary.recycler.BaseViewHolderBean;

public class MusicViewHolderBean extends BaseViewHolderBean {

    private MusicBean musicBean;

    private boolean isPlaying;

    public MusicViewHolderBean(MusicBean musicBean) {
        super(GeneralViewHolderFactory.TYPE_MUSIC_ITEM);
        this.musicBean = musicBean;
    }

    public MusicBean getMusicBean() {
        return musicBean;
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}
