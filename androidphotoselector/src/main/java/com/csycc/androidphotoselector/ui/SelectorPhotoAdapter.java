package com.csycc.androidphotoselector.ui;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.csycc.androidphotoselector.PhotoSelector;
import com.csycc.androidphotoselector.R;
import com.csycc.androidphotoselector.entity.PhotoEntity;
import com.csycc.androidphotoselector.utils.MediaStoreUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by zg on 16/6/28.
 */
public class SelectorPhotoAdapter extends RecyclerView.Adapter<SelectorPhotoAdapter.ViewHolder> {

    private final int TYPE_NORMAL = 0;
    private final int TYPE_CAMERA = 1;

    private int itemHeight;
    private Activity mActivity;
    private List<PhotoEntity> photoEntityList;
    private HashSet<Integer> selectedIndexMap;
    private PhotoSelectListener mPhotoSelectListener;
    private int maxPhotoNumber;

    public SelectorPhotoAdapter(Activity activity, List<PhotoEntity> photoEntityList, PhotoSelectListener photoSelectListener, int maxPhotoNumber) {
        this.mActivity = activity;
        this.photoEntityList = photoEntityList;
        this.mPhotoSelectListener = photoSelectListener;
        this.maxPhotoNumber = maxPhotoNumber;

        selectedIndexMap = new HashSet<>();

        DisplayMetrics mDisplayMetrics = activity.getResources().getDisplayMetrics();
        itemHeight = mDisplayMetrics.widthPixels / 3;
    }

    public void setDataList(List<PhotoEntity> photoEntityList) {
        this.photoEntityList = photoEntityList;
        selectedIndexMap.clear();
        mPhotoSelectListener.onSizeChanged(0);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutResourceId = viewType == TYPE_CAMERA ? R.layout.aps_item_photo_selector_camera : R.layout.aps_item_photo_selector;
        return new ViewHolder(LayoutInflater.from(mActivity).inflate(layoutResourceId, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_NORMAL) {
            holder.photoCheckBox.setChecked(selectedIndexMap.contains(position) ? true : false);
            PhotoSelector.getImageLoader().loadImage(mActivity, holder.photoImg, photoEntityList.get(position).getFilePath());
        }
    }

    @Override
    public int getItemCount() {
        return photoEntityList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 && photoEntityList.size() > 0 && photoEntityList.get(0) == null ? TYPE_CAMERA : TYPE_NORMAL;
    }

    public ArrayList<String> getSelectPhotoPathList() {
        ArrayList<String> selectPhotoPathList = new ArrayList<>();
        for (Integer index : selectedIndexMap) {
            selectPhotoPathList.add(photoEntityList.get(index).getFilePath());
        }
        return selectPhotoPathList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView photoImg;
        private CheckBox photoCheckBox;

        public ViewHolder(final View itemView, int viewType) {
            super(itemView);

            itemView.getLayoutParams().height = itemHeight;

            if (viewType == TYPE_CAMERA) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MediaStoreUtil.startSysImageCamera(mActivity);
                    }
                });
                return;
            }

            photoImg = (ImageView) itemView.findViewById(R.id.photo_img);
            photoCheckBox = (CheckBox) itemView.findViewById(R.id.photo_check_box);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = ViewHolder.this.getAdapterPosition();
                    int size = selectedIndexMap.size();
                    if (selectedIndexMap.contains(position)) {
                        photoCheckBox.setChecked(false);
                        selectedIndexMap.remove(position);
                        size--;
                    } else {
                        if (size < maxPhotoNumber) {
                            photoCheckBox.setChecked(true);
                            selectedIndexMap.add(position);
                            size++;
                        } else {
                            Toast.makeText(itemView.getContext(), "最多只能选择" + maxPhotoNumber + "张图片", Toast.LENGTH_SHORT).show();
                        }
                    }
                    mPhotoSelectListener.onSizeChanged(size);
                }
            });
        }
    }

    public interface PhotoSelectListener {
        void onSizeChanged(int size);
    }
}
