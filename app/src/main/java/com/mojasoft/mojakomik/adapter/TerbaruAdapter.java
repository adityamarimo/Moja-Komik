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
import com.mojasoft.mojakomik.model.TerbaruModel;

import java.util.ArrayList;

public class TerbaruAdapter extends RecyclerView.Adapter<TerbaruAdapter.ListViewHolder> {

    private ArrayList<TerbaruModel> listTerbaru;

    public TerbaruAdapter(ArrayList<TerbaruModel> list) {
        this.listTerbaru = list;
    }

    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public void renewItems(ArrayList<TerbaruModel> terbaruModels) {
        this.listTerbaru = terbaruModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TerbaruAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TerbaruAdapter.ListViewHolder holder, final int position) {
        TerbaruModel terbaruModel = listTerbaru.get(position);

        String str = terbaruModel.getThumb();
        String[] arrOfStr = str.split("resize", 2);

        String finalImage = arrOfStr[0];

        Glide.with(holder.itemView.getContext())
                .load(finalImage)
                .override(Target.SIZE_ORIGINAL)
                .into(holder.imgTerbaru);

        holder.tvType.setText(terbaruModel.getType());

        /*
        if (terbaruModel.getType().contains("Manga")) {
            holder.tvType.setTextColor(Color.parseColor("#fa1616"));
        } else if (terbaruModel.getType().contains("Manhwa")) {
            holder.tvType.setTextColor(Color.parseColor("#28df99"));
        } else if (terbaruModel.getType().contains("Manhua")) {
            holder.tvType.setTextColor(Color.parseColor("#9d65c9"));
        }
        */

        holder.tvTitleTerbaru.setText(terbaruModel.getTitle());
        holder.tvChapter.setText(terbaruModel.getChapter());
        holder.tvDateTerbaru.setText(terbaruModel.getUpdated_on());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listTerbaru.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listTerbaru.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView imgTerbaru;
        TextView tvTitleTerbaru, tvDateTerbaru, tvChapter, tvType;

        ListViewHolder(@NonNull View itemView) {
            super(itemView);
            imgTerbaru = itemView.findViewById(R.id.img_item_photo);
            tvType = itemView.findViewById(R.id.tv_item_type);
            tvTitleTerbaru = itemView.findViewById(R.id.tv_item_judul);
            tvDateTerbaru = itemView.findViewById(R.id.tv_item_upload);
            tvChapter = itemView.findViewById(R.id.tv_item_chapter);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(TerbaruModel data);
    }
}
