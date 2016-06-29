package com.csycc.androidphotoselector.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.csycc.androidphotoselector.PhotoSelector;
import com.csycc.androidphotoselector.R;
import com.csycc.androidphotoselector.entity.AlbumEntity;

import java.util.List;

/**
 * Created by zg on 16/6/28.
 */
public class AlbumSelectorAdapter extends RecyclerView.Adapter<AlbumSelectorAdapter.ViewHolder> {

    private List<AlbumEntity> albumEntityList;
    private int lastSelectIndex = 0;
    private AlbumSelectorPopupWindow.SelectorListener mSelectorListener;

    public AlbumSelectorAdapter(List<AlbumEntity> albumEntityList, AlbumSelectorPopupWindow.SelectorListener selectorListener) {
        this.albumEntityList = albumEntityList;
        this.mSelectorListener = selectorListener;
    }

    public void setData(List<AlbumEntity> albumEntityList, int lastSelectIndex) {
        this.albumEntityList = albumEntityList;
        this.lastSelectIndex = lastSelectIndex;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.aps_item_album_selector, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AlbumEntity albumEntity = albumEntityList.get(position);
        PhotoSelector.getImageLoader().loadImage(holder.albumCoverImg.getContext(), holder.albumCoverImg, albumEntity.getAlbumCoverPath());
        holder.albumNameText.setText(albumEntity.getAlbumName());
        holder.albumCountText.setText(albumEntity.getPhotoCount() + "张");
        holder.albumIsSelect.setVisibility(position == lastSelectIndex ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return albumEntityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView albumCoverImg;
        private TextView albumNameText, albumCountText;
        private View albumIsSelect;

        public ViewHolder(View itemView) {
            super(itemView);

            albumCoverImg = (ImageView) itemView.findViewById(R.id.album_cover_img);
            albumNameText = (TextView) itemView.findViewById(R.id.album_name_text);
            albumCountText = (TextView) itemView.findViewById(R.id.album_count_text);
            albumIsSelect = itemView.findViewById(R.id.album_is_select);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    albumIsSelect.setVisibility(View.VISIBLE);
                    mSelectorListener.onSelected(ViewHolder.this.getAdapterPosition());
                }
            });
        }
    }
}
