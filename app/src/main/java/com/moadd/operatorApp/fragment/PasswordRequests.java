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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import static com.moadd.operatorApp.MainActivity.CURRENT_TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class PasswordRequests extends Fragment {
    Button lockPasswordRequests;
    public PasswordRequests() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_password_requests, container, false);
        lockPasswordRequests= (Button) v.findViewById(R.id.LockPasswordRequest);
        lockPasswordRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LockPasswordRequest fragment = new LockPasswordRequest();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG).addToBackStack(null);
                fragmentTransaction.commitAllowingStateLoss();
            }
        });

        return v;
    }

    }
