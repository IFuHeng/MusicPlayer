package com.example.mylibrary.service;


import android.content.Context;
import android.media.MediaFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ResultReceiver;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media.MediaBrowserServiceCompat;

import com.example.mylibrary.config.Constants;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.text.Cue;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MediaService extends MediaBrowserServiceCompat {
    private static final String TAG = MediaService.class.getSimpleName();
    private static final int WHAT_REFRESH_POSITION = 0;
    private static final String TAG_PLAYER_LISTENER = "Player.Listener";
    private static final String TAG_SESSION_LISTENER = "MediaSession.Callback";
    private static final long INTERVAL_REFRESH = 1000L;
    private static PlayerSessionImpl playerSession;

    @Override
    public void onCreate() {
        super.onCreate();
        if (playerSession == null) {
            playerSession = new PlayerSessionImpl(this);
        }
        setSessionToken(playerSession.getSession().getSessionToken());
    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
        return new MediaBrowserServiceCompat.BrowserRoot(clientPackageName, rootHints);
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {
        result.sendResult(new ArrayList());
    }

    @Override
    public void onCustomAction(@NonNull String action, Bundle extras, @NonNull Result<Bundle> result) {
        Log.d(TAG, "====~onCustomAction: action =  " + action + ", extras =  " + extras + ", result =  " + result);
        super.onCustomAction(action, extras, result);
    }

    private static class PlayerSessionImpl extends MediaSessionCompat.Callback {
        private MediaSessionCompat session;
        private ExoPlayer player;

        /**
         * 播放列表数据
         */
        private final List<MediaMetadataCompat> mediaMetadataList = new ArrayList<>();
        private final DefaultMediaSourceFactory factory;

        public PlayerSessionImpl(Context context) {
            factory = new DefaultMediaSourceFactory(context);
            initSession(context);
            initPlayer(context);
        }

        private Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case WHAT_REFRESH_POSITION:
                        PlaybackStateCompat.Builder builder = new PlaybackStateCompat.Builder();
                        builder.setState(PlaybackStateCompat.STATE_PLAYING, player.getContentPosition(), 1);
                        session.setPlaybackState(builder.build());
                        if (player.isPlaying()) {
                            sendEmptyMessageDelayed(WHAT_REFRESH_POSITION, INTERVAL_REFRESH);
                        }
                        break;
                }
            }
        };


        private void initSession(Context context) {
            if (session == null) {
                session = new MediaSessionCompat(context, "MyMusicService");
                session.setCallback(this);
                session.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
            }
        }

        MediaSessionCompat getSession() {
            return session;
        }

        private void initPlayer(Context context) {
            if (player != null) {
                return;
            }
            player = new SimpleExoPlayer.Builder(context).build();
            player.setPlayWhenReady(true);
            player.addListener(new Player.Listener() {
                @Override
                public void onCues(List<Cue> cues) {
                    Player.Listener.super.onCues(cues);
                    Log.d(TAG_PLAYER_LISTENER, "====~onCues : " + cues);
                }

                @Override
                public void onMetadata(Metadata metadata) {
                    Player.Listener.super.onMetadata(metadata);
                    Log.d(TAG_PLAYER_LISTENER, "====~onMetadata : " + metadata);
                }

                @Override
                public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
                    Player.Listener.super.onMediaItemTransition(mediaItem, reason);
                    Log.d(TAG_PLAYER_LISTENER, "====~onMediaItemTransition : mediaItem = " + mediaItem.mediaId + " ,  reason = " + MediaItemTransitionReason.values()[reason].name());
                    for (MediaMetadataCompat metadataCompat : mediaMetadataList) {
                        if (metadataCompat.getDescription().getMediaId().equals(mediaItem.mediaId)) {
                            session.setMetadata(metadataCompat);
                        }
                    }
                }

                @Override
                public void onTimelineChanged(@NonNull Timeline timeline, int reason) {
                    Player.Listener.super.onTimelineChanged(timeline, reason);
                    Log.d(TAG_PLAYER_LISTENER, "====~onTimelineChanged : timeline = " + timeline + " ,  reason = " + TimelineChangeReason.values()[reason].name());
                }

                @Override
                public void onMediaMetadataChanged(@NonNull MediaMetadata mediaMetadata) {
                    Player.Listener.super.onMediaMetadataChanged(mediaMetadata);
                    Log.d(TAG_PLAYER_LISTENER, "====~onMediaMetadataChanged : mediaMetadata = " + mediaMetadata.title);
                    session.setMetadata(mediaMetadataList.get(player.getCurrentWindowIndex()));
                }

                @Override
                public void onPositionDiscontinuity(Player.PositionInfo oldPosition, Player.PositionInfo newPosition, int reason) {
                    Player.Listener.super.onPositionDiscontinuity(oldPosition, newPosition, reason);
                    String strOld = String.format(Locale.getDefault(), "[positionMs=%d,contentPositionMs=%d, periodIndex=%d, windowIndex=%d, adGroupIndex=%d, adIndexInAdGroup=%d]"
                            , oldPosition.positionMs, oldPosition.contentPositionMs
                            , oldPosition.periodIndex, oldPosition.windowIndex
                            , oldPosition.adGroupIndex, oldPosition.adIndexInAdGroup);
                    String strNew = String.format(Locale.getDefault(), "[positionMs=%d,contentPositionMs=%d, periodIndex=%d, windowIndex=%d, adGroupIndex=%d, adIndexInAdGroup=%d]"
                            , newPosition.positionMs, newPosition.contentPositionMs
                            , newPosition.periodIndex, newPosition.windowIndex
                            , newPosition.adGroupIndex, newPosition.adIndexInAdGroup);
                    Log.d(TAG_PLAYER_LISTENER, "====~onPositionDiscontinuity : oldPosition = " + strOld + " ,  newPosition = " + strNew + " ,  reason = " + PlayerReason.values()[reason].name());
                }

                @Override
                public void onEvents(@NonNull Player player, @NonNull Player.Events events) {
                    Player.Listener.super.onEvents(player, events);
                    List<String> event = new ArrayList<>();
                    for (int i = 0; i < events.size(); i++) {
                        event.add(PlayerEvent.values()[events.get(i)].name());
                    }
                    Log.d(TAG_PLAYER_LISTENER, "====~onEvents : player = " + player + " ,  events = " + event);

                    PlaybackStateCompat.Builder builder = new PlaybackStateCompat.Builder();
                    int state = 0;
                    if (events.contains(Player.EVENT_IS_PLAYING_CHANGED)) {
                        if (player.isPlaying()) {
                            state = PlaybackStateCompat.STATE_PLAYING;
                            builder.setActions(PlaybackStateCompat.ACTION_PLAY);
                            handler.sendEmptyMessage(WHAT_REFRESH_POSITION);
                        } else {
                            state = PlaybackStateCompat.STATE_PAUSED;
                            builder.setActions(PlaybackStateCompat.ACTION_PAUSE);
                            handler.removeMessages(WHAT_REFRESH_POSITION);
                        }
                    } else if (events.contains(Player.EVENT_IS_LOADING_CHANGED)) {
                        state = PlaybackStateCompat.STATE_BUFFERING;
                    } else if (events.contains(Player.EVENT_PLAYER_ERROR)) {
                        Bundle bundle = new Bundle();
                        player.getCurrentWindowIndex();
                        if (player.getCurrentMediaItem() != null) {
                            MediaItem mediaItem = player.getCurrentMediaItem();
                            bundle.putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, mediaItem.mediaId);
                            String title = mediaItem.mediaMetadata.title != null ? mediaItem.mediaMetadata.title.toString() : "";
                            bundle.putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, title);
                        }
                        session.sendSessionEvent(Constants.EVENT_ERROR, bundle);
                        session.getController().getTransportControls().skipToNext();
                        state = PlaybackStateCompat.STATE_SKIPPING_TO_NEXT;
                        builder.setActions(PlaybackStateCompat.ACTION_STOP);
                    }
                    builder.setState(state, player.getContentPosition(), 1);
                    builder.setBufferedPosition(player.getBufferedPosition());

                    session.setPlaybackState(builder.build());
                }

                @Override
                public void onPlaybackStateChanged(int state) {
                    Player.Listener.super.onPlaybackStateChanged(state);
                    Log.d(TAG_PLAYER_LISTENER, "====~onPlaybackStateChanged : state = " + PlaybackReason.values()[state].name());
                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {
                    Player.Listener.super.onPlayerError(error);
                    Log.d(TAG_PLAYER_LISTENER, "====~onPlayerError : error = " + error);
                }

                @Override
                public void onStaticMetadataChanged(List<Metadata> metadataList) {
                    Player.Listener.super.onStaticMetadataChanged(metadataList);
                    Log.d(TAG_PLAYER_LISTENER, "====~onStaticMetadataChanged : metadataList = " + metadataList);
                }

            });
        }

        @Override
        public void onPlay() {
            player.play();
            Log.d(TAG_SESSION_LISTENER, "====~onPlay");
        }

        @Override
        public void onSkipToQueueItem(long id) {
            Log.d(TAG_SESSION_LISTENER, "====~onSkipToQueueItem : id = " + id);
            if (id == player.getCurrentPeriodIndex()) {
                return;
            }
            player.seekToDefaultPosition((int) id);
            player.prepare();
        }

        @Override
        public void onSeekTo(long pos) {
            Log.d(TAG_SESSION_LISTENER, "====~onSeekTo : id = " + pos);
            player.seekTo(pos);
        }

        @Override
        public void onPlayFromMediaId(String mediaId, Bundle extras) {
            Log.d(TAG_SESSION_LISTENER, "====~onPlayFromMediaId : mediaId = " + mediaId + ", mediaId = " + mediaId);
            int count = player.getMediaItemCount();
            for (int i = 0; i < count; i++) {
                MediaItem mediaItem = player.getMediaItemAt(i);
                if (mediaItem.mediaId.equals(mediaId)) {
                    player.setMediaItem(mediaItem);
                    player.play();
                    break;
                }
            }
        }

        @Override
        public void onPause() {
            Log.d(TAG_SESSION_LISTENER, "====~onPause ");
            player.pause();
        }

        @Override
        public void onStop() {
            Log.d(TAG_SESSION_LISTENER, "====~onStop ");
            player.stop();
        }

        @Override
        public void onSkipToNext() {
            Log.d(TAG_SESSION_LISTENER, "====~onSkipToNext ");
            super.onSkipToNext();
            player.next();
        }

        @Override
        public void onSkipToPrevious() {
            Log.d(TAG_SESSION_LISTENER, "====~onSkipToPrevious ");
            super.onSkipToPrevious();
            player.next();
        }

        @Override
        public void onCustomAction(String action, Bundle extras) {
            Log.d(TAG_SESSION_LISTENER, "====~onCustomAction: action = " + action + ", extras = " + extras);
            switch (action) {
                case Constants.CMD_SET_QUEUE: {
                    List<MediaMetadataCompat> list = extras.getParcelableArrayList(Constants.KEY_QUEUE);
                    List<MediaSource> playlist = new ArrayList<>();
                    List<MediaSessionCompat.QueueItem> queue = new ArrayList<>();
                    if (list != null) {
                        for (int i = 0; i < list.size(); i++) {
                            MediaMetadataCompat metadata = list.get(i);
                            MediaDescriptionCompat description = MetadataUtils.turnMetadata2Description(metadata);
                            Uri uri = description.getMediaUri();
                            Log.d(TAG_SESSION_LISTENER, "====~onCustomAction: index = " + i + ", uri = " + uri);
                            MediaItem mediaItem = MediaItem.fromUri(uri);
                            queue.add(new MediaSessionCompat.QueueItem(description, i));
                            playlist.add(factory.createMediaSource(mediaItem));
                        }
                        // 缓存播放列表
                        mediaMetadataList.clear();
                        mediaMetadataList.addAll(list);
                    }
                    // 设置当前服务中的播放列表
                    session.setQueue(queue);
                    // 设置播放器播放队列
                    player.setMediaSources(playlist, true);
                    player.prepare();
                    break;
                }
            }
            super.onCustomAction(action, extras);
        }

        @Override
        public void onPlayFromSearch(String query, Bundle extras) {
            Log.d(TAG_SESSION_LISTENER, "====~onPlayFromSearch: query =  " + query + ", extras = " + extras);
            int count = player.getMediaItemCount();
            for (int i = 0; i < count; i++) {
                MediaItem mediaItem = player.getMediaItemAt(i);
                if ((mediaItem.mediaMetadata.title != null && mediaItem.mediaMetadata.title.toString().contains(query))
                        || (mediaItem.mediaMetadata.artist != null && mediaItem.mediaMetadata.artist.toString().contains(query))
                        || (mediaItem.mediaMetadata.albumTitle != null && mediaItem.mediaMetadata.albumTitle.toString().contains(query))
                        || (mediaItem.mediaMetadata.albumArtist != null && mediaItem.mediaMetadata.albumArtist.toString().contains(query))
                        || (mediaItem.mediaMetadata.description != null && mediaItem.mediaMetadata.description.toString().contains(query))
                        || (mediaItem.mediaMetadata.displayTitle != null && mediaItem.mediaMetadata.displayTitle.toString().contains(query))
                ) {
                    player.setMediaItem(mediaItem);
                    player.play();
                    break;
                }
            }
        }

        @Override
        public void onPrepare() {
            Log.d(TAG_SESSION_LISTENER, "====~onPrepare");
            player.prepare();
        }

        @Override
        public void onPrepareFromMediaId(String mediaId, Bundle extras) {
            Log.d(TAG_SESSION_LISTENER, "====~onPrepareFromMediaId: mediaId =  " + mediaId + ", extras = " + extras);
            int count = player.getMediaItemCount();
            for (int i = 0; i < count; i++) {
                MediaItem mediaItem = player.getMediaItemAt(i);
                if (mediaItem.mediaId.equals(mediaId)) {
                    player.setMediaItem(mediaItem);
                    player.prepare();
                    break;
                }
            }
        }

        @Override
        public void onFastForward() {
            Log.d(TAG_SESSION_LISTENER, "====~onFastForward");
            float speed = player.getPlaybackParameters().speed;
            if (speed >= 16) {
                player.setPlaybackSpeed(1);
            } else {
                player.setPlaybackSpeed(speed * 2);
            }
        }

        @Override
        public void onAddQueueItem(MediaDescriptionCompat description) {
            Log.d(TAG_SESSION_LISTENER, "====~onAddQueueItem: description =  " + description);
            player.addMediaItem(MetadataUtils.turnMediaDescription2MediaItem(description));
        }

        @Override
        public void onAddQueueItem(MediaDescriptionCompat description, int index) {
            Log.d(TAG_SESSION_LISTENER, "====~onAddQueueItem: description =  " + description);
            List<MediaSessionCompat.QueueItem> list = session.getController().getQueue();
            session.setQueue(list);
            player.addMediaItem(index, MetadataUtils.turnMediaDescription2MediaItem(description));
        }

        @Override
        public void onRemoveQueueItem(MediaDescriptionCompat description) {
            Log.d(TAG_SESSION_LISTENER, "====~onRemoveQueueItem: description =  " + description);
            int count = player.getMediaItemCount();
            for (int i = 0; i < count; i++) {
                MediaItem mediaItem = player.getMediaItemAt(i);
                if (mediaItem.mediaId.equals(description.getMediaId())) {
                    // 如果是当期音乐，则播放下一首后，再删除
                    if (player.getCurrentMediaItem() == mediaItem) {
                        player.next();
                    }
                    player.removeMediaItem(i);
                    break;
                }
            }
        }

        @Override
        public void onSetRepeatMode(int repeatMode) {
            Log.d(TAG_SESSION_LISTENER, "====~onSetRepeatMode: repeatMode =  " + repeatMode);
            player.setRepeatMode(repeatMode);
        }

        @Override
        public void onSetPlaybackSpeed(float speed) {
            Log.d(TAG_SESSION_LISTENER, "====~onSetPlaybackSpeed: speed =  " + speed);
            player.setPlaybackSpeed(speed);
        }

        @Override
        public void onCommand(String command, Bundle extras, ResultReceiver cb) {
            Log.d(TAG_SESSION_LISTENER, "====~onCommand: command =  " + command + ", extras = " + extras + ", cb = " + cb);
            super.onCommand(command, extras, cb);
        }

        @Override
        public void onSetShuffleMode(int shuffleMode) {
            Log.d(TAG_SESSION_LISTENER, "====~onSetShuffleMode: shuffleMode =  " + shuffleMode);
            player.setShuffleModeEnabled(shuffleMode == PlaybackStateCompat.SHUFFLE_MODE_NONE);
        }

    }
}
