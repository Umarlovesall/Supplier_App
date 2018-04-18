package com.moadd.operatorApp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.moadd.operatorApp.MachinePlace;
import com.moaddi.operatorApp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class MachinetypeAndPlace extends Fragment {

Button normal,masjid;
    public MachinetypeAndPlace() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_machinetype_and_place, container, false);
        normal= (Button) v.findViewById(R.id.normal);
        masjid= (Button) v.findViewById(R.id.masjid);
        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MachinePlace fragment = new MachinePlace();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        masjid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MasjidMachinePlace fragment = new MasjidMachinePlace();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        return v;
    }

}
