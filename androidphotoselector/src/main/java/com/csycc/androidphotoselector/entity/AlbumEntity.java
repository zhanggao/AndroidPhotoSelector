package com.csycc.androidphotoselector.entity;

/**
 * Created by zg on 16/6/28.
 */
public class AlbumEntity {
    private String albumName;
    private int photoCount = 0;
    private String albumCoverPath;

    public AlbumEntity(String albumName) {
        this.albumName = albumName;
    }

    public AlbumEntity(String albumName, String albumCoverPath, int photoCount) {
        this.albumName = albumName;
        this.albumCoverPath = albumCoverPath;
        this.photoCount = photoCount;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setPhotoCount(int photoCount) {
        this.photoCount = photoCount;
    }

    public int getPhotoCount() {
        return photoCount;
    }

    public void setAlbumCoverPath(String albumCoverPath) {
        this.albumCoverPath = albumCoverPath;
    }

    public String getAlbumCoverPath() {
        return albumCoverPath;
    }

}
