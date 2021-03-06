package com.mojasoft.mojakomik.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.mojasoft.mojakomik.R;
import com.mojasoft.mojakomik.model.GenreDetailModel;

import java.util.ArrayList;
import java.util.List;

public class GenreDetailAdapter extends RecyclerView.Adapter<GenreDetailAdapter.ListViewHolder> {

    private List<GenreDetailModel> listGenreMain;

    public GenreDetailAdapter(ArrayList<GenreDetailModel> list) {
        this.listGenreMain = list;
    }

    public void renewItems(List<GenreDetailModel> genreDetailModels) {
        this.listGenreMain = genreDetailModels;
        notifyDataSetChanged();
    }

    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public GenreDetailAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_card, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreDetailAdapter.ListViewHolder holder, final int position) {
        GenreDetailModel genreDetailModel = listGenreMain.get(position);

        String str = genreDetailModel.getThumb();
        String[] arrOfStr = str.split("resize", 2);

        String finalImage = arrOfStr[0];

        Glide.with(holder.itemView.getContext())
                .load(finalImage)
                .override(Target.SIZE_ORIGINAL)
                .into(holder.imgPopular);

        holder.tvType.setText(genreDetailModel.getType());
        holder.tvTitlePopular.setText(genreDetailModel.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listGenreMain.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listGenreMain.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPopular;
        TextView tvTitlePopular, tvType;

        ListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tv_card_type);
            imgPopular = itemView.findViewById(R.id.img_card_photo);
            tvTitlePopular = itemView.findViewById(R.id.tv_card_judul);
            tvTitlePopular.setMaxLines(2);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(GenreDetailModel data);
    }
}
