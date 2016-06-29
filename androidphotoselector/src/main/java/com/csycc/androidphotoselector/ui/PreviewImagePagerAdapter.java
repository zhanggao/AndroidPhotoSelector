package com.csycc.androidphotoselector.ui;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.csycc.androidphotoselector.PhotoSelector;
import com.csycc.androidphotoselector.third_lib.TouchImageView;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by zg on 16/6/29.
 */
public class PreviewImagePagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<String> imgPathList;
    private LinkedList<TouchImageView> recycleItemViewsList = new LinkedList<>();

    public PreviewImagePagerAdapter(Context context, List<String> imgPathList) {
        this.mContext = context;
        this.imgPathList = imgPathList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TouchImageView itemView;
        if (recycleItemViewsList.isEmpty()) {
            itemView = new TouchImageView(container.getContext());
            itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        } else {
            itemView = recycleItemViewsList.removeLast();
        }
        PhotoSelector.getImageLoader().loadImage(mContext, itemView, imgPathList.get(position));
        container.addView(itemView, 0);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        TouchImageView itemView = (TouchImageView) object;
        container.removeView(itemView);
        recycleItemViewsList.add(itemView);
    }

    @Override
    public int getCount() {
        return imgPathList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
