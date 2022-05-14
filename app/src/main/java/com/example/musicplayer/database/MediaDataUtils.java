package com.example.musicplayer.database;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.musicplayer.entity.MusicBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MediaDataUtils {
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
                result.add(mp3Info);
            }
        }
        mAudioCursor.close();
        Collections.sort(result, (o1, o2) -> (int) (o1.getId() - o2.getId()));
        return result;
    }
}

