package com.example.musicplayer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.musicplayer.databinding.ActivityPlayerBinding;
import com.example.musicplayer.ui.adpter.QueueItemAdapter;

import java.util.Locale;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = PlayActivity.class.getSimpleName();

    private ActivityPlayerBinding binding;
    private PlayViewModel model;
    private QueueItemAdapter mAdapter;
    private Handler handler;

    private final MediaBrowserCompat.ConnectionCallback connectionCallback = new MediaBrowserCompat.ConnectionCallback() {
        @Override
        public void onConnected() {
            Log.d(TAG, "====~ onConnected");
            model.onConnected(PlayActivity.this, getHandler());
        }

        @Override
        public void onConnectionFailed() {
            Log.d(TAG, "====~ onConnectionFailed");
            model.onConnectionFailed();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initModel();
        doConnect();
        initData();
    }

    private void initData() {
        if (model.arrMusicLiveData.getValue() != null && !model.arrMusicLiveData.getValue().isEmpty()) {
            mAdapter.submitList(model.arrMusicLiveData.getValue());
        }
        model.arrMusicLiveData.observe(this, queueItems -> mAdapter.submitList(queueItems));

        if (model.nowPlayingLiveData.getValue() != null) {
            binding.sampleText.setText(model.nowPlayingLiveData.getValue().getDescription().getTitle());
        }
        model.nowPlayingLiveData.observe(this, nowPlaying -> binding.sampleText.setText(nowPlaying.getDescription().getTitle()));

        model.durationLiveData.observe(this, duration -> {
            binding.tvDuration.setText(String.format(Locale.getDefault(), App.FORMAT_TIME, duration / 1000 / 60, (duration / 1000) % 60));
            binding.seekbar.setMax(Math.toIntExact(duration));
        });
        model.positionLiveData.observe(this, position -> {
            binding.tvPosition.setText(String.format(Locale.getDefault(), App.FORMAT_TIME, position / 1000 / 60, (position / 1000) % 60));
            binding.seekbar.setProgress(Math.toIntExact(position));
        });
        model.positionBufferedLiveData.observe(this, position -> {
            binding.seekbar.setSecondaryProgress(Math.toIntExact(position));
        });

        model.isPlayingLiveData.observe(this, isPlaying -> binding.btnPlay.setImageResource(isPlaying ? R.drawable.ic_pause : R.drawable.ic_play));
    }

    private void doConnect() {
        model.connect2MediaService(this, connectionCallback);
    }

    private void initModel() {
        this.model = new ViewModelProvider(this).get(PlayViewModel.class);
    }

    private void initView() {
        binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.root);
        mAdapter = new QueueItemAdapter(App.getInstance().sIOExecutor, position -> model.skipToQueueItem(position));
        binding.listMedia.setAdapter(mAdapter);
        DividerItemDecoration divider = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        divider.setDrawable(new ColorDrawable(Color.DKGRAY));
        binding.listMedia.addItemDecoration(divider);

        binding.btnPlay.setOnClickListener(this);
        binding.btnPrevious.setOnClickListener(this);
        binding.btnNext.setOnClickListener(this);

        binding.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                model.seekTo(seekBar.getProgress());
            }
        });
    }

    private Handler getHandler() {
        if (handler == null) {
            HandlerThread thread = new HandlerThread(getClass().getCanonicalName());
            thread.start();
            handler = new Handler(thread.getLooper());
        }
        return handler;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNext:
                model.skipToNext();
                break;
            case R.id.btnPrevious:
                model.skipToPrevious();
                break;
            case R.id.btnPlay:
                model.playOrPause();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle(R.string.exit).setMessage(R.string.ask_when_exit)
                .setPositiveButton(R.string.exit_and_close_music, (dialog, which) -> android.os.Process.killProcess(android.os.Process.myPid()))
                .setNegativeButton(R.string.only_exit, (dialog, which) -> PlayActivity.super.onBackPressed())
                .setNeutralButton(R.string.cancel, (dialog, which) -> dialog.cancel())
                .show();
    }
}