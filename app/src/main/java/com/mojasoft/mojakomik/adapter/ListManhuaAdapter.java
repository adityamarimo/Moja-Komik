package com.mojasoft.mojakomik.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mojasoft.mojakomik.R;
import com.mojasoft.mojakomik.model.ListManhuaModel;

import java.util.ArrayList;

public class ListManhuaAdapter extends RecyclerView.Adapter<ListManhuaAdapter.ListViewHolder> {

    private ArrayList<ListManhuaModel> listManhua;

    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public ListManhuaAdapter(ArrayList<ListManhuaModel> list) {
        this.listManhua = list;
    }

    public void renewItems(ArrayList<ListManhuaModel> manhuaModels) {
        this.listManhua = manhuaModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListManhuaAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListManhuaAdapter.ListViewHolder holder, final int position) {
        ListManhuaModel listManhuaModel = listManhua.get(position);

        String str = listManhuaModel.getThumb();
        String[] arrOfStr = str.split("resize", 2);

        String finalImage = arrOfStr[0];

        Glide.with(holder.itemView.getContext())
                .load(finalImage)
                .apply(new RequestOptions().override(180, 130))
                .into(holder.imgManhua);

        holder.tvTitleManhua.setText(listManhuaModel.getTitle());
        holder.tvChapter.setText(listManhuaModel.getChapter());
        holder.tvUploadOn.setText(listManhuaModel.getUpdated_on());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listManhua.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listManhua.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView imgManhua;
        TextView tvTitleManhua, tvUploadOn, tvChapter;

        ListViewHolder(@NonNull View itemView) {
            super(itemView);

            imgManhua = itemView.findViewById(R.id.img_item_photo);
            tvTitleManhua = itemView.findViewById(R.id.tv_item_judul);
            tvUploadOn = itemView.findViewById(R.id.tv_item_upload);
            tvChapter = itemView.findViewById(R.id.tv_item_chapter);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(ListManhuaModel data);
    }
}
