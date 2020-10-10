package com.mojasoft.mojakomik.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mojasoft.mojakomik.MainActivity;
import com.mojasoft.mojakomik.R;
import com.mojasoft.mojakomik.model.ListGenreModel;

import java.util.ArrayList;
import java.util.List;

public class CircleGenreAdapter extends RecyclerView.Adapter<CircleGenreAdapter.ListViewHolder> {

    private List<ListGenreModel> listGenreModel;

    public CircleGenreAdapter(ArrayList<ListGenreModel> list) {
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

    private static int[] iconImages = {
            R.drawable.icon_action,
            R.drawable.icon_adventure,
            R.drawable.icon_comedy,
            R.drawable.icon_drama,
            R.drawable.icon_fantasy,
            R.drawable.icon_horror,
            R.drawable.icon_isekai,
            R.drawable.icon_mistery,
            R.drawable.icon_romance,
            R.drawable.icon_seinen,
            R.drawable.icon_shoujo,
            R.drawable.icon_shounen,
            R.drawable.icon_slice,
            R.drawable.icon_sports
    };

    @NonNull
    @Override
    public CircleGenreAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_circle_genre, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CircleGenreAdapter.ListViewHolder holder, final int position) {
        final ListGenreModel listGenre = listGenreModel.get(position);

        holder.tvGenreTitle.setText(listGenre.getTitle());
        Log.d(MainActivity.class.getSimpleName(), "Test Position : " + position + ", Test Index : " + iconImages[1]);

        Glide.with(holder.itemView.getContext())
                .load(iconImages[position])
                .apply(new RequestOptions().override(180, 130))
                .into(holder.imgIcon);

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
        ImageView imgIcon;

        ListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGenreTitle = itemView.findViewById(R.id.tv_circle_titleGenre);
            imgIcon = itemView.findViewById(R.id.imgIconGenre);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(ListGenreModel data);
    }
}
