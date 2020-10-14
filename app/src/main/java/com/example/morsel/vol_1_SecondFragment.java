package com.example.morsel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class vol_1_SecondFragment extends Fragment {

    View view;
    TextView tv_ty;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.vol_1_fragment_second, container, false);

        tv_ty = view.findViewById(R.id.tv_ty);
        return view;

    }
}

