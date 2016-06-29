package com.csycc.androidphotoselectordemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.csycc.androidphotoselector.PhotoSelector;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_PHOTO = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 只要在调用前初始化就可以了
        PhotoSelector.init(new PhotoSelector.ImageLoader() {
            @Override
            public void loadImage(Context context, ImageView imageView, String filePath) {
                Glide.with(context).load(new File(filePath)).placeholder(R.drawable.aps_default_img).into(imageView);
            }
        });
    }

    public void chosePhotoOnClick(View view) {
        PhotoSelector.startSelector(this, REQUEST_PHOTO, 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PHOTO && resultCode == Activity.RESULT_OK) {

            ArrayList<String> photoPathList = PhotoSelector.getPhotoPathListFromIntentData(data);

            for (int i = 0, len = photoPathList.size(); i < len; i++) {
                Log.i("xxxxxx", photoPathList.get(i));
            }
        }
    }
}
