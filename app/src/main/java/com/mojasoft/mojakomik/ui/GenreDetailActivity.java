package com.mojasoft.mojakomik.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.mojasoft.mojakomik.R;
import com.mojasoft.mojakomik.adapter.GenreRowDetailAdapter;
import com.mojasoft.mojakomik.model.GenreDetailModel;
import com.mojasoft.mojakomik.model.ListGenreModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class GenreDetailActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_DATA = "extra_data";
    private ArrayList<GenreDetailModel> listGenre = new ArrayList<>();
    private GenreRowDetailAdapter genreRowDetailAdapter;
    private RecyclerView rvGenre;

    private ProgressDialog progressDialog;
    private Button btnBack, btnNext;
    private TextView txtPageNumber;

    private String genre = "";
    private int pageNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvGenre = findViewById(R.id.rv_Genre_fragment);
        rvGenre.setHasFixedSize(true);

        rvGenre.setLayoutManager(new LinearLayoutManager(this));
        genreRowDetailAdapter = new GenreRowDetailAdapter(listGenre);
        rvGenre.setAdapter(genreRowDetailAdapter);

        genreRowDetailAdapter.setOnItemClickCallback(new GenreRowDetailAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(GenreDetailModel data) {
                showSelectedGenre(data);
            }
        });

        txtPageNumber = findViewById(R.id.txtPageNumberGenre);
        btnBack = findViewById(R.id.btnBackGenre);
        btnNext = findViewById(R.id.btnNextGenre);
        btnBack.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        pageNumber = 1;

        checkStatusBtn();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mohon Tunggu");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sedang menampilkan data");

        ListGenreModel listGenreModel  = getIntent().getParcelableExtra(EXTRA_DATA);
        genre = listGenreModel.getEndpoint();
        getSupportActionBar().setTitle(listGenreModel.getTitle());
        getListGenre();
    }

    private void showSelectedGenre(GenreDetailModel genreDetailModel) {
        Intent moveToDetailActivity = new Intent(GenreDetailActivity.this, DetailKomikActivity.class);
        moveToDetailActivity.putExtra(DetailKomikActivity.EXTRA_DATA, genreDetailModel);
        DetailKomikActivity.EXTRA_STATUS = 6;
        startActivity(moveToDetailActivity);
    }

    private void getListGenre() {
        progressDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();

        final String url = "https://mojakomik-api.herokuapp.com/api/genres/" + genre + pageNumber;
        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                ArrayList<GenreDetailModel> genreDetailModels = new ArrayList<>();
                String result = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("manga_list"));

                    for (int i = 0; i < jsonArray.length(); i++) {
                        GenreDetailModel genre = new GenreDetailModel();
                        JSONObject jsonObjectList = jsonArray.getJSONObject(i);
                        String title = jsonObjectList.getString("title");
                        String thumb = jsonObjectList.getString("thumb");
                        String endpoint = jsonObjectList.getString("endpoint");
                        String type = jsonObjectList.getString("type");
                        genre.setTitle(title);
                        genre.setThumb(thumb);
                        genre.setEndpoint(endpoint);
                        genre.setType(type);

                        genreDetailModels.add(genre);
                    }
                    genreRowDetailAdapter.renewItems(genreDetailModels);
                    progressDialog.dismiss();
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(GenreDetailActivity.this, "Gagal Menampilkan Data Genre", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(GenreDetailActivity.this, "Gagal Menampilkan Data Genre, error : " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkStatusBtn() {
        if (pageNumber == 1) {
            btnBack.setVisibility(View.INVISIBLE);
            txtPageNumber.setText("\t" + pageNumber + "\t");
        } else {
            btnBack.setVisibility(View.VISIBLE);
            txtPageNumber.setText("\t" + pageNumber + "\t");
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnNextGenre) {
            pageNumber++;

            ArrayList<GenreDetailModel> genreDetailModels = new ArrayList<>();
            genreRowDetailAdapter.renewItems(genreDetailModels);
            getListGenre();

            checkStatusBtn();
        } else if (view.getId() == R.id.btnBackGenre) {
            pageNumber--;

            ArrayList<GenreDetailModel> genreDetailModels = new ArrayList<>();
            genreRowDetailAdapter.renewItems(genreDetailModels);
            getListGenre();

            checkStatusBtn();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}