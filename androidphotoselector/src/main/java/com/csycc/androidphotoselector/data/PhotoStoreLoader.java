package com.csycc.androidphotoselector.data;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.csycc.androidphotoselector.entity.AlbumEntity;
import com.csycc.androidphotoselector.entity.PhotoEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zg on 16/6/28.
 */
public class PhotoStoreLoader {

    private List<AlbumEntity> albumEntityList;
    private Map<String, List<PhotoEntity>> albumDetailMap;

    private String allPhotoAlbumName = "所有照片";

    public void loadAllAlbums(Context context) {
        albumEntityList = new ArrayList<>();
        AlbumEntity allPhotoAlbumEntity = new AlbumEntity(allPhotoAlbumName);
        albumEntityList.add(allPhotoAlbumEntity);
        albumDetailMap = new HashMap<>();
        ArrayList<PhotoEntity> allPhotoEntityList = new ArrayList<>();
        albumDetailMap.put(allPhotoAlbumName, allPhotoEntityList);

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, MediaStore.Images.ImageColumns.SIZE}, null, null, null);

        if (null == cursor) {
            return;
        }

        Map<String, AlbumEntity> map = new HashMap<>();
        String albumsName, photoFilePath;
        AlbumEntity albumEntity;
        PhotoEntity photoEntity;

        while (cursor.moveToNext()) {
            if (cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns.SIZE)) > 1024 * 10) {
                albumsName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME));
                photoFilePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));

                albumEntity = map.get(albumsName);
                if (null != albumEntity) {
                    albumEntity.setPhotoCount(albumEntity.getPhotoCount() + 1);
                } else {
                    albumEntity = new AlbumEntity(albumsName, photoFilePath, 1);
                    albumEntityList.add(albumEntity);
                    map.put(albumsName, albumEntity);
                }

                photoEntity = new PhotoEntity(photoFilePath);

                allPhotoEntityList.add(photoEntity);

                List<PhotoEntity> photoEntityList = albumDetailMap.get(albumsName);
                if (null == photoEntityList) {
                    photoEntityList = new ArrayList<>();
                    albumDetailMap.put(albumsName, photoEntityList);
                }
                photoEntityList.add(photoEntity);
            }
        }

        allPhotoAlbumEntity.setAlbumCoverPath(allPhotoEntityList.get(0).getFilePath());
        allPhotoAlbumEntity.setPhotoCount(allPhotoEntityList.size());
    }

    public List<AlbumEntity> getAlbumList() {
        return albumEntityList;
    }

    public List<PhotoEntity> getPhotoListByName(String albumName) {
        return albumDetailMap.get(albumName);
    }

}
