package com.moadd.DemoLockApp.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.moadd.DemoLockApp.Login;
import com.moaddi.operatorApp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Lock_Setup_Details extends Fragment {
Button barcode,operator,suppliers,opHotspot,supHotspot,password,wifiTimeOut;
public static String text="";
SharedPreferences AllDetails;
SharedPreferences.Editor AllDetailsEt;
    public Lock_Setup_Details() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_lock__setup__details, container, false);
        barcode= (Button) v.findViewById(R.id.lockBarcode);
        operator= (Button) v.findViewById(R.id.connectedOperator);
        suppliers= (Button) v.findViewById(R.id.connectedSupplier);
        opHotspot= (Button) v.findViewById(R.id.opHotspot);
        supHotspot= (Button) v.findViewById(R.id.supHotspot);
        password= (Button) v.findViewById(R.id.password);
        wifiTimeOut= (Button) v.findViewById(R.id.wifiTimeOut);
        AllDetails=getActivity().getSharedPreferences(Login.selectedlockBarcode, Context.MODE_PRIVATE);
        AllDetailsEt=AllDetails.edit();
        barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
if (!AllDetails.getString("LockSerialNumber","").equals(""))
{
    text=AllDetails.getString("LockSerialNumber","");
}
else
{
    text="Not Available";
}
                DisplayText fragment = new DisplayText();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        operator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AllDetails.getString("Operator Id and App Id","").equals(""))
                {
                    text="Operator Id and App Id : "+AllDetails.getString("Operator Id and App Id","");
                }
                else
                {
                    text="Not Available";
                }
                DisplayText fragment = new DisplayText();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        suppliers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AllDetails.getString("Connected Suppliers IDs and App IDs","").equals(""))
                {
                    text="Connected Suppliers IDs and App IDs : "+AllDetails.getString("Connected Suppliers IDs and App IDs","");
                }
                else
                {
                    text="Not Available";
                }
                DisplayText fragment = new DisplayText();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        opHotspot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AllDetails.getString("Operator Hotspot Details","").equals(""))
                {
                    text="Operator Hotspot Details : "+AllDetails.getString("Operator Hotspot Details","");
                }
                else
                {
                    text="Not Available";
                }
                DisplayText fragment = new DisplayText();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        supHotspot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AllDetails.getString("Supplier Hotspot Details","").equals(""))
                {
                    text="Supplier Hotspot Details : "+AllDetails.getString("Supplier Hotspot Details","");
                }
                else
                {
                    text="Not Available";
                }
                DisplayText fragment = new DisplayText();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AllDetails.getString("Password Of the Lock","").equals(""))
                {
                    text="Password Of the Lock : "+AllDetails.getString("Password Of the Lock","");
                }
                else
                {
                    text="Not Available";
                }
                DisplayText fragment = new DisplayText();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        wifiTimeOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AllDetails.getString("Date,Time and WifiLimit","").equals(""))
                {
                    text="Wifi Time out Limit : "+AllDetails.getString("Date,Time and WifiLimit","");
                }
                else
                {
                    text="Not Available";
                }
                DisplayText fragment = new DisplayText();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        return v;
    }

}
