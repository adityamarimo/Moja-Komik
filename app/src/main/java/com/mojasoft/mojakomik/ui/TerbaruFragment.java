package com.mojasoft.mojakomik.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.mojasoft.mojakomik.R;
import com.mojasoft.mojakomik.adapter.TerbaruAdapter;
import com.mojasoft.mojakomik.model.TerbaruModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TerbaruFragment extends Fragment implements View.OnClickListener {

    private ArrayList<TerbaruModel> listTerbaru = new ArrayList<>();
    private TerbaruAdapter terbaruAdapter;
    private RecyclerView rvTerbaru;

    private ProgressDialog progressDialog;
    private Button btnBack, btnNext;
    private TextView txtPageNumber;

    private int pageNumber;
    private int pageNumberSpinner;

    private DisplayMetrics displayMetrics;
    private ArrayAdapter<String> spinnerAdapter;
    private Spinner spinner;
    private ImageView iconForward;
    private int statusItems = 0;

    private ArrayList<String> ItemSpinner = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_new, container, false);

        rvTerbaru = root.findViewById(R.id.rv_new_fragment);
        rvTerbaru.setHasFixedSize(true);

        rvTerbaru.setLayoutManager(new LinearLayoutManager(getActivity()));
        terbaruAdapter = new TerbaruAdapter(listTerbaru);
        rvTerbaru.setAdapter(terbaruAdapter);

        iconForward = root.findViewById(R.id.iconForwardNew);
        iconForward.setOnClickListener(this);

        spinner = root.findViewById(R.id.spinnerNew);
        ItemSpinner.add("1");

        //Inisialiasi Array Adapter dengan memasukkan String Array
        spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, ItemSpinner);

        //Memasukan Adapter pada Spinner
        spinner.setAdapter(spinnerAdapter);

        terbaruAdapter.setOnItemClickCallback(new TerbaruAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(TerbaruModel data) {
                showSelectedTerbaru(data);
            }
        });

        txtPageNumber = root.findViewById(R.id.txtPageNumberNew);
        btnBack = root.findViewById(R.id.btnBackNew);
        btnNext = root.findViewById(R.id.btnNextNew);
        btnBack.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;

        rvTerbaru.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, screenHeight - 500));

        pageNumber = 1;
        pageNumberSpinner = 1;

        checkStatusBtn();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Mohon Tunggu");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sedang menampilkan data");

        getItemsTerbaru();

        return root;
    }

    private void showSelectedTerbaru(TerbaruModel terbaruModel) {
        Intent moveToDetailActivity = new Intent(getActivity(), DetailKomikActivity.class);
        moveToDetailActivity.putExtra(DetailKomikActivity.EXTRA_DATA, terbaruModel);
        DetailKomikActivity.EXTRA_STATUS = 1;
        startActivity(moveToDetailActivity);
    }

    private void getItemsTerbaru() {
        progressDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        int page = 0;
        if (statusItems == 0) {
            page = pageNumber;
        } else if (statusItems == 1) {
            String temp = spinner.getSelectedItem().toString();
            page = Integer.valueOf(temp);
        }

        final String url = "https://mojakomik-api.herokuapp.com/api/manga/page/" + page;
        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                ArrayList<TerbaruModel> terbaruModels = new ArrayList<>();
                String result = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("manga_list"));

                    for (int i = 0; i < jsonArray.length(); i++) {
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
                    terbaruAdapter.renewItems(terbaruModels);
                    progressDialog.dismiss();
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Gagal Menampilkan List Terbaru Komik", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), "Gagal Menampilkan List Terbaru Komik, error = " + errorMessage, Toast.LENGTH_SHORT).show();
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
        if (view.getId() == R.id.btnNextNew) {
            pageNumber++;
            if (statusItems == 0){
                pageNumberSpinner++;
                ItemSpinner.add(String.valueOf(pageNumberSpinner));
                spinner.setAdapter(spinnerAdapter);
            }

            spinner.setSelection(pageNumber - 1);
            statusItems = 0;

            ArrayList<TerbaruModel> terbaruModels = new ArrayList<>();
            terbaruAdapter.renewItems(terbaruModels);
            getItemsTerbaru();

            checkStatusBtn();
        } else if (view.getId() == R.id.btnBackNew) {
            pageNumber--;
            statusItems = 1;

            ArrayList<TerbaruModel> terbaruModels = new ArrayList<>();
            terbaruAdapter.renewItems(terbaruModels);
            getItemsTerbaru();

            checkStatusBtn();
        } else if (view.getId() == R.id.iconForwardNew) {
            statusItems = 1;
            String getItem = spinner.getSelectedItem().toString();
            String page = Integer.toString(pageNumber);
            if (!getItem.contains(page)) {
                getItemsTerbaru();

                pageNumber = Integer.valueOf(getItem);
                checkStatusBtn();
            }
        }
    }
}