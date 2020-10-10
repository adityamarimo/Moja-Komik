package com.mojasoft.mojakomik.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.mojasoft.mojakomik.R;
import com.mojasoft.mojakomik.adapter.ChapterAdapter;
import com.mojasoft.mojakomik.adapter.ListGenreAdapter;
import com.mojasoft.mojakomik.model.ChapterModel;
import com.mojasoft.mojakomik.model.GenreDetailModel;
import com.mojasoft.mojakomik.model.ListGenreModel;
import com.mojasoft.mojakomik.model.ListManhuaModel;
import com.mojasoft.mojakomik.model.ListManhwaModel;
import com.mojasoft.mojakomik.model.PopularModel;
import com.mojasoft.mojakomik.model.RecommendedModel;
import com.mojasoft.mojakomik.model.TerbaruModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class DetailKomikActivity extends AppCompatActivity {
    public static final String EXTRA_DATA = "extra_data";
    public static int EXTRA_STATUS = 0;

    TextView tvJudul, tvAuthor, tvSinopsis, tvStatus, tvType;
    ImageView imgPoster, imgThumb;
    private ProgressDialog progressDialog;

    private ArrayList<ListGenreModel> listGenreModel = new ArrayList<>();
    private ListGenreAdapter genreAdapter;
    private RecyclerView rvGenre;

    private ArrayList<ChapterModel> listChapter = new ArrayList<>();
    private ChapterAdapter chapterAdapter;
    private RecyclerView rvChapter;

    private String title;
    private String temp_sinopsis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_komik);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvGenre = findViewById(R.id.rv_genre_detail);
        rvGenre.setHasFixedSize(true);

        rvChapter = findViewById(R.id.rv_chapter_detail);
        rvChapter.setHasFixedSize(true);

        rvChapter.setLayoutManager(new LinearLayoutManager(this));
        chapterAdapter = new ChapterAdapter(listChapter);
        rvChapter.setAdapter(chapterAdapter);

        rvGenre.setLayoutManager(new GridLayoutManager(this, 3));
        genreAdapter = new ListGenreAdapter(listGenreModel);
        rvGenre.setAdapter(genreAdapter);

        genreAdapter.setOnItemClickCallback(new ListGenreAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(ListGenreModel data) {
                showSelectedListGenre(data);
            }
        });

        chapterAdapter.setOnItemClickCallback(new ChapterAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(ChapterModel data) {
                showSelectedChapter(data);
            }
        });

        progressDialog = new ProgressDialog(DetailKomikActivity.this);
        progressDialog.setTitle("Mohon Tunggu");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sedang menampilkan data");

        setComponent();
        setData();
    }

    private void showSelectedChapter(ChapterModel chapterModel) {
        Intent moveToDetailActivity = new Intent(DetailKomikActivity.this, DetailChapterActivity.class);
        moveToDetailActivity.putExtra(DetailChapterActivity.EXTRA_DATA, chapterModel);
        startActivity(moveToDetailActivity);
    }

    private void showSelectedListGenre(ListGenreModel listGenreModel) {
        Toast.makeText(DetailKomikActivity.this, listGenreModel.getTitle(), Toast.LENGTH_SHORT).show();
    }

    private void setComponent() {
        tvJudul = findViewById(R.id.DetailJudul);

        tvJudul.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tvJudul.setSelected(true);
        tvJudul.setSingleLine(true);

        tvAuthor = findViewById(R.id.DetailAuthor);
        tvSinopsis = findViewById(R.id.DetailSinopsis);
        tvStatus = findViewById(R.id.DetailStatus);
        tvType = findViewById(R.id.DetailType);
        imgPoster = findViewById(R.id.imgDetailPoster);
        imgThumb = findViewById(R.id.imgDetailThumb);
    }

    private void setData() {
        if (EXTRA_STATUS == 0) {
            RecommendedModel recommendedModel = getIntent().getParcelableExtra(EXTRA_DATA);

            String str = recommendedModel.getThumb();
            String[] arrOfStr = str.split("resize", 2);
            String finalImage = arrOfStr[0];

            getDetailKomik(recommendedModel.getEndpoint(), finalImage);
        } else if (EXTRA_STATUS == 1) {
            TerbaruModel terbaruModel = getIntent().getParcelableExtra(EXTRA_DATA);

            String str = terbaruModel.getThumb();
            String[] arrOfStr = str.split("resize", 2);
            String finalImage = arrOfStr[0];

            getDetailKomik(terbaruModel.getEndpoint(), finalImage);
        } else if (EXTRA_STATUS == 2) {
            PopularModel popularModel = getIntent().getParcelableExtra(EXTRA_DATA);

            String str = popularModel.getThumb();
            String[] arrOfStr = str.split("resize", 2);
            String finalImage = arrOfStr[0];

            getDetailKomik(popularModel.getEndpoint(), finalImage);
        } else if (EXTRA_STATUS == 3) {
            GenreDetailModel genreDetailModel = getIntent().getParcelableExtra(EXTRA_DATA);

            String str = genreDetailModel.getThumb();
            String[] arrOfStr = str.split("resize", 2);
            String finalImage = arrOfStr[0];

            getDetailKomik(genreDetailModel.getEndpoint(), finalImage);
        } else if (EXTRA_STATUS == 4) {
            ListManhwaModel manhwaModel = getIntent().getParcelableExtra(EXTRA_DATA);

            String str = manhwaModel.getThumb();
            String[] arrOfStr = str.split("resize", 2);
            String finalImage = arrOfStr[0];

            getDetailKomik(manhwaModel.getEndpoint(), finalImage);
        } else if (EXTRA_STATUS == 5) {
            ListManhuaModel manhuaModel = getIntent().getParcelableExtra(EXTRA_DATA);

            String str = manhuaModel.getThumb();
            String[] arrOfStr = str.split("resize", 2);
            String finalImage = arrOfStr[0];

            getDetailKomik(manhuaModel.getEndpoint(), finalImage);
        } else if (EXTRA_STATUS == 6) {
            GenreDetailModel genreDetailModel = getIntent().getParcelableExtra(EXTRA_DATA);

            String str = genreDetailModel.getThumb();
            String[] arrOfStr = str.split("resize", 2);
            String finalImage = arrOfStr[0];

            getDetailKomik(genreDetailModel.getEndpoint(), finalImage);
        }
    }

    private void getDetailKomik(String endpoint, final String finalImage) {
        progressDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        final String url = "https://mojakomik-api.herokuapp.com/api/manga/detail/" + endpoint;
        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Glide.with(DetailKomikActivity.this)
                            .load(finalImage)
                            .override(Target.SIZE_ORIGINAL)
                            .into(imgThumb);

                    String str = jsonObject.getString("thumb");
                    String[] arrOfStr = str.split("w=", 2);
                    String finalImagePoster = arrOfStr[0];

                    Glide.with(DetailKomikActivity.this)
                            .load(finalImagePoster)
                            .override(Target.SIZE_ORIGINAL)
                            .into(imgPoster);
                    title = jsonObject.getString("title");
                    tvJudul.setText(title);
                    getSupportActionBar().setTitle(tvJudul.getText());
                    tvType.setText("Type : " + jsonObject.getString("type"));
                    tvAuthor.setText(jsonObject.getString("author"));
                    temp_sinopsis = jsonObject.getString("synopsis");
                    tvSinopsis.setText(temp_sinopsis);
                    tvStatus.setText("Status : " + jsonObject.getString("status"));

                    JSONArray jsonArrayGenre = jsonObject.getJSONArray("genre_list");
                    JSONArray jsonArrayChapter = jsonObject.getJSONArray("chapter");

                    for (int i = 0; i < jsonArrayGenre.length(); i++) {
                        ListGenreModel listGenreModel = new ListGenreModel();
                        JSONObject jsonObjectGenre = jsonArrayGenre.getJSONObject(i);
                        listGenreModel.setTitle(jsonObjectGenre.getString("genre_name"));

                        DetailKomikActivity.this.listGenreModel.add(listGenreModel);
                    }

                    for (int i = 0; i < jsonArrayChapter.length(); i++) {
                        ChapterModel chapterModel = new ChapterModel();
                        JSONObject jsonObjectChapter = jsonArrayChapter.getJSONObject(i);
                        chapterModel.setChapter_title(jsonObjectChapter.getString("chapter_title"));
                        chapterModel.setChapter_endpoint(jsonObjectChapter.getString("chapter_endpoint"));

                        listChapter.add(chapterModel);
                    }

                    genreAdapter.renewItems(listGenreModel);
                    chapterAdapter.renewItems(listChapter);
                    progressDialog.dismiss();
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(DetailKomikActivity.this, "Gagal Menampilkan Data Detail Komik", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(DetailKomikActivity.this, "Gagal Menampilkan Data Detail Komik, error : " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void TestChapter(View view) {
        DetailSinopsisFragment mOptionDialogFragment = new DetailSinopsisFragment();

        Bundle bundle = new Bundle();
        bundle.putString("sinopsis", temp_sinopsis);
        mOptionDialogFragment.setArguments(bundle);

        FragmentManager mFragmentManager = getSupportFragmentManager();
        mOptionDialogFragment.show(mFragmentManager, DetailSinopsisFragment.class.getSimpleName());
    }

}