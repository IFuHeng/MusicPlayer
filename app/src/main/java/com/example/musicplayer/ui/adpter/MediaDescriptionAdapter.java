package com.example.musicplayer.ui.adpter;

import android.graphics.Color;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.databinding.ItemMusicBinding;

import java.util.concurrent.Executor;

public class MediaDescriptionAdapter extends ListAdapter<MediaDescriptionCompat, MediaDescriptionAdapter.MediaDescriptionCompatViewHolder> {
    private static final float[] TEMP_HSV = new float[]{0, 1, 1};
    private final OnItemClickListener listener;

    public MediaDescriptionAdapter(Executor executor, OnItemClickListener listener) {
        super(new AsyncDifferConfig.Builder<>(new DiffUtil.ItemCallback<MediaDescriptionCompat>() {
            @Override
            public boolean areItemsTheSame(@NonNull MediaDescriptionCompat oldItem, @NonNull MediaDescriptionCompat newItem) {
                return TextUtils.equals(oldItem.toString(), newItem.toString());
            }

            @Override
            public boolean areContentsTheSame(@NonNull MediaDescriptionCompat oldItem, @NonNull MediaDescriptionCompat newItem) {
                return TextUtils.equals(oldItem.toString(), newItem.toString());
            }
        }).setBackgroundThreadExecutor(executor).build());
        this.listener = listener;
    }

    @NonNull
    @Override
    public MediaDescriptionCompatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMusicBinding binding = ItemMusicBinding.inflate(LayoutInflater.from(parent.getContext()), null, false);
        return new MediaDescriptionCompatViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaDescriptionCompatViewHolder holder, int position) {
        holder.setData(getItem(position));
    }

    class MediaDescriptionCompatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @NonNull
        ItemMusicBinding binding;
        private MediaDescriptionCompat description;

        public MediaDescriptionCompatViewHolder(@NonNull ItemMusicBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.btnPlay.setVisibility(View.INVISIBLE);
            binding.root.setOnClickListener(this);
        }

        public void setData(MediaDescriptionCompat description) {
            this.description = description;
            this.binding.tvTitle.setText(description.getTitle());
            this.binding.tvIndex.setText(String.valueOf(getAdapterPosition() + 1));
            TEMP_HSV[0] = getAdapterPosition() + 180;
            this.binding.tvIndex.setTextColor(Color.HSVToColor(TEMP_HSV));
            this.binding.tvArtist.setText(description.getSubtitle());
            this.binding.tvDuration.setText(getDurationStr((int) (description.getExtras().getLong(MediaMetadataCompat.METADATA_KEY_DURATION) / 1000)));
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

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onClick(getAdapterPosition());
            }
        }
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }
}

