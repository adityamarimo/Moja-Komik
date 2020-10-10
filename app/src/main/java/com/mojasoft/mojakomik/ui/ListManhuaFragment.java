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
import com.mojasoft.mojakomik.adapter.ListManhuaAdapter;
import com.mojasoft.mojakomik.model.ListManhuaModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class ListManhuaFragment extends Fragment implements View.OnClickListener {

    private ArrayList<ListManhuaModel> listManhua = new ArrayList<>();
    private ListManhuaAdapter manhuaAdapter;
    private RecyclerView rvManhua;
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

    public ListManhuaFragment() {
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvManhua = view.findViewById(R.id.rv_Manhua_fragment);
        rvManhua.setHasFixedSize(true);

        btnBack = view.findViewById(R.id.btnBackManhua);
        btnNext = view.findViewById(R.id.btnNextManhua);
        txtPageNumber = view.findViewById(R.id.txtPageNumberManhua);

        iconForward = view.findViewById(R.id.iconForwardManhua);
        iconForward.setOnClickListener(this);

        spinner = view.findViewById(R.id.spinnerManhua);
        ItemSpinner.add("1");

        //Inisialiasi Array Adapter dengan memasukkan String Array
        spinnerAdapter = new ArrayAdapter<>(mActivity, android.R.layout.simple_spinner_item, ItemSpinner);

        //Memasukan Adapter pada Spinner
        spinner.setAdapter(spinnerAdapter);

        btnNext.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        rvManhua.setLayoutManager(new LinearLayoutManager(mActivity));
        manhuaAdapter = new ListManhuaAdapter(listManhua);
        rvManhua.setAdapter(manhuaAdapter);

        manhuaAdapter.setOnItemClickCallback(new ListManhuaAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(ListManhuaModel data) {
                showSelectedManhua(data);
            }
        });

        displayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;

        rvManhua.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, screenHeight - 600));

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Mohon Tunggu");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sedang menampilkan data");

        pageNumber = 1;

        checkStatusBtn();

        getListManhua();
    }

    private void showSelectedManhua(ListManhuaModel listManhuaModel) {
        Intent moveToDetailActivity = new Intent(mActivity, DetailKomikActivity.class);
        moveToDetailActivity.putExtra(DetailKomikActivity.EXTRA_DATA, listManhuaModel);
        DetailKomikActivity.EXTRA_STATUS = 5;
        startActivity(moveToDetailActivity);
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

    private void getListManhua() {
        progressDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        int page = 0;
        if (statusItems == 0) {
            page = pageNumber;
        } else if (statusItems == 1) {
            String temp = spinner.getSelectedItem().toString();
            page = Integer.valueOf(temp);
        }

        final String url = "https://mojakomik-api.herokuapp.com/api/manhua/" + page;
        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                ArrayList<ListManhuaModel> manhuaModels = new ArrayList<>();
                String result = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("manga_list"));

                    for (int i = 0; i < jsonArray.length(); i++) {
                        ListManhuaModel manhua = new ListManhuaModel();
                        JSONObject jsonObjectList = jsonArray.getJSONObject(i);
                        String title = jsonObjectList.getString("title");
                        String thumb = jsonObjectList.getString("thumb");
                        String endpoint = jsonObjectList.getString("endpoint");
                        String updated_on = jsonObjectList.getString("updated_on");
                        String chapter = jsonObjectList.getString("chapter");

                        manhua.setTitle(title);
                        manhua.setThumb(thumb);
                        manhua.setEndpoint(endpoint);
                        manhua.setUpdated_on(updated_on);
                        manhua.setChapter(chapter);

                        manhuaModels.add(manhua);
                    }
                    manhuaAdapter.renewItems(manhuaModels);
                    progressDialog.dismiss();
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(mActivity, "Gagal Menampilkan List Manhua", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(mActivity, "Gagal Menampilkan List Manhua, error = " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnNextManhua) {
            pageNumber++;
            if (statusItems == 0) {
                pageNumberSpinner++;
                ItemSpinner.add(String.valueOf(pageNumberSpinner));
                spinner.setAdapter(spinnerAdapter);
            }

            spinner.setSelection(pageNumber - 1);
            statusItems = 0;

            ArrayList<ListManhuaModel> manhuaModels = new ArrayList<>();
            manhuaAdapter.renewItems(manhuaModels);
            getListManhua();

            checkStatusBtn();
        } else if (view.getId() == R.id.btnBackManhua) {
            pageNumber--;
            statusItems = 1;

            ArrayList<ListManhuaModel> manhuaModels = new ArrayList<>();
            manhuaAdapter.renewItems(manhuaModels);
            getListManhua();

            checkStatusBtn();
        }else if (view.getId() == R.id.iconForwardManhua) {
            statusItems = 1;
            String getItem = spinner.getSelectedItem().toString();
            String page = Integer.toString(pageNumber);
            if (!getItem.contains(page)) {
                getListManhua();

                pageNumber = Integer.valueOf(getItem);
                checkStatusBtn();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_manhua, container, false);
    }
}