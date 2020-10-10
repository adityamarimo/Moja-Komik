package com.mojasoft.mojakomik.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.mojasoft.mojakomik.R;

public class AboutFragment extends Fragment implements View.OnClickListener{

    LinearLayout lineAboutApp;
    LinearLayout lineAboutMe;
    LinearLayout lineAboutLog;

    TextView textViewDonasi;

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about, container, false);

        lineAboutApp = root.findViewById(R.id.aboutAplikasi);
        lineAboutMe = root.findViewById(R.id.aboutSaya);
        lineAboutLog = root.findViewById(R.id.btnLog);
        textViewDonasi = root.findViewById(R.id.btnDonasi);

        lineAboutApp.setOnClickListener(this);
        lineAboutMe.setOnClickListener(this);
        lineAboutLog.setOnClickListener(this);
        textViewDonasi.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.aboutAplikasi){
            Intent intent = new Intent(getActivity(), TentangAplikasiActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.aboutSaya){
            Intent intent = new Intent(getActivity(), TentangSayaActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.btnDonasi){
            String donasi = "https://trakteer.id/mojasoft";
            Intent moveToDetailActivity = new Intent(Intent.ACTION_VIEW);
            moveToDetailActivity.setData(Uri.parse(donasi));
            startActivity(moveToDetailActivity);
        } else if (view.getId() == R.id.btnLog){
            Intent intent = new Intent(getActivity(), LogAppActivity.class);
            startActivity(intent);
        }
    }
}