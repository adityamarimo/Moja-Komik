package com.mojasoft.mojakomik.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.mojasoft.mojakomik.R;

public class TentangSayaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tentang_saya);

        getSupportActionBar().setTitle("Tentang Saya");
    }
}