package com.csycc.androidphotoselector.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.csycc.androidphotoselector.PhotoSelector;
import com.csycc.androidphotoselector.R;
import com.csycc.androidphotoselector.data.PhotoStoreLoader;
import com.csycc.androidphotoselector.entity.AlbumEntity;
import com.csycc.androidphotoselector.entity.PhotoEntity;
import com.csycc.androidphotoselector.utils.MediaStoreUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zg on 16/6/28.
 */
public class SelectorActivity extends Activity {

    public static void startInstance(Activity activity, int requestCode, int maxPhotoNumber) {
        Intent intent = new Intent(activity, SelectorActivity.class);
        intent.putExtra(KEY_MAX_PHONE_NUMBER, maxPhotoNumber < 1 ? 1 : maxPhotoNumber);
        activity.startActivityForResult(intent, requestCode);
    }

    public static ArrayList<String> getPhotoPathListFromIntentData(Intent data) {
        ArrayList<String> filePathList = null;
        if (null != data) {
            filePathList = (ArrayList<String>) data.getSerializableExtra(KEY_PHOTO_PATH_LIST);
        }
        return null == filePathList ? new ArrayList<String>() : filePathList;
    }

    private static final String KEY_MAX_PHONE_NUMBER = "max_phone_number";
    private static final String KEY_PHOTO_PATH_LIST = "photo_path_list";

    private int maxPhotoNumber;

    private TextView sureBtn, albumSelectBtn, previewBtn;

    private PhotoStoreLoader mPhotoStoreLoader;

    private RecyclerView photoRecyclerView;
    private List<PhotoEntity> photoEntityList;
    private SelectorPhotoAdapter mSelectorPhotoAdapter;

    private List<AlbumEntity> albumEntityList;
    private AlbumSelectorPopupWindow mAlbumSelectorPopupWindow;
    private int lastAlbumSelectIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aps_activity_selector);

        maxPhotoNumber = getIntent().getIntExtra(KEY_MAX_PHONE_NUMBER, 1);

        initView();

        initData();
    }

    private void initView() {
        sureBtn = (TextView) findViewById(R.id.sure_btn);
        sureBtn.setText("完成(0/" + maxPhotoNumber + ")");
        albumSelectBtn = (TextView) findViewById(R.id.album_select_btn);
        previewBtn = (TextView) findViewById(R.id.preview_btn);

        photoRecyclerView = (RecyclerView) findViewById(R.id.photo_recycler_view);
        photoRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        photoEntityList = new ArrayList<>();
        mSelectorPhotoAdapter = new SelectorPhotoAdapter(this, photoEntityList, new SelectorPhotoAdapter.PhotoSelectListener() {
            @Override
            public void onSizeChanged(int size) {
                previewBtn.setText("预览(" + size + ")");
                sureBtn.setText("完成(" + size + "/" + maxPhotoNumber + ")");
            }
        }, maxPhotoNumber);
        photoRecyclerView.setAdapter(mSelectorPhotoAdapter);
    }

    private void initData() {
        mPhotoStoreLoader = new PhotoStoreLoader();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mPhotoStoreLoader.loadAllAlbums(SelectorActivity.this);
                albumEntityList = mPhotoStoreLoader.getAlbumList();

                SelectorActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addCameraItem();
                        showOneAlbum(0);
                    }
                });
            }
        }).run();
    }

    private void addCameraItem() {
        if (PhotoSelector.getIsNeedCamera()) {
            AlbumEntity albumEntity = albumEntityList.get(0);
            mPhotoStoreLoader.getPhotoListByName(albumEntity.getAlbumName()).add(0, null);
        }
    }

    private void showAlbumSelectorPopupWindow(View view) {
        if (null == mAlbumSelectorPopupWindow) {
            mAlbumSelectorPopupWindow = new AlbumSelectorPopupWindow(this, new AlbumSelectorPopupWindow.SelectorListener() {
                @Override
                public void onSelected(int index) {
                    showOneAlbum(index);
                }
            });
        }
        mAlbumSelectorPopupWindow.showAsDropDown(view, albumEntityList, lastAlbumSelectIndex);
    }

    private void showOneAlbum(int albumIndex) {
        lastAlbumSelectIndex = albumIndex;
        AlbumEntity albumEntity = albumEntityList.get(albumIndex);
        albumSelectBtn.setText(albumEntity.getAlbumName());
        photoEntityList = mPhotoStoreLoader.getPhotoListByName(albumEntity.getAlbumName());
        mSelectorPhotoAdapter.setDataList(photoEntityList);
    }

    public void selectorOnCLick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.back_btn) {
            this.finish();
        } else if (viewId == R.id.sure_btn) {
            setSelectorResult(mSelectorPhotoAdapter.getSelectPhotoPathList());
        } else if (viewId == R.id.album_select_btn) {
            showAlbumSelectorPopupWindow(view);
        } else if (viewId == R.id.preview_btn) {
            PreviewActivity.startInstance(this, mSelectorPhotoAdapter.getSelectPhotoPathList());
        }
    }

    private void setSelectorResult(ArrayList<String> pathList) {
        Intent intent = new Intent();
        intent.putExtra(KEY_PHOTO_PATH_LIST, pathList);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MediaStoreUtil.IMAGE_CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            String pathFromURI = MediaStoreUtil.getSysCaptureCacheFilePath();
            if (null != pathFromURI) {
                ArrayList<String> pathList = new ArrayList<>();
                pathList.add(pathFromURI);
                setSelectorResult(pathList);
            }
        }
    }
}
