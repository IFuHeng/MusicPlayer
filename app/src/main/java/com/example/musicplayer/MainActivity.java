package com.example.musicplayer;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;

import com.example.musicplayer.database.MediaDataUtils;
import com.example.musicplayer.databinding.ActivityMainBinding;
import com.example.musicplayer.entity.MusicBean;
import com.example.musicplayer.jni.NativeObject;
import com.example.musicplayer.service.MusicService;
import com.example.musicplayer.ui.holder.GeneralViewHolderFactory;
import com.example.musicplayer.ui.vhbean.MusicViewHolderBean;
import com.example.mylibrary.recycler.BaseViewHolder;
import com.example.mylibrary.recycler.CustomPagerListAdapter;
import com.example.mylibrary.recycler.OnItemClickListener;
import com.example.mylibrary.recycler.OnItemLongClickListener;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
        , ServiceConnection
        , OnItemClickListener<MusicViewHolderBean>
        , OnItemLongClickListener<MusicViewHolderBean> {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding binding;
    private MainViewModel model;
    private MusicService.PlayerStateBind musicService;
    private ListAdapter<MusicViewHolderBean, BaseViewHolder> mAdapter;
    private Player.Listener mPlayerListener = new Player.Listener() {
        @Override
        public void onCues(List<Cue> cues) {
            Log.d(TAG, "==== onCues : " + cues);
            Player.Listener.super.onCues(cues);
        }

        @Override
        public void onMetadata(Metadata metadata) {
            Log.d(TAG, "==== onMetadata : " + metadata);
            Player.Listener.super.onMetadata(metadata);
        }

        @Override
        public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
            Log.d(TAG, "==== onMediaItemTransition : mediaItem = " + mediaItem + ", reason = " + reason);
            Player.Listener.super.onMediaItemTransition(mediaItem, reason);
        }

        @Override
        public void onMediaMetadataChanged(MediaMetadata mediaMetadata) {
            Log.d(TAG, "==== onMediaMetadataChanged : mediaMetadata = " + mediaMetadata);
            Player.Listener.super.onMediaMetadataChanged(mediaMetadata);
        }

        @Override
        public void onIsLoadingChanged(boolean isLoading) {
            Log.d(TAG, "==== onIsLoadingChanged : isLoading = " + isLoading);
            Player.Listener.super.onIsLoadingChanged(isLoading);
        }

        @Override
        public void onIsPlayingChanged(boolean isPlaying) {
            Log.d(TAG, "==== onIsPlayingChanged : isPlaying = " + isPlaying);
            Player.Listener.super.onIsPlayingChanged(isPlaying);
        }

        @Override
        public void onTimelineChanged(Timeline timeline, int reason) {
            Log.d(TAG, "==== onTimelineChanged : timeline = " + timeline + ",  reason = " + reason);
            Player.Listener.super.onTimelineChanged(timeline, reason);
        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            Log.d(TAG, "==== onTracksChanged : trackGroups = " + trackGroups + ",  trackSelections = " + trackSelections);
            Player.Listener.super.onTracksChanged(trackGroups, trackSelections);
        }

        @Override
        public void onStaticMetadataChanged(List<Metadata> metadataList) {
            Log.d(TAG, "==== onStaticMetadataChanged : metadataList = " + metadataList);
            Player.Listener.super.onStaticMetadataChanged(metadataList);
        }

        @Override
        public void onAvailableCommandsChanged(Player.Commands availableCommands) {
            Log.d(TAG, "==== onAvailableCommandsChanged : availableCommands = " + availableCommands);
            Player.Listener.super.onAvailableCommandsChanged(availableCommands);
        }

        @Override
        public void onPlaybackStateChanged(int state) {
            Log.d(TAG, "==== onPlaybackStateChanged : state = " + state);
            Player.Listener.super.onPlaybackStateChanged(state);
        }

        @Override
        public void onPlaybackSuppressionReasonChanged(int playbackSuppressionReason) {
            Log.d(TAG, "==== onPlaybackSuppressionReasonChanged : playbackSuppressionReason = " + playbackSuppressionReason);
            Player.Listener.super.onPlaybackSuppressionReasonChanged(playbackSuppressionReason);
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            Log.d(TAG, "==== onPlayerError : error = " + error);
            Player.Listener.super.onPlayerError(error);
        }

        @Override
        public void onPositionDiscontinuity(Player.PositionInfo oldPosition, Player.PositionInfo newPosition, int reason) {
            Log.d(TAG, "==== onPositionDiscontinuity : oldPosition = " + oldPosition + ", newPosition = " + newPosition + ", reason = " + reason);
            Player.Listener.super.onPositionDiscontinuity(oldPosition, newPosition, reason);
        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
            Log.d(TAG, "==== onShuffleModeEnabledChanged : shuffleModeEnabled = " + shuffleModeEnabled);
            Player.Listener.super.onShuffleModeEnabledChanged(shuffleModeEnabled);
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {
            Log.d(TAG, "==== onRepeatModeChanged : repeatMode = " + repeatMode);
            Player.Listener.super.onRepeatModeChanged(repeatMode);
        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            Log.d(TAG, "==== onPlaybackParametersChanged : playbackParameters = " + playbackParameters);
            Player.Listener.super.onPlaybackParametersChanged(playbackParameters);
        }

        @Override
        public void onPlayWhenReadyChanged(boolean playWhenReady, int reason) {
            Log.d(TAG, "==== onPlayWhenReadyChanged : playWhenReady = " + playWhenReady + ", reason = " + reason);
            Player.Listener.super.onPlayWhenReadyChanged(playWhenReady, reason);
        }

        @Override
        public void onEvents(Player player, Player.Events events) {
            Log.d(TAG, "==== onEvents : player = " + player + ", events = " + events);

            Player.Listener.super.onEvents(player, events);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.root);

        TextView tv = binding.sampleText;
        tv.setText(new NativeObject().stringFromJNI());

        initView();
        initModel();
    }

    private void initModel() {
        this.model = new ViewModelProvider(this).get(MainViewModel.class);

        this.model.getArrMusic().observe(this, musicBeans -> {
            //判断减少
            List<MusicViewHolderBean> list = new ArrayList<>();
            for (MusicBean musicBean : musicBeans) {
                list.add(new MusicViewHolderBean(musicBean));
            }
            mAdapter.submitList(list);
            binding.sampleText.setText("音乐（" + list.size() + ")");
        });
    }

    private void initView() {
        mAdapter = new CustomPagerListAdapter(new GeneralViewHolderFactory(this, this), this, this);
        binding.listMedia.setAdapter(mAdapter);
        DividerItemDecoration divider = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        divider.setDrawable(new ColorDrawable(Color.DKGRAY));
        binding.listMedia.addItemDecoration(divider);

//        binding.btnNext.setOnClickListener(this);
//        binding.btnPrevious.setOnClickListener(this);
//        binding.btnPlay.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<MusicBean> arr = MediaDataUtils.getMusic(this);
        Collections.sort(arr, (o1, o2) -> (int) (o2.getId() - o1.getId()));
        model.getArrMusic().postValue(arr);
        bindService(new Intent(this, MusicService.class), this, BIND_NOT_FOREGROUND);
    }


    @Override
    protected void onPause() {
        unbindService(this);
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btnNext:
//                Toast.makeText(this, "点击下一首", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.btnPrevious:
//                Toast.makeText(this, "点击上一首", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.btnPlay:
//                Toast.makeText(this, "点击播放", Toast.LENGTH_SHORT).show();
//                break;
            default:
                if (v.getTag() != null && v.getTag() instanceof Integer) {
                    int clickIndex = (int) v.getTag();

                }
        }
    }

    @Override
    public void onItemClick(View view, int position, MusicViewHolderBean bean) {
        Toast.makeText(this, "点击" + bean.getData().getTitle(), Toast.LENGTH_SHORT).show();
        if (musicService == null) {
            bindService(new Intent(this, MusicService.class), this, BIND_AUTO_CREATE);
            Toast.makeText(this, "服务尚未启动,请重试", Toast.LENGTH_SHORT).show();
        } else {

            List<String> list = new ArrayList<>();
            for (MusicBean musicBean : model.getArrMusic().getValue()) {
                list.add(musicBean.getUrl());
            }
            musicService.getPlayer().removeListener(mPlayerListener);
            musicService.getPlayer().addListener(mPlayerListener);
            musicService.play(list, position);
//            musicService.play(bean.getMusicBean().getUrl());
        }
    }

    @Override
    public void onItemLongClick(View view, int position, MusicViewHolderBean bean) {
        Toast.makeText(this, "长按点击" + bean.getData().getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        musicService = (MusicService.PlayerStateBind) service;
        musicService.getPlayer();
        Toast.makeText(this, "服务启动成功", Toast.LENGTH_SHORT).show();
//        binding.playerView.setPlayer(musicService.getPlayer());
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        if (musicService != null && musicService.getPlayer() != null) {
            musicService.getPlayer().removeListener(mPlayerListener);
        }
        musicService = null;
    }
}