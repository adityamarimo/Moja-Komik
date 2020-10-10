package com.mojasoft.mojakomik.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.mojasoft.mojakomik.MainActivity;
import com.mojasoft.mojakomik.R;

public class DetailSinopsisFragment extends DialogFragment {
    TextView textView;

    public DetailSinopsisFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        savedInstanceState = getArguments();
        String sinopsis = savedInstanceState.getString("sinopsis", "");

        String finalSinopsis = "\t\t\t" + sinopsis + "  ";
        textView = view.findViewById(R.id.txtDetailSinopsis);

        textView.setText(finalSinopsis);
        Log.d(MainActivity.class.getSimpleName(), "Testing = " + sinopsis);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_sinopsis, container, false);
    }
}