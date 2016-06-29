package com.csycc.androidphotoselector.entity;

/**
 * Created by zg on 16/6/28.
 */
public class PhotoEntity {

    private String filePath;

    public PhotoEntity(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
