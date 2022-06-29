package com.example.musicplayer;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import androidx.activity.ComponentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mylibrary.config.Constants;
import com.example.mylibrary.service.MediaService;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PlayViewModel extends ViewModel {
    private static final String TAG = PlayViewModel.class.getSimpleName();
    private MediaBrowserCompat mediaBrowserCompat;
    private MediaControllerCompat controller;

    private final MutableLiveData<List<MediaDescriptionCompat>> arrMusic;
    private MediaControllerCompat.Callback callback = new MediaControllerCompat.Callback() {
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
            Log.d(TAG, "====~onSessionEvent: event =  " + event + ", extras = " + extras.keySet());
            super.onSessionEvent(event, extras);
        }

        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat state) {
            Log.d(TAG, "====~onPlaybackStateChanged: state =  " + state.getState());
            super.onPlaybackStateChanged(state);
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            Log.d(TAG, "====~onMetadataChanged: metadata =  " + metadata.getDescription().getTitle());
            super.onMetadataChanged(metadata);
        }

        @Override
        public void onQueueChanged(List<MediaSessionCompat.QueueItem> queue) {
            Log.d(TAG, "====~onQueueChanged: queue =  " + queue);
            super.onQueueChanged(queue);
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
    }

    public void onConnectionFailed() {
        controller = null;
        mediaBrowserCompat = null;
    }

    public PlayViewModel() {
        arrMusic = new MutableLiveData<>(new ArrayList<>());
    }

    public MutableLiveData<List<MediaDescriptionCompat>> getArrMusic() {
        return arrMusic;
    }

    public void setQueue(List<MediaDescriptionCompat> list) {
        arrMusic.postValue(list);
        if (controller != null) {
            if (list != null && !list.isEmpty()) {
                String mediaId = list.get(0).getMediaId();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(Constants.KEY_QUEUE, new ArrayList<>(list));
//                controller.getTransportControls().prepareFromMediaId(mediaId, bundle);
                controller.getTransportControls().sendCustomAction(Constants.CMD_SET_QUEUE, bundle);
            } else {
                controller.getTransportControls().sendCustomAction(Constants.CMD_SET_QUEUE, null);
            }
        }
    }

    public void play() {
        if (controller != null) {
            controller.getTransportControls().play();
        }
    }

    public void pause() {
        if (controller != null) {
            controller.getTransportControls().pause();
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
}
