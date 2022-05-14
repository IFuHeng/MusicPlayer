package com.example.musicplayer.ui.music;

import android.graphics.Color;

import androidx.annotation.NonNull;

import com.example.musicplayer.R;
import com.example.musicplayer.databinding.ItemMusicBinding;
import com.example.mylibrary.recycler.BaseViewHolder;
import com.example.mylibrary.recycler.OnItemClickListener;

public class MusicViewHolder extends BaseViewHolder<MusicViewHolderBean> {
    private MusicViewHolderBean musicViewHolderBean;
    private ItemMusicBinding binding;

    public MusicViewHolder(@NonNull ItemMusicBinding binding) {
        super(binding.root);
        this.binding = binding;
        binding.btnPlay.setOnClickListener(v -> {
            if (MusicViewHolder.this.onItemClickListener != null) {
                MusicViewHolder.this.onItemClickListener.onItemClick(v, getAdapterPosition(), musicViewHolderBean);
            }
        });
    }

    @Override
    public void onDataChanged(MusicViewHolderBean musicViewHolderBean) {
        this.musicViewHolderBean = musicViewHolderBean;
        this.binding.tvTitle.setText(musicViewHolderBean.getMusicBean().getTitle());
        this.binding.btnPlay.setImageResource(musicViewHolderBean.isPlaying()
                ? R.drawable.ic_pause
                : R.drawable.ic_play);
        this.binding.tvIndex.setText(String.valueOf(getAdapterPosition()));
        this.binding.tvIndex.setTextColor(Color.HSVToColor(new float[]{getAdapterPosition() + 180, 1, 1}));
        this.binding.tvArtist.setText(musicViewHolderBean.getMusicBean().getArtist());
        this.binding.tvDuration.setText(getDurationStr((int) (musicViewHolderBean.getMusicBean().getDuration() / 1000)));
    }

    private String getDurationStr(int duration) {
        int hour, minutes, seconds;
        minutes = (int) (duration % 3600);
        minutes /= 60;
        seconds = (int) (duration % 60);
        if (duration >= 3600) {
            hour = (int) (duration / 3600);
            return String.format("%02d:%02d:%02d", hour, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
