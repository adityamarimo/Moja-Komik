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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.mojasoft.mojakomik.MainActivity;
import com.mojasoft.mojakomik.R;
import com.mojasoft.mojakomik.adapter.ListManhwaAdapter;
import com.mojasoft.mojakomik.model.ListManhwaModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ListManhwaFragment extends Fragment implements View.OnClickListener {

    private ArrayList<ListManhwaModel> listManhwa = new ArrayList<>();
    private ListManhwaAdapter manhwaAdapter;
    private RecyclerView rvManhwa;
    Button btnBack, btnNext;
    TextView txtPageNumber;

    private int pageNumber;
    private int pageNumberSpinner;
    private ProgressDialog progressDialog;

    private DisplayMetrics displayMetrics;
    private ArrayAdapter<String> spinnerAdapter;
    private Spinner spinner;
    private ImageView iconForward;
    private int statusItems = 0;
    private ArrayList<String> ItemSpinner = new ArrayList<>();

    private MainActivity mActivity;

    public ListManhwaFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_manhwa, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvManhwa = view.findViewById(R.id.rv_Manhwa_fragment);
        rvManhwa.setHasFixedSize(true);

        btnBack = view.findViewById(R.id.btnBackManhwa);
        btnNext = view.findViewById(R.id.btnNextManhwa);
        txtPageNumber = view.findViewById(R.id.txtPageNumberManhwa);

        iconForward = view.findViewById(R.id.iconForwardManhwa);
        iconForward.setOnClickListener(this);

        spinner = view.findViewById(R.id.spinnerManhwa);
        ItemSpinner.add("1");

        //Inisialiasi Array Adapter dengan memasukkan String Array
        spinnerAdapter = new ArrayAdapter<>(mActivity, android.R.layout.simple_spinner_item, ItemSpinner);

        //Memasukan Adapter pada Spinner
        spinner.setAdapter(spinnerAdapter);

        btnNext.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        rvManhwa.setLayoutManager(new LinearLayoutManager(mActivity));
        manhwaAdapter = new ListManhwaAdapter(listManhwa);
        rvManhwa.setAdapter(manhwaAdapter);

        manhwaAdapter.setOnItemClickCallback(new ListManhwaAdapter.OnItemClickCallback() {

            @Override
            public void onItemClicked(ListManhwaModel data) {
                showSelectedManhwa(data);
            }
        });

        displayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;

        rvManhwa.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, screenHeight - 600));

        progressDialog = new ProgressDialog(mActivity);
        progressDialog.setTitle("Mohon Tunggu");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sedang menampilkan data");

        pageNumber = 1;
        pageNumberSpinner = 1;

        checkStatusBtn();

        getListManhwa();
    }

    private void showSelectedManhwa(ListManhwaModel listManhwaModel) {
        Intent moveToDetailActivity = new Intent(mActivity, DetailKomikActivity.class);
        moveToDetailActivity.putExtra(DetailKomikActivity.EXTRA_DATA, listManhwaModel);
        DetailKomikActivity.EXTRA_STATUS = 4;
        startActivity(moveToDetailActivity);
    }

    private void getListManhwa() {
        progressDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        int page = 0;
        if (statusItems == 0) {
            page = pageNumber;
        } else if (statusItems == 1) {
            String temp = spinner.getSelectedItem().toString();
            page = Integer.valueOf(temp);
        }

        final String url = "https://mojakomik-api.herokuapp.com/api/manhwa/" + page;
        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                ArrayList<ListManhwaModel> manhwaModels = new ArrayList<>();
                String result = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("manga_list"));

                    for (int i = 0; i < jsonArray.length(); i++) {
                        ListManhwaModel manhwa = new ListManhwaModel();
                        JSONObject jsonObjectList = jsonArray.getJSONObject(i);
                        String title = jsonObjectList.getString("title");
                        String thumb = jsonObjectList.getString("thumb");
                        String endpoint = jsonObjectList.getString("endpoint");
                        String updated_on = jsonObjectList.getString("updated_on");
                        String chapter = jsonObjectList.getString("chapter");

                        manhwa.setTitle(title);
                        manhwa.setThumb(thumb);
                        manhwa.setEndpoint(endpoint);
                        manhwa.setUpdated_on(updated_on);
                        manhwa.setChapter(chapter);

                        manhwaModels.add(manhwa);
                    }
                    manhwaAdapter.renewItems(manhwaModels);
                    progressDialog.dismiss();
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(mActivity, "Gagal Menampilkan List Manhwa", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(mActivity, "Gagal Menampilkan List Manhwa, error = " + errorMessage, Toast.LENGTH_SHORT).show();
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
        if (view.getId() == R.id.btnNextManhwa) {
            pageNumber++;
            if (statusItems == 0) {
                pageNumberSpinner++;
                ItemSpinner.add(String.valueOf(pageNumberSpinner));
                spinner.setAdapter(spinnerAdapter);
            }

            spinner.setSelection(pageNumber - 1);
            statusItems = 0;

            ArrayList<ListManhwaModel> manhwaModels = new ArrayList<>();
            manhwaAdapter.renewItems(manhwaModels);
            getListManhwa();

            checkStatusBtn();
        } else if (view.getId() == R.id.btnBackManhwa) {
            pageNumber--;
            statusItems = 1;

            ArrayList<ListManhwaModel> manhwaModels = new ArrayList<>();
            manhwaAdapter.renewItems(manhwaModels);
            getListManhwa();

            checkStatusBtn();
        } else if (view.getId() == R.id.iconForwardManhwa) {
            statusItems = 1;
            String getItem = spinner.getSelectedItem().toString();
            String page = Integer.toString(pageNumber);
            if (!getItem.contains(page)) {
                getListManhwa();

                pageNumber = Integer.valueOf(getItem);
                checkStatusBtn();
            }
        }
    }
}