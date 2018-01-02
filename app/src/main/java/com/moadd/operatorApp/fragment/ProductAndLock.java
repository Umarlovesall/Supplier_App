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
public class ProductAndLock extends Fragment {
Button oneByOne,continueConnection;

    public ProductAndLock() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_product_and_lock, container, false);
        oneByOne = (Button) v.findViewById(R.id.OneByOne);
        continueConnection = (Button) v.findViewById(R.id.ContinueConnection);
        oneByOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OneByOne fragment = new OneByOne();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG).addToBackStack(null);
                fragmentTransaction.commitAllowingStateLoss();
            }
        });
        continueConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContinueConnection fragment = new ContinueConnection();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG).addToBackStack(null);
                fragmentTransaction.commitAllowingStateLoss();
            }
        });
        return v;
    }
}
