package com.example.mylibrary.service;


import android.media.MediaFormat;
import android.net.Uri;
import android.os.Bundle;
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

public class MediaService extends MediaBrowserServiceCompat {
    private static final String TAG = MediaService.class.getSimpleName();
    private static final String TAG_PLAYER_LISTENER = "Player.Listener";
    private static final String TAG_SESSION_LISTENER = "MediaSession.Callback";
    private MediaSessionCompat session;
    private ExoPlayer player;

    @Override
    public void onCreate() {
        super.onCreate();
        session = new MediaSessionCompat(this, "MyMusicService");
        setSessionToken(session.getSessionToken());
        session.setCallback(callback);
        session.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
        );
        player = new SimpleExoPlayer.Builder(this).build();
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
                Log.d(TAG_PLAYER_LISTENER, "====~onMediaItemTransition : mediaItem = " + mediaItem + " ,  reason = " + reason);
            }

            @Override
            public void onTimelineChanged(Timeline timeline, int reason) {
                Player.Listener.super.onTimelineChanged(timeline, reason);
                Log.d(TAG_PLAYER_LISTENER, "====~onTimelineChanged : timeline = " + timeline + " ,  reason = " + reason);
            }

            @Override
            public void onMediaMetadataChanged(MediaMetadata mediaMetadata) {
                Player.Listener.super.onMediaMetadataChanged(mediaMetadata);
                Log.d(TAG_PLAYER_LISTENER, "====~onMediaMetadataChanged : mediaMetadata = " + mediaMetadata);
            }

            @Override
            public void onPositionDiscontinuity(Player.PositionInfo oldPosition, Player.PositionInfo newPosition, int reason) {
                Player.Listener.super.onPositionDiscontinuity(oldPosition, newPosition, reason);
                Log.d(TAG_PLAYER_LISTENER, "====~onPositionDiscontinuity : oldPosition = " + oldPosition + " ,  newPosition = " + newPosition + " ,  reason = " + reason);
            }

            @Override
            public void onEvents(Player player, Player.Events events) {
                Player.Listener.super.onEvents(player, events);
                List<Integer> event = new ArrayList<>();
                for (int i = 0; i < events.size(); i++) {
                    event.add(events.get(i));
                }
                Log.d(TAG_PLAYER_LISTENER, "====~onEvents : player = " + player + " ,  events = " + event);
            }

            @Override
            public void onPlaybackStateChanged(int state) {
                Player.Listener.super.onPlaybackStateChanged(state);
                Log.d(TAG_PLAYER_LISTENER, "====~onPlaybackStateChanged : state = " + state);
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

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
        return new MediaBrowserServiceCompat.BrowserRoot(clientPackageName, rootHints);
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {
        result.sendResult(new ArrayList());
    }

    private MediaSessionCompat.Callback callback = new MediaSessionCompat.Callback() {
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
                    List<MediaDescriptionCompat> list = extras.getParcelableArrayList(Constants.KEY_QUEUE);
                    List<MediaSource> playlist = new ArrayList<>();
                    List<MediaSessionCompat.QueueItem> queue = new ArrayList<>();
                    DefaultMediaSourceFactory factory = new DefaultMediaSourceFactory(MediaService.this);
                    if (list != null) {
                        for (int i = 0; i < list.size(); i++) {
                            MediaDescriptionCompat description = list.get(i);
                            Uri uri = description.getMediaUri();
                            Log.d(TAG_SESSION_LISTENER, "====~onCustomAction: index = " + i + ", uri = " + uri);
                            MediaItem mediaItem = MediaItem.fromUri(uri);//turnMediaDescription2MediaItem(description);
                            queue.add(new MediaSessionCompat.QueueItem(description, i));
                            playlist.add(factory.createMediaSource(mediaItem));
                        }
                    }
                    session.setQueue(queue);
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
            player.addMediaItem(turnMediaDescription2MediaItem(description));
        }

        @Override
        public void onAddQueueItem(MediaDescriptionCompat description, int index) {
            Log.d(TAG_SESSION_LISTENER, "====~onAddQueueItem: description =  " + description);
            List<MediaSessionCompat.QueueItem> list = session.getController().getQueue();
            session.setQueue(list);
            player.addMediaItem(index, turnMediaDescription2MediaItem(description));
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
    };

    public static MediaItem turnMediaDescription2MediaItem(@Nullable @NonNull MediaDescriptionCompat description) {
        if (description == null || description.getMediaId() == null) {
            return null;
        }
        MediaMetadata.Builder mediaMetadataBuilder = new MediaMetadata.Builder();
        mediaMetadataBuilder.setTitle(description.getTitle())
                .setMediaUri(description.getMediaUri());
        if (description.getExtras() != null) {
            if (description.getExtras().containsKey(MediaMetadataCompat.METADATA_KEY_ALBUM))
                mediaMetadataBuilder.setAlbumTitle(description.getExtras().getString(MediaMetadataCompat.METADATA_KEY_ALBUM));
            if (description.getExtras().containsKey(MediaMetadataCompat.METADATA_KEY_ARTIST))
                mediaMetadataBuilder.setArtist(description.getExtras().getString(MediaMetadataCompat.METADATA_KEY_ARTIST));
            if (description.getExtras().containsKey(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE))
                mediaMetadataBuilder.setDisplayTitle(description.getExtras().getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE));
            mediaMetadataBuilder.setExtras(description.getExtras());
        }
        MediaMetadata mediaMetadata = mediaMetadataBuilder.build();
        MediaItem.Builder builder = new MediaItem.Builder()
                .setMediaId(description.getMediaId())
                .setUri(description.getMediaUri())
                .setMediaId(description.getMediaId())
                .setMediaMetadata(mediaMetadata);
        if (description.getExtras() != null && description.getExtras().containsKey(MediaFormat.KEY_MIME)) {
            builder.setMimeType(description.getExtras().getString(MediaFormat.KEY_MIME));
        }
        return builder.build();
    }

    @Override
    public void onCustomAction(@NonNull String action, Bundle extras, @NonNull Result<Bundle> result) {
        Log.d(TAG, "====~onCustomAction: action =  " + action + ", extras =  " + extras + ", result =  " + result);
        super.onCustomAction(action, extras, result);
    }

}
