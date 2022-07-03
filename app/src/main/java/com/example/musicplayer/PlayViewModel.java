package com.example.musicplayer;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import androidx.activity.ComponentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicplayer.database.MediaDataUtils;
import com.example.mylibrary.config.Constants;
import com.example.mylibrary.service.MediaService;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PlayViewModel extends ViewModel {
    private static final String TAG = PlayViewModel.class.getSimpleName();
    private MediaBrowserCompat mediaBrowserCompat;
    private MediaControllerCompat controller;

    /**
     * 播放列表
     */
    public final MutableLiveData<List<MediaSessionCompat.QueueItem>> arrMusicLiveData = new MutableLiveData<>(new ArrayList<>());
    /**
     * 正在播放
     */
    public final MutableLiveData<MediaMetadataCompat> nowPlayingLiveData = new MutableLiveData<>();
    public final MutableLiveData<Long> durationLiveData = new MutableLiveData(0L);
    public final MutableLiveData<Long> positionLiveData = new MutableLiveData(0L);
    public final MutableLiveData<Long> positionBufferedLiveData = new MutableLiveData(0L);
    public final MutableLiveData<Boolean> isPlayingLiveData = new MutableLiveData(false);


    private final MediaControllerCompat.Callback callback = new MediaControllerCompat.Callback() {
        @Override
        public void onSessionReady() {
            Log.d(TAG, "====~onSessionReady");
            super.onSessionReady();
        }

        @Override
        public void onSessionDestroyed() {
            Log.d(TAG, "====~onSessionDestroyed");
            super.onSessionDestroyed();
        }

        @Override
        public void onSessionEvent(String event, Bundle extras) {
            StringBuilder sb = new StringBuilder("{");
            for (String s : extras.keySet()) {
                sb.append(s).append(':').append(extras.get(s)).append(',').append(' ');
            }
            sb.delete(sb.length() - 2, sb.length());
            sb.append('}');
            Log.d(TAG, "====~onSessionEvent: event =  " + event + ", extras = " + sb);
            super.onSessionEvent(event, extras);
        }

        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat state) {
            Log.d(TAG, "====~onPlaybackStateChanged: state =  " + state.getState());
            if (state.getState() == PlaybackStateCompat.STATE_PLAYING) {
                positionLiveData.postValue(state.getPosition());
                isPlayingLiveData.postValue(true);
            } else if (state.getState() == PlaybackStateCompat.STATE_BUFFERING) {
                positionBufferedLiveData.postValue(state.getBufferedPosition());
            } else if (state.getState() == PlaybackStateCompat.STATE_PAUSED) {
                isPlayingLiveData.postValue(false);
            }
            super.onPlaybackStateChanged(state);
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            Log.d(TAG, "====~onMetadataChanged: metadata =  " + metadata.getDescription().getTitle());
            nowPlayingLiveData.postValue(metadata);
            isPlayingLiveData.postValue(false);
            durationLiveData.postValue(metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION));
            super.onMetadataChanged(metadata);
        }

        @Override
        public void onQueueChanged(List<MediaSessionCompat.QueueItem> queue) {
            Log.d(TAG, "====~onQueueChanged: queue =  " + queue);
            super.onQueueChanged(queue);

            arrMusicLiveData.postValue(queue);
        }

        @Override
        public void onQueueTitleChanged(CharSequence title) {
            Log.d(TAG, "====~onQueueTitleChanged: title =  " + title);
            super.onQueueTitleChanged(title);
        }

        @Override
        public void onExtrasChanged(Bundle extras) {
            Log.d(TAG, "====~onExtrasChanged: extras =  " + extras);
            super.onExtrasChanged(extras);
        }

        @Override
        public void onAudioInfoChanged(MediaControllerCompat.PlaybackInfo info) {
            Log.d(TAG, "====~onAudioInfoChanged: info =  " + info);
            super.onAudioInfoChanged(info);
        }

        @Override
        public void onCaptioningEnabledChanged(boolean enabled) {
            Log.d(TAG, "====~onCaptioningEnabledChanged: enabled =  " + enabled);
            super.onCaptioningEnabledChanged(enabled);
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {
            Log.d(TAG, "====~onRepeatModeChanged: repeatMode =  " + repeatMode);
            super.onRepeatModeChanged(repeatMode);
        }

        @Override
        public void onShuffleModeChanged(int shuffleMode) {
            Log.d(TAG, "====~onShuffleModeChanged: shuffleMode =  " + shuffleMode);
            super.onShuffleModeChanged(shuffleMode);
        }

        @Override
        public void binderDied() {
            Log.d(TAG, "====~binderDied");
            super.binderDied();
        }
    };

    public void connect2MediaService(ComponentActivity activity, MediaBrowserCompat.ConnectionCallback connectionCallback) {
        mediaBrowserCompat = new MediaBrowserCompat(activity, new ComponentName(activity, MediaService.class), connectionCallback, new Bundle());
        mediaBrowserCompat.connect();
    }

    public void onConnected(@NotNull Context context, Handler handler) {
        controller = new MediaControllerCompat(context, mediaBrowserCompat.getSessionToken());
        controller.registerCallback(callback, handler);
        List<MediaSessionCompat.QueueItem> queue = controller.getQueue();
        arrMusicLiveData.postValue(controller.getQueue());
        // 如果当前播放列表没有内容，读取并生成播放列表
        if (queue == null || queue.isEmpty()) {
            setQueue(MediaDataUtils.getMediaMetadataFromSysDb(context));
        }
    }

    public void onConnectionFailed() {
        controller = null;
        mediaBrowserCompat = null;
    }

    public PlayViewModel() {
    }

    private void setQueue(List<MediaMetadataCompat> list) {
        if (controller != null) {
            if (list != null && !list.isEmpty()) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(Constants.KEY_QUEUE, new ArrayList<>(list));
                controller.getTransportControls().sendCustomAction(Constants.CMD_SET_QUEUE, bundle);
            } else {
                controller.getTransportControls().sendCustomAction(Constants.CMD_SET_QUEUE, null);
            }
        }
    }

    public void playOrPause() {
        if (controller != null) {
            if (Boolean.TRUE.equals(isPlayingLiveData.getValue())) {
                controller.getTransportControls().pause();
            } else {
                controller.getTransportControls().play();
            }
        }
    }

    public void skipToPrevious() {
        if (controller != null) {
            controller.getTransportControls().skipToPrevious();
        }
    }

    public void skipToNext() {
        if (controller != null) {
            controller.getTransportControls().skipToNext();
        }
    }

    public void skipToQueueItem(long id) {
        if (controller != null) {
            controller.getTransportControls().skipToQueueItem(id);
        }
    }

    /**
     * 跳转当前歌曲进度到指定进度
     *
     * @param position 指定进度
     */
    public void seekTo(long position) {
        if (controller != null && nowPlayingLiveData.getValue() != null) {
            controller.getTransportControls().seekTo(position);
        }
    }
}
