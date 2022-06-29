package com.example.musicplayer.ui.holder;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.example.musicplayer.R;
import com.example.musicplayer.databinding.ItemMusicBinding;
import com.example.musicplayer.ui.vhbean.MusicViewHolderBean;
import com.example.mylibrary.recycler.BaseViewHolder;

public class MusicViewHolder extends BaseViewHolder<MusicViewHolderBean> {
    private MusicViewHolderBean musicViewHolderBean;
    private ItemMusicBinding binding;

    public MusicViewHolder(@NonNull ItemMusicBinding binding, LifecycleOwner lifecycleOwner) {
        super(binding.root);
        this.binding = binding;
        binding.btnPlay.setOnClickListener(v -> {
            if (MusicViewHolder.this._onItemClickListener != null) {
                MusicViewHolder.this._onItemClickListener.onItemClick(v, getAdapterPosition(), musicViewHolderBean);
            }
        });
    }

    @Override
    public void onDataChanged(MusicViewHolderBean musicViewHolderBean) {
        this.musicViewHolderBean = musicViewHolderBean;
        this.binding.tvTitle.setText(musicViewHolderBean.getData().getTitle());
        this.binding.btnPlay.setImageResource(musicViewHolderBean.isPlaying()
                ? R.drawable.ic_pause
                : R.drawable.ic_play);
        this.binding.tvIndex.setText(String.valueOf(getAdapterPosition()));
        this.binding.tvIndex.setTextColor(Color.HSVToColor(new float[]{getAdapterPosition() + 180, 1, 1}));
        this.binding.tvArtist.setText(musicViewHolderBean.getData().getArtist());
        this.binding.tvDuration.setText(getDurationStr((int) (musicViewHolderBean.getData().getDuration() / 1000)));
    }

    private String getDurationStr(int duration) {
        int hour, minutes, seconds;
        minutes = duration % 3600;
        minutes /= 60;
        seconds = duration % 60;
        if (duration >= 3600) {
            hour = duration / 3600;
            return String.format("%02d:%02d:%02d", hour, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }
}
