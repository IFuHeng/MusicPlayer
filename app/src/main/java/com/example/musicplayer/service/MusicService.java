package com.example.musicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSource;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class MusicService extends Service {

    private static volatile PlayerStateBind bind;

    @Override
    public IBinder onBind(Intent intent) {
        if (bind == null) {
            synchronized (this) {
                if (bind == null) {
                    bind = new PlayerStateBind();
                }
            }
        }
        Log.d(getClass().getSimpleName(), "====~ onBind(): " + bind);
        return bind;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(getClass().getSimpleName(), "====~ onCreate(): " + this);
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(getClass().getSimpleName(), "====~ onRebind(): " + bind);
        super.onRebind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(getClass().getSimpleName(), "====~ onStartCommand(): " + bind);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(getClass().getSimpleName(), "====~ onDestroy(): " + this);
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(getClass().getSimpleName(), "====~ onUnbind(): " + bind);
        return super.onUnbind(intent);
    }

    public class PlayerStateBind extends Binder {
        private ExoPlayer player;
        private ReentrantLock mLock = new ReentrantLock();

        public void play(@NonNull @NotNull String path) {
            if (path == null || path.isEmpty()) {
                Toast.makeText(getApplicationContext(), "empty media path", Toast.LENGTH_SHORT).show();
                return;
            }
            synchronized (mLock) {
                if (player == null) {
                    player = new SimpleExoPlayer.Builder(MusicService.this).build();
                }
                if (player.getCurrentMediaItem() == null || !player.getCurrentMediaItem().mediaId.equals(path)) {
                    player.setMediaItem(MediaItem.fromUri(path));
                }
                player.prepare();
                player.play();
            }
        }

        public void play(@NonNull @NotNull List<String> paths, int index) {
            if (paths.isEmpty()) {
                Toast.makeText(getApplicationContext(), "empty media path", Toast.LENGTH_SHORT).show();
                return;
            }
            synchronized (mLock) {
                if (player == null) {
                    player = new SimpleExoPlayer.Builder(MusicService.this).build();
                }
                List<MediaSource> playlist = new ArrayList<>();
                DefaultMediaSourceFactory factory = new DefaultMediaSourceFactory(MusicService.this);
                for (String path : paths) {
                    playlist.add(factory.createMediaSource(MediaItem.fromUri(path)));
                }
                player.setMediaSources(playlist, index, 0);
                player.prepare();
                player.play();
            }
        }

        public ExoPlayer getPlayer() {
            synchronized (mLock) {
                if (player == null) {
                    player = new SimpleExoPlayer.Builder(MusicService.this).build();
                }
                return player;
            }
        }

        public void pause() {
            synchronized (mLock) {
                if (player != null && player.isPlaying()) {
                    player.pause();
                }
            }
        }

        public void stop() {
            synchronized (mLock) {
                if (player != null && player.isPlaying()) {
                    player.stop();
                }
            }
        }

        public void release() {
            synchronized (mLock) {
                if (player != null) {
                    player.release();
                }
            }
        }
    }
}