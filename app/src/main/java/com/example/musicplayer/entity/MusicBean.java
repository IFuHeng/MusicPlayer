package com.example.musicplayer.entity;

import java.util.Objects;

public class MusicBean {
    private long id;
    private String title;
    private String artist;
    private long duration;
    private long size;
    private String url;
    String displayName;
    String album;
    String mimeType;

    /**
     * Cache the hash code for the MusicBean
     */
    private int hash;

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

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MusicBean musicBean = (MusicBean) o;
        return id == musicBean.id && duration == musicBean.duration && size == musicBean.size && hash == musicBean.hash && Objects.equals(title, musicBean.title) && Objects.equals(artist, musicBean.artist) && Objects.equals(url, musicBean.url) && Objects.equals(displayName, musicBean.displayName) && Objects.equals(album, musicBean.album) && Objects.equals(mimeType, musicBean.mimeType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, artist, duration, size, url, displayName, album, mimeType, hash);
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
