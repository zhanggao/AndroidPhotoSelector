package com.csycc.androidphotoselector.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.csycc.androidphotoselector.R;

import java.util.ArrayList;

/**
 * Created by zg on 16/6/29.
 */
public class PreviewActivity extends Activity {

    private static final String KEY_PHOTO_PATH_LIST = "photo_path_list";

    public static void startInstance(Context context, ArrayList<String> photoPathList) {
        if (null != photoPathList && !photoPathList.isEmpty()) {
            Intent intent = new Intent(context, PreviewActivity.class);
            intent.putExtra(KEY_PHOTO_PATH_LIST, photoPathList);
            context.startActivity(intent);
        }
    }

    private TextView previewIndexText;
    private ArrayList<String> photoPathList;
    private PreviewImagePagerAdapter mPreviewImagePagerAdapter;
    private ViewPager previewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aps_activity_preview);

        photoPathList = (ArrayList<String>) getIntent().getSerializableExtra(KEY_PHOTO_PATH_LIST);

        previewIndexText = (TextView) findViewById(R.id.preview_index_text);
        previewIndexText.setText("1/" + photoPathList.size());

        mPreviewImagePagerAdapter = new PreviewImagePagerAdapter(this, photoPathList);
        previewPager = (ViewPager) findViewById(R.id.preview_pager);
        previewPager.setAdapter(mPreviewImagePagerAdapter);

        previewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                previewIndexText.setText(position + 1 + "/" + photoPathList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void previewOnClick(View view) {
        if (view.getId() == R.id.back_btn) {
            this.finish();
        }
    }
}
