package com.csycc.androidphotoselector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import com.csycc.androidphotoselector.ui.SelectorActivity;

import java.util.ArrayList;

/**
 * Created by zg on 16/6/29.
 */
public class PhotoSelector {

    private static ImageLoader mImageLoader;

    public static void init(ImageLoader imageLoader) {
        mImageLoader = imageLoader;
    }

    public static ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public static void startSelector(Activity activity, int requestCode, int maxPhotoNumber) {
        SelectorActivity.startInstance(activity, requestCode, maxPhotoNumber);
    }

    public static ArrayList<String> getPhotoPathListFromIntentData(Intent data) {
        return SelectorActivity.getPhotoPathListFromIntentData(data);
    }

    private static boolean isNeedCamera = true;

    public static void setIsNeedCamera(boolean isNeed) {
        isNeedCamera = isNeed;
    }

    public static boolean getIsNeedCamera() {
        return isNeedCamera;
    }

    public interface ImageLoader {
        void loadImage(Context context, ImageView imageView, String filePath);
    }
}
