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
import com.mojasoft.mojakomik.model.PopularModel;

import java.util.ArrayList;
import java.util.List;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.ListViewHolder> {

    private List<PopularModel> listPopular;

    public PopularAdapter(ArrayList<PopularModel> list) {
        this.listPopular = list;
    }

    public void renewItems(List<PopularModel> PopularModels) {
        this.listPopular = PopularModels;
        notifyDataSetChanged();
    }

    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public PopularAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_card, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularAdapter.ListViewHolder holder, final int position) {
        PopularModel popularModel = listPopular.get(position);

        String str = popularModel.getThumb();
        String[] arrOfStr = str.split("resize", 2);

        String finalImage = arrOfStr[0];

        Glide.with(holder.itemView.getContext())
                .load(finalImage)
                .override(Target.SIZE_ORIGINAL)
                .into(holder.imgPopular);

        holder.tvType.setText(popularModel.getType());
        holder.tvTitlePopular.setText(popularModel.getTitle());
        holder.tvDatePopular.setText(popularModel.getUpload_on());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listPopular.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPopular.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPopular;
        TextView tvTitlePopular, tvDatePopular, tvType;

        ListViewHolder(View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tv_card_type);
            imgPopular = itemView.findViewById(R.id.img_card_photo);
            tvTitlePopular = itemView.findViewById(R.id.tv_card_judul);
            tvDatePopular = itemView.findViewById(R.id.tv_card_upload);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(PopularModel data);
    }
}
