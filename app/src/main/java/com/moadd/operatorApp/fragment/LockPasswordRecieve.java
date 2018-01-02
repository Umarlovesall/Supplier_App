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
public class LockPasswordRecieve extends Fragment {


    public LockPasswordRecieve() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_lock_password_recieve, container, false);

        return v;
    }

}
