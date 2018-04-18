package com.moadd.operatorApp.fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.android.CaptureActivity;
import com.moadd.operatorApp.BarcodeResultSend;
import com.moadd.operatorApp.LockBelongsToOperatorOrNot;
import com.moadd.operatorApp.Login;
import com.moaddi.operatorApp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class LockPasswordRecieve extends Fragment {
ListView lv;
ArrayList<String> al;
ArrayAdapter aa;
Button BarcodeReader,clear;
TextView tv;
String contents;
    LockBelongsToOperatorOrNot b;
    public LockPasswordRecieve() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_lock_password_recieve, container, false);
        b=new LockBelongsToOperatorOrNot();
        BarcodeReader= (Button) v.findViewById(R.id.bar);
        clear= (Button) v.findViewById(R.id.clear);
        tv= (TextView) v.findViewById(R.id.bartext);
        lv = (ListView) v.findViewById(R.id.lv);
        al = new ArrayList<String>();
        aa = new ArrayAdapter<String>(getActivity(),R.layout.white_text_arraylist,al);
        lv.setAdapter(aa);
        BarcodeReader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment.flag=1;
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                intent.setAction("com.google.zxing.client.android.SCAN");
                intent.putExtra("SAVE_HISTORY", false);
                startActivityForResult(intent, 0);
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               al.clear();
               aa.notifyDataSetChanged();
            }
        });
        return v;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                contents = data.getStringExtra("result");
                tv.setText(contents);
                b.setLockBarcode(contents);
                new HttpRequestTask().execute();
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
                Toast.makeText(getActivity(), "No Output", Toast.LENGTH_LONG).show();
            }
        }
    }
    private class HttpRequestTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                //The link on which we have to POST data and in return it will return some data
                String URL = "https://www.moaddi.com/moaddi/supplier/servieschecklockpassword.htm";
                //String URL = "http://192.168.0.109:8081/Moaddi2/supplier/servieschecklockpassword.htm";
                //Create and set object 'l' of bean class LoginForm,which we will POST then
                //b.setUserRoleId("21");
                b.setUserRoleId(Login.userRoleId);
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                //postforobject method POSTs data to server and brings back LoginForm object format data.
                String lf = restTemplate.postForObject(URL,b , String.class);
                return lf;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String lf) {
            //The returned object of LoginForm that we recieve from postforobject in doInBackground is displayed here.
            //tv.setText(lf.getUsername()+lf.getPassword());
            //Toast.makeText(getActivity(),lf, Toast.LENGTH_SHORT).show();
            if (lf!=null) {
                //Saving all details of the lock in separate field so as to use it later for fetching "price" and other details of the items.
                JSONObject l = null;
                try {
                    l = new JSONObject(lf);
                    al.add(tv.getText() + "                                                 "+l.getString("password"));
                    aa.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(getActivity(),"Password doesn't exist",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
