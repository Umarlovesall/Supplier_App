package com.moadd.operatorApp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moaddi.operatorApp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContinueConnection extends Fragment {


    public ContinueConnection() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_continue_connection, container, false);
        return v;
    }

}