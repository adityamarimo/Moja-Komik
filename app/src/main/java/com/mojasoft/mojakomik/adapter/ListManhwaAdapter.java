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
import com.mojasoft.mojakomik.model.ListManhwaModel;

import java.util.ArrayList;

public class ListManhwaAdapter extends RecyclerView.Adapter<ListManhwaAdapter.ListViewHolder> {

    private ArrayList<ListManhwaModel> listManhwa;

    public ListManhwaAdapter(ArrayList<ListManhwaModel> list) {
        this.listManhwa = list;
    }

    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public void renewItems(ArrayList<ListManhwaModel> manhwaModels) {
        this.listManhwa = manhwaModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListManhwaAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListManhwaAdapter.ListViewHolder holder, final int position) {
        ListManhwaModel listManhwaModel = listManhwa.get(position);

        String str = listManhwaModel.getThumb();
        String[] arrOfStr = str.split("resize", 2);

        String finalImage = arrOfStr[0];

        Glide.with(holder.itemView.getContext())
                .load(finalImage)
                .apply(new RequestOptions().override(180, 130))
                .into(holder.imgManhwa);

        holder.tvTitleManhwa.setText(listManhwaModel.getTitle());
        holder.tvChapter.setText(listManhwaModel.getChapter());
        holder.tvUploadOn.setText(listManhwaModel.getUpdated_on());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listManhwa.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listManhwa.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView imgManhwa;
        TextView tvTitleManhwa, tvUploadOn, tvChapter;
        ListViewHolder(@NonNull View itemView) {
            super(itemView);

            imgManhwa = itemView.findViewById(R.id.img_item_photo);
            tvTitleManhwa = itemView.findViewById(R.id.tv_item_judul);
            tvUploadOn = itemView.findViewById(R.id.tv_item_upload);
            tvChapter = itemView.findViewById(R.id.tv_item_chapter);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(ListManhwaModel data);
    }
}
