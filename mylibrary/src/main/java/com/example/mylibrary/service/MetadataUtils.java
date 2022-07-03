package com.example.mylibrary.service;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;

import java.util.ArrayList;
import java.util.List;

public class MetadataUtils {
    public static MediaDescriptionCompat turnMetadata2Description(MediaMetadataCompat metadata) {
        MediaDescriptionCompat.Builder bob = new MediaDescriptionCompat.Builder();
        bob.setMediaId(metadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID));
        bob.setTitle(metadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE));
        bob.setSubtitle(metadata.getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE));
        bob.setDescription(metadata.getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION));
        if (metadata.containsKey(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI)) {
            bob.setIconUri(Uri.parse(metadata.getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI)));
        }
        if (metadata.containsKey(MediaMetadataCompat.METADATA_KEY_MEDIA_URI)) {
            bob.setMediaUri(Uri.parse(metadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI)));
        }

        Bundle bundle = new Bundle();
        if (metadata.containsKey(MediaMetadataCompat.METADATA_KEY_BT_FOLDER_TYPE)) {
            bundle.putLong(MediaDescriptionCompat.EXTRA_BT_FOLDER_TYPE, metadata.getLong(MediaMetadataCompat.METADATA_KEY_BT_FOLDER_TYPE));
        }
        if (bundle.containsKey(MediaMetadataCompat.METADATA_KEY_DOWNLOAD_STATUS)) {
            bundle.putLong(MediaDescriptionCompat.EXTRA_DOWNLOAD_STATUS, metadata.getLong(MediaMetadataCompat.METADATA_KEY_DOWNLOAD_STATUS));
        }
        if (metadata.containsKey(MediaMetadataCompat.METADATA_KEY_DURATION)) {
            bundle.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION));
        }
        if (!bundle.isEmpty()) {
            bob.setExtras(bundle);
        }

        return bob.build();
    }

    public static MediaMetadataCompat turnDescription2MediaMetadata(MediaDescriptionCompat description) {
        MediaMetadataCompat.Builder builder = new MediaMetadataCompat.Builder();
        if (description == null || description.getExtras() == null) {
            return builder.build();
        }
        builder.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, description.getExtras().getLong(MediaMetadataCompat.METADATA_KEY_DURATION));
        return builder.build();
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
}
