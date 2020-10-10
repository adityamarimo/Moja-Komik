package com.mojasoft.mojakomik.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.mojasoft.mojakomik.R;
import com.mojasoft.mojakomik.adapter.GridListGenreAdapter;
import com.mojasoft.mojakomik.model.ListGenreModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class GridListGenreFragment extends Fragment {

    private ArrayList<ListGenreModel> listGenreModel = new ArrayList<>();
    private GridListGenreAdapter genreAdapter;
    private RecyclerView rvGenre;
    private ProgressDialog progressDialog;

    public GridListGenreFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_grid_list_genre, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvGenre = view.findViewById(R.id.rv_genre_grid);
        rvGenre.setHasFixedSize(true);

        rvGenre.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        genreAdapter = new GridListGenreAdapter(listGenreModel);
        rvGenre.setAdapter(genreAdapter);

        genreAdapter.setOnItemClickCallback(new GridListGenreAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(ListGenreModel data) {
                showSelectedGenre(data);
            }
        });

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Mohon Tunggu");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sedang menampilkan data");

        getListGenre();
    }

    private void showSelectedGenre(ListGenreModel listGenreModel) {
        Intent moveToDetailActivity = new Intent(getActivity(), GenreDetailActivity.class);
        moveToDetailActivity.putExtra(GenreDetailActivity.EXTRA_DATA, listGenreModel);
        startActivity(moveToDetailActivity);
    }

    private void getListGenre() {
        progressDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        final String url = "https://mojakomik-api.herokuapp.com/api/genres";
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("list_genre"));

                    for (int i = 0; i < jsonArray.length(); i++) {
                        ListGenreModel genreModel = new ListGenreModel();
                        JSONObject jsonObjectGenre = jsonArray.getJSONObject(i);

                        if (!jsonObjectGenre.getString("title").contains("Action")
                                && !jsonObjectGenre.getString("title").contains("Adventure")
                                && !jsonObjectGenre.getString("title").contains("Comedy")
                                && !jsonObjectGenre.getString("title").contains("Drama")
                                && !jsonObjectGenre.getString("title").contains("Fantasy")
                                && !jsonObjectGenre.getString("title").contains("Horror")
                                && !jsonObjectGenre.getString("title").contains("Isekai")
                                && !jsonObjectGenre.getString("title").contains("Mystery")
                                && !jsonObjectGenre.getString("title").contains("Romance")
                                && !jsonObjectGenre.getString("title").contains("Seinen")
                                && !jsonObjectGenre.getString("endpoint").contains("shoujo/")
                                && !jsonObjectGenre.getString("title").contains("Shounen")
                                && !jsonObjectGenre.getString("title").contains("Slice of Life")
                                && !jsonObjectGenre.getString("title").contains("Sports")
                                && !jsonObjectGenre.getString("title").contains("Manhua")
                                && !jsonObjectGenre.getString("endpoint").contains("https")) {
                            genreModel.setTitle(jsonObjectGenre.getString("title"));
                            genreModel.setEndpoint(jsonObjectGenre.getString("endpoint"));

                            listGenreModel.add(genreModel);
                        }
                    }
                    genreAdapter.renewItems(listGenreModel);
                    progressDialog.dismiss();
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Gagal Menampilkan List Genre Komik", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String errorMessage;
                switch (statusCode) {
                    case 401:
                        errorMessage = statusCode + " : Bad Request";
                        break;
                    case 403:
                        errorMessage = statusCode + " : Forbiden";
                        break;
                    case 404:
                        errorMessage = statusCode + " : Not Found";
                        break;
                    default:
                        errorMessage = statusCode + " : " + error.getMessage();
                        break;
                }
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Gagal Menampilkan List Genre Komik, error = " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}