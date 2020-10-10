package com.mojasoft.mojakomik.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mojasoft.mojakomik.R;
import com.mojasoft.mojakomik.model.DetailChapterModel;

import java.util.ArrayList;
import java.util.List;

public class DetailChapterAdapter extends RecyclerView.Adapter<DetailChapterAdapter.ListViewHolder>{

    private List<DetailChapterModel> listChapter;

    public DetailChapterAdapter(ArrayList<DetailChapterModel> list) {
        this.listChapter = list;
    }

    public void renewItems(List<DetailChapterModel> detailChapterModels) {
        this.listChapter = detailChapterModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DetailChapterAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_detail_chapter, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailChapterAdapter.ListViewHolder holder, final int position) {
        DetailChapterModel detail = listChapter.get(position);

        Glide.with(holder.itemView.getContext())
                .load(detail.getChapter_image_link())
                .fitCenter()
                .into(holder.imgChapter);
    }

    @Override
    public int getItemCount() {
        return listChapter.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView imgChapter;
        ListViewHolder(@NonNull View itemView) {
            super(itemView);

            imgChapter = itemView.findViewById(R.id.imgDetailChapter);
        }
    }
}
