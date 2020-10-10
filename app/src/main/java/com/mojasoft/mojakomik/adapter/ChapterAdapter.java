package com.mojasoft.mojakomik.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mojasoft.mojakomik.R;
import com.mojasoft.mojakomik.model.ChapterModel;

import java.util.ArrayList;
import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ListViewHolder> {

    private List<ChapterModel> listChapter;

    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public ChapterAdapter(ArrayList<ChapterModel> list) {
        this.listChapter = list;
    }

    public void renewItems(List<ChapterModel> chapterModels) {
        this.listChapter = chapterModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChapterAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_chapter, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterAdapter.ListViewHolder holder, final int position) {
        ChapterModel chapterModel = listChapter.get(position);

        holder.tvChapterTitle.setText(chapterModel.getChapter_title());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listChapter.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listChapter.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        TextView tvChapterTitle;
        ListViewHolder(@NonNull View itemView) {
            super(itemView);

            tvChapterTitle = itemView.findViewById(R.id.tv_chapter_title);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(ChapterModel data);
    }
}
