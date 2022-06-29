package com.example.musicplayer.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class MusicBean {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    private long id;
    @ColumnInfo(typeAffinity = ColumnInfo.TEXT)
    private String title;
    @ColumnInfo(typeAffinity = ColumnInfo.TEXT)
    private String artist;
    @ColumnInfo(typeAffinity = ColumnInfo.INTEGER)
    private long duration;
    @ColumnInfo(typeAffinity = ColumnInfo.INTEGER)
    private long size;
    @ColumnInfo(typeAffinity = ColumnInfo.TEXT)
    private String url;
    @ColumnInfo(typeAffinity = ColumnInfo.TEXT)
    String displayName;
    @ColumnInfo(typeAffinity = ColumnInfo.TEXT)
    String album;
    @ColumnInfo(typeAffinity = ColumnInfo.TEXT)
    String mimeType;

    @Ignore
    /**
     * Cache the hash code for the MusicBean
     */
    private int hash;

    @Ignore
    private boolean isInLocalStorage;

    public MusicBean(long id, String title, String artist,
                     long duration, long size, String url,
                     String displayName, String album, String mimeType) {
        this.artist = artist;
        this.duration = duration;
        this.id = id;
        this.size = size;
        this.title = title;
        this.url = url;
        this.displayName = displayName;
        this.album = album;
        this.mimeType = mimeType;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public long getDuration() {
        return duration;
    }

    public long getSize() {
        return size;
    }

    public String getUrl() {
        return url;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getAlbum() {
        return album;
    }

    public String getMimeType() {
        return mimeType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MusicBean musicBean = (MusicBean) o;
        return id == musicBean.id && duration == musicBean.duration && size == musicBean.size && Objects.equals(title, musicBean.title) && Objects.equals(artist, musicBean.artist) && Objects.equals(url, musicBean.url) && Objects.equals(displayName, musicBean.displayName) && Objects.equals(album, musicBean.album) && Objects.equals(mimeType, musicBean.mimeType);
    }

    public boolean isInLocalStorage() {
        return isInLocalStorage;
    }

    public void setInLocalStorage(boolean inLocalStorage) {
        isInLocalStorage = inLocalStorage;
    }

    @Override
    public int hashCode() {
        if (hash == 0) {
            hash = Objects.hash(id, title, artist, duration, size, url, displayName, album, mimeType);
        }
        return hash;
    }

    @Override
    public String toString() {
        return "MusicBean{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", duration=" + duration +
                ", size=" + size +
                ", url='" + url + '\'' +
                ", displayName='" + displayName + '\'' +
                ", album='" + album + '\'' +
                ", mimeType='" + mimeType + '\'' +
                '}';
    }
}
