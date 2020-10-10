package com.mojasoft.mojakomik.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.mojasoft.mojakomik.R;
import com.mojasoft.mojakomik.adapter.GenreDetailAdapter;
import com.mojasoft.mojakomik.adapter.PopularAdapter;
import com.mojasoft.mojakomik.adapter.RecommendedAdapter;
import com.mojasoft.mojakomik.adapter.TerbaruAdapter;
import com.mojasoft.mojakomik.model.GenreDetailModel;
import com.mojasoft.mojakomik.model.PopularModel;
import com.mojasoft.mojakomik.model.RecommendedModel;
import com.mojasoft.mojakomik.model.TerbaruModel;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class HomeFragment extends Fragment implements View.OnClickListener {

    SliderView sliderView;
    private RecommendedAdapter recommendedAdapter;

    private ArrayList<TerbaruModel> listTerbaru = new ArrayList<>();
    private TerbaruAdapter terbaruAdapter;
    private RecyclerView rvTerbaru;

    private ArrayList<PopularModel> listPopular = new ArrayList<>();
    private PopularAdapter popularAdapter;
    private RecyclerView rvPopular;

    private ArrayList<GenreDetailModel> listGenre = new ArrayList<>();
    private GenreDetailAdapter genreDetailAdapter;
    private RecyclerView rvGenre;

    private TextView textViewFantasy;
    private TextView textViewAction;
    private TextView textViewTerbaru;

    private String statusGenre;
    private ProgressDialog progressDialog;
    private SearchView searchView;
    private ProgressBar progressBarGenre, progressBarTerbaru;
    private ImageView refreshTerbaru;

    //EKSPERIMEN
    DisplayMetrics displayMetrics;
    //END

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Moja Komik");

        textViewAction = root.findViewById(R.id.choose_action);
        textViewFantasy = root.findViewById(R.id.choose_fantasy);
        textViewAction.setOnClickListener(this);
        textViewFantasy.setOnClickListener(this);

        sliderView = root.findViewById(R.id.imageSlider);
        searchView = root.findViewById(R.id.searchMain);

        progressBarGenre = root.findViewById(R.id.progressBarGenre);
        progressBarTerbaru = root.findViewById(R.id.progressBarTerbaru);
        progressBarTerbaru.setVisibility(View.INVISIBLE);
        textViewTerbaru = root.findViewById(R.id.txtTerbaru);

        refreshTerbaru = root.findViewById(R.id.refreshTerbaru);
        refreshTerbaru.setOnClickListener(this);

        rvTerbaru = root.findViewById(R.id.rv_terbaru);
        rvTerbaru.setHasFixedSize(true);

        rvPopular = root.findViewById(R.id.rv_popular);
        rvPopular.setHasFixedSize(true);

        rvGenre = root.findViewById(R.id.rv_genre_main);
        rvGenre.setHasFixedSize(true);

        displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;

        searchView.setLayoutParams(new LinearLayout.LayoutParams(screenWidth - 150,100));
        rvTerbaru.setNestedScrollingEnabled(false);

        recommendedAdapter = new RecommendedAdapter(getActivity());
        sliderView.setSliderAdapter(recommendedAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.CUBEINROTATIONTRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

        recommendedAdapter.setOnItemClickCallback(new RecommendedAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(RecommendedModel data) {
                showSelectedRecomended(data);
            }
        });

        rvTerbaru.setLayoutManager(new LinearLayoutManager(getActivity()));
        terbaruAdapter = new TerbaruAdapter(listTerbaru);
        rvTerbaru.setAdapter(terbaruAdapter);

        rvPopular.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        popularAdapter = new PopularAdapter(listPopular);
        rvPopular.setAdapter(popularAdapter);

        rvGenre.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        genreDetailAdapter = new GenreDetailAdapter(listGenre);
        rvGenre.setAdapter(genreDetailAdapter);

        terbaruAdapter.setOnItemClickCallback(new TerbaruAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(TerbaruModel data) {
                showSelectedTerbaru(data);
            }
        });

        popularAdapter.setOnItemClickCallback(new PopularAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(PopularModel data) {
                showSelectedPopular(data);
            }
        });

        genreDetailAdapter.setOnItemClickCallback(new GenreDetailAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(GenreDetailModel data) {
                showSelectedGenre(data);
            }
        });

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Mohon Tunggu");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sedang menampilkan data");

        getItemsRecommended();
        getItemsTerbaru();
        getItemsPopular();

        statusGenre = "Action";
        getItemsGenre();
        Search();

        return root;
    }

    public void getItemsRecommended() {
        progressDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        final String url = "https://mojakomik-api.herokuapp.com/api/recommended";
        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                List<RecommendedModel> recomendedModels = new ArrayList<>();
                String result = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("manga_list"));

                    for (int i = 0; i < jsonArray.length() * 2 / 3; i++) {
                        RecommendedModel recomendedModel = new RecommendedModel();
                        JSONObject jsonObjectList = jsonArray.getJSONObject(i);
                        String title = jsonObjectList.getString("title");
                        String thumb = jsonObjectList.getString("thumb");
                        String endpoint = jsonObjectList.getString("endpoint");
                        recomendedModel.setTitle(title);
                        recomendedModel.setThumb(thumb);
                        recomendedModel.setEndpoint(endpoint);

                        recomendedModels.add(recomendedModel);
                    }
                    recommendedAdapter.renewItems(recomendedModels);
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Gagal Menampilkan Data Recommended", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), "Gagal Menampilkan Data Recommended, error : " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getItemsTerbaru() {
        refreshTerbaru.setEnabled(false);
        textViewTerbaru.setText("Terbaru");
        AsyncHttpClient client = new AsyncHttpClient();
        final String url = "https://mojakomik-api.herokuapp.com/api/manga/page/1";
        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                ArrayList<TerbaruModel> terbaruModels = new ArrayList<>();
                String result = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("manga_list"));

                    for (int i = 0; i < 5; i++) {
                        TerbaruModel terbaru = new TerbaruModel();
                        JSONObject jsonObjectList = jsonArray.getJSONObject(i);
                        String title = jsonObjectList.getString("title");
                        String thumb = jsonObjectList.getString("thumb");
                        String endpoint = jsonObjectList.getString("endpoint");
                        String type = jsonObjectList.getString("type");
                        String updated_on = jsonObjectList.getString("updated_on");
                        String chapter = jsonObjectList.getString("chapter");

                        terbaru.setTitle(title);
                        terbaru.setThumb(thumb);
                        terbaru.setEndpoint(endpoint);
                        terbaru.setType(type);
                        terbaru.setUpdated_on(updated_on);
                        terbaru.setChapter(chapter);

                        terbaruModels.add(terbaru);
                    }
                    progressBarTerbaru.setVisibility(View.INVISIBLE);
                    terbaruAdapter.renewItems(terbaruModels);
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Gagal Menampilkan Data Terbaru", Toast.LENGTH_SHORT).show();
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
                progressBarGenre.setVisibility(View.INVISIBLE);
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Gagal Menampilkan Data Terbaru, error : " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getItemsPopular() {
        AsyncHttpClient client = new AsyncHttpClient();
        final String url = "https://mojakomik-api.herokuapp.com/api/manga/popular/1";
        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                ArrayList<PopularModel> popularModels = new ArrayList<>();
                String result = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("manga_list"));

                    for (int i = 0; i < jsonArray.length() / 2; i++) {
                        PopularModel popular = new PopularModel();
                        JSONObject jsonObjectList = jsonArray.getJSONObject(i);
                        String title = jsonObjectList.getString("title");
                        String thumb = jsonObjectList.getString("thumb");
                        String endpoint = jsonObjectList.getString("endpoint");
                        String type = jsonObjectList.getString("type");
                        String upload_on = jsonObjectList.getString("upload_on");
                        popular.setTitle(title);
                        popular.setThumb(thumb);
                        popular.setEndpoint(endpoint);
                        popular.setType(type);
                        popular.setUpload_on(upload_on);

                        popularModels.add(popular);
                    }
                    popularAdapter.renewItems(popularModels);
                    progressDialog.dismiss();
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Gagal Menampilkan Data Popular", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), "Gagal Menampilkan Data Popular, error : " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getItemsGenre() {
        AsyncHttpClient client = new AsyncHttpClient();
        final String url = "https://mojakomik-api.herokuapp.com/api/genres/" + statusGenre + "/1";
        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                ArrayList<GenreDetailModel> genreDetailModels = new ArrayList<>();
                String result = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("manga_list"));

                    for (int i = 0; i < jsonArray.length() / 2; i++) {
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
                    genreDetailAdapter.renewItems(genreDetailModels);
                    progressBarGenre.setVisibility(View.INVISIBLE);
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Gagal Menampilkan Data Genre", Toast.LENGTH_SHORT).show();
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
                progressBarGenre.setVisibility(View.INVISIBLE);
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Gagal Menampilkan Data Genre, error : " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Search() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.isEmpty()) {
                    return false;
                } else {
                    rvTerbaru.setNestedScrollingEnabled(true);
                    ArrayList<TerbaruModel> terbaruModels = new ArrayList<>();
                    terbaruAdapter.renewItems(terbaruModels);
                    textViewTerbaru.setText("Pencarian Keyword ' " + query + " '");
                    getItemsSearch(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                return false;
            }
        });
    }

    private void getItemsSearch(String query) {
        refreshTerbaru.setEnabled(true);
        progressBarTerbaru.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        final String url = "https://mojakomik-api.herokuapp.com/api/cari/" + query;
        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                ArrayList<TerbaruModel> terbaruModels = new ArrayList<>();
                String result = new String(responseBody);
                try {
                    JSONArray jsonArray = new JSONArray(result);

                    if (jsonArray.length() == 0) {
                        progressBarTerbaru.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity(), "Komik Yang Dicari Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            TerbaruModel terbaru = new TerbaruModel();
                            JSONObject jsonObjectList = jsonArray.getJSONObject(i);
                            String title = jsonObjectList.getString("title");
                            String thumb = jsonObjectList.getString("thumb");
                            String endpoint = jsonObjectList.getString("endpoint");
                            String type = jsonObjectList.getString("type");
                            String updated_on = jsonObjectList.getString("updated_on");

                            terbaru.setTitle(title);
                            terbaru.setThumb(thumb);
                            terbaru.setEndpoint(endpoint);
                            terbaru.setType(type);
                            terbaru.setUpdated_on(updated_on);

                            terbaruModels.add(terbaru);
                        }
                        progressBarTerbaru.setVisibility(View.INVISIBLE);
                        terbaruAdapter.renewItems(terbaruModels);
                    }
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Gagal Menampilkan Data Pencarian", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), "Gagal Menampilkan Data Pencarian, error : " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showSelectedRecomended(RecommendedModel recomendedModel) {
        Intent moveToDetailActivity = new Intent(getActivity(), DetailKomikActivity.class);
        moveToDetailActivity.putExtra(DetailKomikActivity.EXTRA_DATA, recomendedModel);
        DetailKomikActivity.EXTRA_STATUS = 0;
        startActivity(moveToDetailActivity);
    }

    private void showSelectedTerbaru(TerbaruModel terbaruModel) {
        Intent moveToDetailActivity = new Intent(getActivity(), DetailKomikActivity.class);
        moveToDetailActivity.putExtra(DetailKomikActivity.EXTRA_DATA, terbaruModel);
        DetailKomikActivity.EXTRA_STATUS = 1;
        startActivity(moveToDetailActivity);
    }

    private void showSelectedPopular(PopularModel popularModel) {
        Intent moveToDetailActivity = new Intent(getActivity(), DetailKomikActivity.class);
        moveToDetailActivity.putExtra(DetailKomikActivity.EXTRA_DATA, popularModel);
        DetailKomikActivity.EXTRA_STATUS = 2;
        startActivity(moveToDetailActivity);
    }

    private void showSelectedGenre(GenreDetailModel genreDetailModel) {
        Intent moveToDetailActivity = new Intent(getActivity(), DetailKomikActivity.class);
        moveToDetailActivity.putExtra(DetailKomikActivity.EXTRA_DATA, genreDetailModel);
        DetailKomikActivity.EXTRA_STATUS = 3;
        startActivity(moveToDetailActivity);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ResourceType")
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.choose_action) {
            progressBarGenre.setVisibility(View.VISIBLE);
            textViewFantasy.setBackgroundColor(android.R.color.transparent);
            textViewFantasy.setTextColor(Color.parseColor("#ff000000"));

            textViewAction.setBackground(getResources().getDrawable(R.drawable.bg_genre_main));
            textViewAction.setTextColor(Color.parseColor("#ffffffff"));

            statusGenre = "Action";
            ArrayList<GenreDetailModel> genreDetailModels = new ArrayList<>();
            genreDetailAdapter.renewItems(genreDetailModels);
            getItemsGenre();
        } else if (view.getId() == R.id.choose_fantasy) {
            progressBarGenre.setVisibility(View.VISIBLE);
            textViewAction.setBackgroundColor(android.R.color.transparent);
            textViewAction.setTextColor(Color.parseColor("#ff000000"));

            textViewFantasy.setBackground(getResources().getDrawable(R.drawable.bg_genre_main));
            textViewFantasy.setTextColor(Color.parseColor("#ffffffff"));

            statusGenre = "Fantasy";
            ArrayList<GenreDetailModel> genreDetailModels = new ArrayList<>();
            genreDetailAdapter.renewItems(genreDetailModels);
            getItemsGenre();
        } else if (view.getId() == R.id.refreshTerbaru) {
            rvTerbaru.setNestedScrollingEnabled(false);
            searchView.setQuery("", false);
            searchView.setIconified(true);
            progressBarTerbaru.setVisibility(View.VISIBLE);
            ArrayList<TerbaruModel> terbaruModels = new ArrayList<>();
            terbaruAdapter.renewItems(terbaruModels);
            getItemsTerbaru();
        }
    }
}