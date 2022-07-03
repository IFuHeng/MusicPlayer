package com.example.musicplayer.ui.adpter;

import android.graphics.Color;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
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

import java.util.Locale;
import java.util.concurrent.Executor;

public class QueueItemAdapter extends ListAdapter<MediaSessionCompat.QueueItem, QueueItemAdapter.QueueItemCompatViewHolder> {
    private static final float[] TEMP_HSV = new float[]{0, 1, 1};
    private final OnItemClickListener listener;

    public QueueItemAdapter(Executor executor, OnItemClickListener listener) {
        super(new AsyncDifferConfig.Builder<>(new DiffUtil.ItemCallback<MediaSessionCompat.QueueItem>() {
            @Override
            public boolean areItemsTheSame(@NonNull MediaSessionCompat.QueueItem oldItem, @NonNull MediaSessionCompat.QueueItem newItem) {
                return TextUtils.equals(oldItem.toString(), newItem.toString());
            }

            @Override
            public boolean areContentsTheSame(@NonNull MediaSessionCompat.QueueItem oldItem, @NonNull MediaSessionCompat.QueueItem newItem) {
                return TextUtils.equals(oldItem.getDescription().toString(), newItem.getDescription().toString());
            }
        }).setBackgroundThreadExecutor(executor).build());
        this.listener = listener;
    }

    @NonNull
    @Override
    public QueueItemCompatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMusicBinding binding = ItemMusicBinding.inflate(LayoutInflater.from(parent.getContext()), null, false);
        return new QueueItemCompatViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull QueueItemCompatViewHolder holder, int position) {
        holder.setData(getItem(position));
    }

    class QueueItemCompatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @NonNull
        ItemMusicBinding binding;
        private MediaSessionCompat.QueueItem queueItem;

        public QueueItemCompatViewHolder(@NonNull ItemMusicBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.btnPlay.setVisibility(View.INVISIBLE);
            binding.root.setOnClickListener(this);
        }

        public void setData(MediaSessionCompat.QueueItem queueItem) {
            this.queueItem = queueItem;
            MediaDescriptionCompat description = queueItem.getDescription();
            this.binding.tvTitle.setText(description.getTitle());
            this.binding.tvIndex.setText(String.valueOf(getAdapterPosition() + 1));
            TEMP_HSV[0] = getAdapterPosition() + 180;
            this.binding.tvIndex.setTextColor(Color.HSVToColor(TEMP_HSV));
            this.binding.tvArtist.setText(description.getSubtitle());
            if (description.getExtras() != null) {
                this.binding.tvDuration.setText(getDurationStr((int) (description.getExtras().getLong(MediaMetadataCompat.METADATA_KEY_DURATION) / 1000)));
            } else {
                this.binding.tvDuration.setText(null);
            }
        }

        private String getDurationStr(int duration) {
            int hour, minutes, seconds;
            minutes = duration % 3600;
            minutes /= 60;
            seconds = duration % 60;
            if (duration >= 3600) {
                hour = duration / 3600;
                return String.format(Locale.getDefault(), "%02d:%02d:%02d", hour, minutes, seconds);
            } else {
                return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
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

