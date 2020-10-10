package com.mojasoft.mojakomik.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.mojasoft.mojakomik.R;
import com.mojasoft.mojakomik.adapter.DetailChapterAdapter;
import com.mojasoft.mojakomik.model.ChapterModel;
import com.mojasoft.mojakomik.model.DetailChapterModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class DetailChapterActivity extends AppCompatActivity {

    public static final String EXTRA_DATA = "extra_data";
    private ArrayList<DetailChapterModel> listChapter = new ArrayList<>();
    private DetailChapterAdapter chapterAdapter;
    private RecyclerView rvChapter;

    private TextView txtChapNow;

    ProgressDialog progressDialog;
    private String downloadLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_chapter);

        rvChapter = findViewById(R.id.rv_detail_chapter);
        rvChapter.setHasFixedSize(true);

        rvChapter.setLayoutManager(new LinearLayoutManager(this));
        chapterAdapter = new DetailChapterAdapter(listChapter);
        rvChapter.setAdapter(chapterAdapter);

        progressDialog = new ProgressDialog(DetailChapterActivity.this);
        progressDialog.setTitle("Mohon Tunggu");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Untuk Zoom Gambar Hanya Perlu Tap 2x Saja. Untuk Mendownload Silahkan Klik Icon Pojok Kanan Atas");

        ChapterModel chapterModel = getIntent().getParcelableExtra(EXTRA_DATA);
        getSupportActionBar().setTitle(chapterModel.getChapter_title());

        getListChapter(chapterModel.getChapter_endpoint());
    }

    private void getListChapter(String chapter_endpoint) {
        progressDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        final String url = "http://mojakomik-api.herokuapp.com/api/chapter/" + chapter_endpoint;
        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("chapter_image"));
                    downloadLink = jsonObject.getString("download_link");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        DetailChapterModel chapter = new DetailChapterModel();
                        JSONObject jsonObjectChapter = jsonArray.getJSONObject(i);
                        chapter.setChapter_image_link(jsonObjectChapter.getString("chapter_image_link"));
                        chapter.setImage_number(jsonObjectChapter.getString("image_number"));

                        listChapter.add(chapter);
                    }
                    chapterAdapter.renewItems(listChapter);
                    progressDialog.dismiss();
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(DetailChapterActivity.this, "Gagal Menampilkan Data Detail Chapter", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(DetailChapterActivity.this, "Gagal Menampilkan Data Detail Chapter, error : " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item_chapter, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setMode(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    public void setMode(int selectedMode) {
        switch (selectedMode) {
            case R.id.menuDownload:
                if (downloadLink.contains("https")) {
                    Intent moveToDetailActivity = new Intent(Intent.ACTION_VIEW);
                    moveToDetailActivity.setData(Uri.parse(downloadLink));
                    startActivity(moveToDetailActivity);
                } else {
                    Toast.makeText(DetailChapterActivity.this, "Mohon Maaf Untuk Komik Ini Belum Tersedia Versi PDF", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}