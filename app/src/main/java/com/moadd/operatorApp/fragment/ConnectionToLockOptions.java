package com.moadd.operatorApp.fragment;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.moaddi.operatorApp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConnectionToLockOptions extends Fragment {
Button onebyone,continueC;

    public ConnectionToLockOptions() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_connection_to_lock_options, container, false);
        onebyone= (Button) v.findViewById(R.id.sendData);
        continueC= (Button) v.findViewById(R.id.continuec);
        onebyone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ConnectToLocks fragment = new ConnectToLocks();
                TrialOneByOne fragment = new TrialOneByOne();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        continueC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContinueConnection fragment = new ContinueConnection();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        return v;
    }

}
