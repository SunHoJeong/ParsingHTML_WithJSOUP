package com.example.suno.kakao;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<HashMap<String, String>> imageList;
    private ImageLoader imageLoader;

    public RecyclerViewAdapter(Context context, List<HashMap<String, String>> imageList) {
        this.context = context;
        this.imageList = imageList;

        imageLoader = new ImageLoader(this.context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageViewHolder vh = new ImageViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image, parent, false), imageLoader);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ImageViewHolder) holder).bindView(imageList.get(position));
    }

    @Override
    public int getItemCount() {
        return imageList != null ? imageList.size() : 0;
    }

}
