package com.moadd.DemoLockApp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moaddi.operatorApp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class LockSettings extends Fragment {


    public LockSettings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_lock_settings, container, false);
        return v;
    }

}
