package com.csycc.androidphotoselector.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;

/**
 * Created by zg on 16/5/11.
 */
public class MediaStoreUtil {

    /**
     * 手机取视频
     */
    public static final int VIDEO_ALBUM_REQUEST_CODE = 9;
    /**
     * 手机拍摄照片
     */
    public static final int IMAGE_CAMERA_REQUEST_CODE = 10;
    /**
     * 手机相册照片
     */
    public static final int IMAGE_ALBUM_REQUEST_CODE = 11;

    private static String captureCacheFilePath = null;

    public static void startSysImageCamera(Activity activity) {
        String fileDirPath = Environment.getExternalStorageDirectory() + "/DCIM/Camera/";
        File dirFile = new File(fileDirPath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        captureCacheFilePath = fileDirPath + System.currentTimeMillis() + ".jpg";
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(captureCacheFilePath)));
        activity.startActivityForResult(intent, IMAGE_CAMERA_REQUEST_CODE);
    }

    public static String getSysCaptureCacheFilePath() {
        if (null != captureCacheFilePath) {
            if (new File(captureCacheFilePath).exists()) {
                return captureCacheFilePath;
            }
        }
        return null;
    }

    public static void startSysImageAlbum(Activity activity) {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        activity.startActivityForResult(intent, IMAGE_ALBUM_REQUEST_CODE);
    }

    public static String getRealImagePathFromURI(Context context, Uri contentUri) {
        return getRealPathFromURI(context, contentUri, MediaStore.Images.Media.DATA);
    }

    public static void startSysVideoAlbum(Activity activity) {
        Intent intent = new Intent();
        intent.setType("video/*");
        /* 使用Intent.ACTION_GET_CONTENT这个Action */
        intent.setAction(Intent.ACTION_PICK);
        activity.startActivityForResult(intent, VIDEO_ALBUM_REQUEST_CODE);
    }

    public static String getRealVideoPathFromURI(Context context, Uri contentUri) {
        return getRealPathFromURI(context, contentUri, MediaStore.Video.Media.DATA);
    }

    private static String getRealPathFromURI(Context context, Uri contentUri, String columnName) {
        String res = null;
        if (null == contentUri) {
            return res;
        }
        String uriString = contentUri.toString();
        if (uriString.startsWith("file:")) {
            res = contentUri.getPath();
            return res;
        }

        String[] projection = {columnName};
        Cursor cursor = context.getContentResolver().query(contentUri, projection, null, null, null);
        if (null != cursor && cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(columnName);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
}
