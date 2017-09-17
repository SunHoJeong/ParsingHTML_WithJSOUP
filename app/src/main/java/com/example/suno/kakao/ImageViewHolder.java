package com.example.suno.kakao;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.suno.kakao.R.id.imageView;


public class ImageViewHolder extends RecyclerView.ViewHolder {
    @BindView(imageView)
    ImageView imgv;
    @BindView(R.id.textView_title)
    TextView tvTitle;

    private ImageLoader imageLoader;

    public ImageViewHolder(View itemView, ImageLoader imageLoader) {
        super(itemView);
        this.imageLoader = imageLoader;

        ButterKnife.bind(this, itemView);
    }

    public void bindView(HashMap<String, String> map) {
        imageLoader.loadBitmap(map.get("url"), imgv);
        tvTitle.setText(map.get("title"));
    }
}
