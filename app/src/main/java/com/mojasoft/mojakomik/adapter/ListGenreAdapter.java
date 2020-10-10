package com.mojasoft.mojakomik.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mojasoft.mojakomik.R;
import com.mojasoft.mojakomik.model.ListGenreModel;

import java.util.ArrayList;
import java.util.List;

public class ListGenreAdapter extends RecyclerView.Adapter<ListGenreAdapter.ListViewHolder> {

    private List<ListGenreModel> listGenreModel;

    public ListGenreAdapter(ArrayList<ListGenreModel> list) {
        this.listGenreModel = list;
    }

    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public void renewItems(List<ListGenreModel> listGenreModels) {
        this.listGenreModel = listGenreModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListGenreAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_genre, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListGenreAdapter.ListViewHolder holder, final int position) {
        final ListGenreModel listGenre = listGenreModel.get(position);

        holder.tvGenreTitle.setText(listGenre.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listGenreModel.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listGenreModel.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        TextView tvGenreTitle;

        ListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGenreTitle = itemView.findViewById(R.id.tv_genre_title);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(ListGenreModel data);
    }
}
