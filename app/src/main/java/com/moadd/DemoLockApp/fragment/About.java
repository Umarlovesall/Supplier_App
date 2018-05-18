package com.moadd.DemoLockApp.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.moaddi.operatorApp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class About extends Fragment {
SharedPreferences sp;
TextView appId;
Button update;
    public About() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_about, container, false);
        sp=getActivity().getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        appId= (TextView) v.findViewById(R.id.AppId);
        update= (Button) v.findViewById(R.id.update);
      /*  update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.moadd.demolock"));
                    startActivity(intent);
            }
        });*/
        if (sp.getString("appId",null)!=null) {
            appId.setText("App Id :  "+sp.getString("appId", null));
        }
        return v;
    }

}
