package com.moadd.operatorApp.fragment;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.moaddi.operatorApp.R;

import static com.moadd.operatorApp.MainActivity.CURRENT_TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConnectionToLockOptions extends Fragment {
Button sendData,reset;

    public ConnectionToLockOptions() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_connection_to_lock_options, container, false);
        sendData= (Button) v.findViewById(R.id.sendData);
        reset= (Button) v.findViewById(R.id.reset);
        sendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectToLocks fragment = new ConnectToLocks();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG).addToBackStack(null);
                fragmentTransaction.commitAllowingStateLoss();
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FactoryResetLock fragment = new FactoryResetLock();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG).addToBackStack(null);
                fragmentTransaction.commitAllowingStateLoss();
            }
        });
        return v;
    }

}
