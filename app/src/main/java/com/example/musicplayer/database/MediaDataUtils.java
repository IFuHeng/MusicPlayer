package com.example.musicplayer.database;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;

import androidx.annotation.NonNull;

import com.example.musicplayer.entity.MusicBean;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MediaDataUtils {

    public static List<MusicBean> getMusic(Context context) {
        List<MusicBean> localMusicAll = MyDatabase.getInstance(context).getMusicDao().getAll();
        List<MusicBean> localSysMusicAll = getMusicsFromSysDb(context);
        ArrayList<MusicBean> result = new ArrayList<>(localMusicAll);
        for (int i = 0; i < localSysMusicAll.size(); i++) {
            MusicBean musicBean = localSysMusicAll.get(i);
            if (!result.contains(musicBean)) {
                musicBean.setInLocalStorage(false);
                result.add(musicBean);
            }
        }
        return result;
    }

    public static List<MusicBean> getMusicsFromSysDb(Context context) {
        ArrayList<MusicBean> result = new ArrayList<>();
        Cursor mAudioCursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,// 字段　没有字段　就是查询所有信息　相当于SQL语句中的　“ * ”
                null, // 查询条件
                null, // 条件的对应?的参数
                MediaStore.Audio.AudioColumns.TITLE);// 排序方式
// 循环输出歌曲的信息
        for (int i = 0; i < mAudioCursor.getCount(); i++) {
            mAudioCursor.moveToNext();
// 找到歌曲标题和总时间对应的列索引
            long id = mAudioCursor.getLong(mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)); // 音乐id
            String title = mAudioCursor.getString((mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));// 音乐标题
            String artist = mAudioCursor.getString(mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));// 艺术家
            long duration = mAudioCursor.getLong(mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));// 时长
            long size = mAudioCursor.getLong(mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)); // 文件大小
            String url = mAudioCursor.getString(mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)); // 文件路径
            int isMusic = mAudioCursor.getInt(mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.IS_MUSIC));// 是否为音乐
            String displayName = mAudioCursor.getString(mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
            String album = mAudioCursor.getString(mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
            String mimeType = mAudioCursor.getString(mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE));
            if (isMusic != 0) {//只有当是音乐的时候才保存
                MusicBean mp3Info = new MusicBean(id, title, artist, duration, size, url, displayName, album, mimeType);
                mp3Info.setInLocalStorage(true);
                result.add(mp3Info);
            }
        }
        mAudioCursor.close();
        Collections.sort(result, (o1, o2) -> (int) (o1.getId() - o2.getId()));
        return result;
    }

    public static List<MediaDescriptionCompat> getMediaDescriptionFromSysDb(Context context) {
        ArrayList<MediaDescriptionCompat> result = new ArrayList<>();
        Cursor mAudioCursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,// 字段　没有字段　就是查询所有信息　相当于SQL语句中的　“ * ”
                "mime_type=?", // 查询条件
                new String[]{"audio/mp3"}, // 条件的对应?的参数
                MediaStore.Audio.AudioColumns.TITLE);// 排序方式

        // 循环输出歌曲的信息
        for (int i = 0; i < mAudioCursor.getCount(); i++) {
            mAudioCursor.moveToNext();
            // 找到歌曲标题和总时间对应的列索引
            long id = mAudioCursor.getLong(mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)); // 音乐id
            String title = mAudioCursor.getString((mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));// 音乐标题
            String artist = mAudioCursor.getString(mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));// 艺术家
            long duration = mAudioCursor.getLong(mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));// 时长
            long size = mAudioCursor.getLong(mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)); // 文件大小
            String url = mAudioCursor.getString(mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)); // 文件路径
            int isMusic = mAudioCursor.getInt(mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.IS_MUSIC));// 是否为音乐
            String displayName = mAudioCursor.getString(mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
            String album = mAudioCursor.getString(mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
            String mimeType = mAudioCursor.getString(mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE));

            if (isMusic != 0) {//只有当是音乐的时候才保存
                Bundle extras = new Bundle();
                extras.putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, displayName);
                extras.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artist);
                extras.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration);
                extras.putString(MediaMetadataCompat.METADATA_KEY_ALBUM, album);
                extras.putLong(MediaMetadataCompat.METADATA_KEY_ADVERTISEMENT, size);
                extras.putLong(MediaMetadataCompat.METADATA_KEY_ART, size);
                extras.putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, mimeType);
//                setAudioInfo(url, extras);
                MediaDescriptionCompat.Builder builder = new MediaDescriptionCompat.Builder()
                        .setMediaId(String.valueOf(id))
                        .setMediaUri(Uri.parse(url))
                        .setDescription(url)
                        .setSubtitle(mimeType)
                        .setTitle(title)
                        .setExtras(extras);

                result.add(builder.build());
            }
        }
        mAudioCursor.close();
        return result;
    }

    public static List<MediaMetadataCompat> getMediaMetadataFromSysDb(Context context) {
        ArrayList<MediaMetadataCompat> result = new ArrayList<>();
        Cursor mAudioCursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,// 字段　没有字段　就是查询所有信息　相当于SQL语句中的　“ * ”
                "mime_type=?", // 查询条件
                new String[]{"audio/mp3"}, // 条件的对应?的参数
                MediaStore.Audio.AudioColumns.TITLE);// 排序方式

        // 循环输出歌曲的信息
        for (int i = 0; i < mAudioCursor.getCount(); i++) {
            mAudioCursor.moveToNext();
            // 找到歌曲标题和总时间对应的列索引
            long id = mAudioCursor.getLong(mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)); // 音乐id
            String title = mAudioCursor.getString((mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));// 音乐标题
            String artist = mAudioCursor.getString(mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));// 艺术家
            long duration = mAudioCursor.getLong(mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));// 时长
            long size = mAudioCursor.getLong(mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)); // 文件大小
            String url = mAudioCursor.getString(mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)); // 文件路径
            int isMusic = mAudioCursor.getInt(mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.IS_MUSIC));// 是否为音乐
            String displayName = mAudioCursor.getString(mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
            String album = mAudioCursor.getString(mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
            String mimeType = mAudioCursor.getString(mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE));

            if (isMusic != 0) {//只有当是音乐的时候才保存
                MediaMetadataCompat.Builder builder = new MediaMetadataCompat.Builder()
                        .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, String.valueOf(id))
                        .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration)
                        .putLong(MediaMetadataCompat.METADATA_KEY_ADVERTISEMENT, size)
                        .putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
                        .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, album)
                        .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, displayName)
                        .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, artist + "-" + title)
                        .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, url)
                        .putString(MediaMetadataCompat.METADATA_KEY_GENRE, mimeType)
                        .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artist);
                result.add(builder.build());
            }
        }
        mAudioCursor.close();
        return result;
    }

    public static List<MediaSource> getMediaSourceFromSysDb(Context context, DefaultMediaSourceFactory factory) {
        ArrayList<MediaSource> result = new ArrayList<>();
        Cursor mAudioCursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,// 字段　没有字段　就是查询所有信息　相当于SQL语句中的　“ * ”
                null, // 查询条件
                null, // 条件的对应?的参数
                MediaStore.Audio.AudioColumns.TITLE);// 排序方式

        // 循环输出歌曲的信息
        for (int i = 0; i < mAudioCursor.getCount(); i++) {
            mAudioCursor.moveToNext();
            // 找到歌曲标题和总时间对应的列索引
            long id = mAudioCursor.getLong(mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)); // 音乐id
            String title = mAudioCursor.getString((mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));// 音乐标题
            String artist = mAudioCursor.getString(mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));// 艺术家
            long duration = mAudioCursor.getLong(mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));// 时长
            long size = mAudioCursor.getLong(mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)); // 文件大小
            String url = mAudioCursor.getString(mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)); // 文件路径
            int isMusic = mAudioCursor.getInt(mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.IS_MUSIC));// 是否为音乐
            String displayName = mAudioCursor.getString(mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
            String album = mAudioCursor.getString(mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
            String mimeType = mAudioCursor.getString(mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE));

            if (isMusic != 0) {//只有当是音乐的时候才保存
                Bundle extras = new Bundle();
                extras.putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, displayName);
                extras.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artist);
                extras.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration);
                extras.putLong(MediaMetadataCompat.METADATA_KEY_ADVERTISEMENT, size);
                extras.putString(MediaMetadataCompat.METADATA_KEY_ALBUM, album);
//                setAudioInfo(url, extras);

                MediaMetadata mediaMetadata = new MediaMetadata.Builder()
                        .setTitle(title)
                        .setMediaUri(Uri.parse(url))
                        .setAlbumTitle(album)
                        .setArtist(artist)
                        .setDisplayTitle(displayName)
                        .setExtras(extras).build();
                MediaItem.Builder builder = new MediaItem.Builder().setMediaId(String.valueOf(id))
                        .setMimeType(mimeType)
                        .setUri(url)
                        .setMediaMetadata(mediaMetadata);

                result.add(factory.createMediaSource(builder.build()));
            }
        }
        mAudioCursor.close();
        return result;
    }

    private static void setAudioInfo(String path, Bundle bundle) {
        MediaExtractor mediaExtractor = new MediaExtractor();
        try {
            mediaExtractor.setDataSource(path);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        int indexOfTrackAudio = -1;
        String mime = null;
        for (int i = 0; i < mediaExtractor.getTrackCount(); i++) {
            MediaFormat format = mediaExtractor.getTrackFormat(i);
            mime = format.getString(MediaFormat.KEY_MIME);
            if (mime.startsWith("audio/")) {
                if (indexOfTrackAudio == -1) {
                    indexOfTrackAudio = i;
                }
            }
        }

        if (indexOfTrackAudio == -1) {
            return;
        }

        mediaExtractor.selectTrack(indexOfTrackAudio);
        MediaFormat format = mediaExtractor.getTrackFormat(indexOfTrackAudio);
        long duration = format.getLong(MediaFormat.KEY_DURATION);
        int channelCount = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
        int bitRate = format.getInteger(MediaFormat.KEY_BIT_RATE);
        int sampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE);

        bundle.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration);
        bundle.putString(MediaFormat.KEY_MIME, mime);
        bundle.putInt(MediaFormat.KEY_CHANNEL_COUNT, channelCount);
        bundle.putInt(MediaFormat.KEY_BIT_RATE, bitRate);
        bundle.putInt(MediaFormat.KEY_SAMPLE_RATE, sampleRate);
    }

    public static MediaItem turnMediaDescription2MediaItem(@NonNull MediaDescriptionCompat description) {
        if (description.getMediaId() == null) {
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
}

