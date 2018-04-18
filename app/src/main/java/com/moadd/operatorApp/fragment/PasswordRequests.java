package com.moadd.operatorApp.fragment;


import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.moadd.operatorApp.BarcodeResultSend;
import com.moadd.operatorApp.Login;
import com.moaddi.operatorApp.R;
/**
 * A simple {@link Fragment} subclass.
 */
public class PasswordRequests extends Fragment {
    Button lockPasswordRequests,lockFormCase,passwordsRecieved;
    public PasswordRequests() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_password_requests, container, false);
        lockPasswordRequests= (Button) v.findViewById(R.id.LockPasswordRequest);
        lockFormCase= (Button) v.findViewById(R.id.lockForm);
        passwordsRecieved=(Button) v.findViewById(R.id.LockPasswordRecieve);
        lockPasswordRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LockPasswordRequest fragment = new LockPasswordRequest();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        lockFormCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LockFormCase fragment = new LockFormCase();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        passwordsRecieved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LockPasswordRecieve fragment = new LockPasswordRecieve();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        return v;
    }

    }
