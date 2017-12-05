package com.moadd.operatorApp.fragment;



import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moadd.operatorApp.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ConnectionToLockOptions extends Fragment {


    public ConnectionToLockOptions() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_connection_to_lock_options, container, false);
    }

}
