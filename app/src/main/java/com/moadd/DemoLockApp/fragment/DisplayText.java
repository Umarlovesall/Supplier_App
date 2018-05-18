package com.moadd.DemoLockApp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moaddi.operatorApp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DisplayText extends Fragment {

TextView tv;
    public DisplayText() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_display_text, container, false);
        tv= (TextView) v.findViewById(R.id.tv);
        tv.setText(Lock_Setup_Details.text);
        return v;
    }

}
