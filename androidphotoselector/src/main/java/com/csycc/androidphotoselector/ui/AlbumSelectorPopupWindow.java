package com.csycc.androidphotoselector.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.csycc.androidphotoselector.R;
import com.csycc.androidphotoselector.entity.AlbumEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zg on 16/6/28.
 */
public class AlbumSelectorPopupWindow extends PopupWindow {

    private RecyclerView albumRecyclerView;
    private AlbumSelectorAdapter mAlbumSelectorAdapter;

    public AlbumSelectorPopupWindow(Context context, final SelectorListener selectorListener) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.aps_popup_album_selector, null);
        setContentView(contentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);

        DisplayMetrics mDisplayMetrics = context.getResources().getDisplayMetrics();
        int heightPixels = mDisplayMetrics.heightPixels;
        float displayDensity = mDisplayMetrics.density;

        setHeight((int) (heightPixels - 160 * displayDensity));

        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable(context.getResources(), (Bitmap) null));

        albumRecyclerView = (RecyclerView) contentView.findViewById(R.id.album_recycler_view);
        albumRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mAlbumSelectorAdapter = new AlbumSelectorAdapter(new ArrayList<AlbumEntity>(), new SelectorListener() {
            @Override
            public void onSelected(int index) {
                selectorListener.onSelected(index);
                dismiss();
            }
        });
        albumRecyclerView.setAdapter(mAlbumSelectorAdapter);
    }

    public void showAsDropDown(View anchor, List<AlbumEntity> albumEntityList, int lastSelectIndex) {
        mAlbumSelectorAdapter.setData(albumEntityList, lastSelectIndex);
        albumRecyclerView.scrollToPosition(0);
        showAsDropDown(anchor);
    }

    public interface SelectorListener {
        void onSelected(int index);
    }

}
