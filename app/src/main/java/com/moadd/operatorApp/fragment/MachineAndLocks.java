package com.moadd.operatorApp.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.client.android.CaptureActivity;
import com.moadd.operatorApp.BarcodeResultSend;
import com.moadd.operatorApp.Login;
import com.moadd.operatorApp.MachineAndLock;
import com.moadd.operatorApp.MachineLockForm;
import com.moaddi.operatorApp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpRequest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class MachineAndLocks extends Fragment {
    Button BarcodeLock,BarcodeMachine,disconnect,connect;
    EditText BarTextMachine,BarTextLock;
    String contents;
    SharedPreferences sp,st,mAndL;
    SharedPreferences.Editor mnlEt;
    ArrayList<String> al;
    String status;
    int internet;
    MachineAndLock mnl;
    public MachineAndLocks() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_machine_and_locks, container, false);
        BarcodeLock= (Button) v.findViewById(R.id.LockBarcodeRead);
        BarcodeMachine= (Button) v.findViewById(R.id.machineBarcodeRead);
        disconnect= (Button) v.findViewById(R.id.disconnect);
        connect= (Button) v.findViewById(R.id.connect);
        mnl=new MachineAndLock();
        BarTextMachine= (EditText) v.findViewById(R.id.enteredMachineBarcode);
        BarTextLock= (EditText) v.findViewById(R.id.enteredLockBarcode);
        al = new ArrayList<String>();
        sp=getActivity().getSharedPreferences("AllLocksItemsMachines",MODE_PRIVATE);
        st=getActivity().getSharedPreferences("SupplierItemsandLocks",MODE_PRIVATE);
        mAndL=getActivity().getSharedPreferences("MachineAndLock",MODE_PRIVATE);
        mnlEt=mAndL.edit();
        BarcodeMachine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                intent.setAction("com.google.zxing.client.android.SCAN");
                intent.putExtra("SAVE_HISTORY", false);
                startActivityForResult(intent, 0);
            }
        });
        BarcodeLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                intent.setAction("com.google.zxing.client.android.SCAN");
                intent.putExtra("SAVE_HISTORY", false);
                startActivityForResult(intent, 1);
            }
        });
        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s= sp.getString("MachineDetails","");
                if (!s.equals("") && !BarTextMachine.getText().toString().trim().equals(""))
                {
                    try {
                        JSONArray j = new JSONArray(s);
                        for (int i=0;i<j.length();i++)
                        {
                            JSONObject l =j.getJSONObject(i);
                            al.add(l.getString("machineSno"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (al.contains(BarTextMachine.getText().toString().trim()))
                    {
                        Toast.makeText(getActivity(),"Machine belongs to the Supplier",Toast.LENGTH_SHORT).show();
                        String t= st.getString("lockDetails","");
                        if (!t.equals("") && !BarTextLock.getText().toString().equals(""))
                        {
                            try {
                                JSONArray j = new JSONArray(t);
                                for (int i=0;i<j.length();i++)
                                {
                                    JSONObject l =j.getJSONObject(i);
                                    al.add(l.getString("lockSno"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (al.contains(BarTextLock.getText().toString().trim()))
                            {
                                Toast.makeText(getActivity(),"Lock belongs to the Supplier",Toast.LENGTH_SHORT).show();
                                status="Disconnect";
                                mnl.setStatus(status);
                                if(internet==1) {
                                    new HttpRequestTask().execute();
                                }
                                else if (internet==0)
                                {
                                    mnlEt.putString("pending",mAndL.getString("pending","")+mnl.getLock()+" "+mnl.getMachine()+" "+mnl.getUserRoleId()+" "+mnl.getStatus()+"#").apply();
                                    // Toast.makeText(getActivity(), "Item and lock connected (Offline),will connect online on syncronization.",Toast.LENGTH_LONG).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(getActivity(),"Lock doesn't belong to the Supplier",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getActivity(),"Enter/Scan Lock barcode first",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Machine doesn't belong to the Supplier",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getActivity(),"Enter/Scan Machine barcode first",Toast.LENGTH_SHORT).show();
                }

            }
        });
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s= sp.getString("MachineDetails","");
                if (!s.equals("") && !BarTextMachine.getText().toString().trim().equals(""))
                {
                    try {
                        JSONArray j = new JSONArray(s);
                        for (int i=0;i<j.length();i++)
                        {
                            JSONObject l =j.getJSONObject(i);
                            al.add(l.getString("machineSno"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (al.contains(BarTextMachine.getText().toString().trim()))
                    {
                        Toast.makeText(getActivity(),"Machine belongs to the Supplier",Toast.LENGTH_SHORT).show();
                        String t= st.getString("lockDetails","");
                        if (!t.equals("") && !BarTextLock.getText().toString().equals(""))
                        {
                            try {
                                JSONArray j = new JSONArray(t);
                                for (int i=0;i<j.length();i++)
                                {
                                    JSONObject l =j.getJSONObject(i);
                                    al.add(l.getString("lockSno"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (al.contains(BarTextLock.getText().toString().trim()))
                            {
                                Toast.makeText(getActivity(),"Lock belongs to the Supplier",Toast.LENGTH_SHORT).show();
                                status="Connect";
                                mnl.setStatus(status);
                                if(internet==1) {
                                    new HttpRequestTask().execute();
                                }
                                else if (internet==0)
                                {
                                    mnlEt.putString("pending",mAndL.getString("pending","")+mnl.getLock()+" "+mnl.getMachine()+" "+mnl.getUserRoleId()+" "+mnl.getStatus()+"#").apply();
                                    // Toast.makeText(getActivity(), "Item and lock connected (Offline),will connect online on syncronization.",Toast.LENGTH_LONG).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(getActivity(),"Lock doesn't belong to the Supplier",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getActivity(),"Enter/Scan Lock barcode first",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Machine doesn't belong to the Supplier",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getActivity(),"Enter/Scan Machine barcode first",Toast.LENGTH_SHORT).show();
                }
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
                BarTextMachine.setText(contents);
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
                Toast.makeText(getActivity(), "No Output", Toast.LENGTH_LONG).show();
            }
        }
        else if (resultCode ==1)
        {
            if (resultCode == RESULT_OK) {
                contents = data.getStringExtra("result");
                BarTextLock.setText(contents);
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
                Toast.makeText(getActivity(), "No Output", Toast.LENGTH_LONG).show();
            }
        }
    }
    public class HttpRequestTask extends AsyncTask<Void, Void,String > {
        // String a=null;
        String la=null;
        @Override
        public  String doInBackground(Void... params) {
            try {
                //The link on which we have to POST data and in return it will return some data
                String URL = "https://www.moaddi.com/moaddi/supplier/servicemachinelock.htm";
                //String URL = "http://192.168.0.104:8082/WebServices/supplier/servicemachinelock.htm";
               mnl.setLock(BarTextLock.getText().toString());
                mnl.setMachine(BarTextMachine.getText().toString());
                mnl.setUserRoleId(Login.userRoleId);
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                la = restTemplate.postForObject(URL,mnl,String.class);
                return la;
            } catch (Exception e) {
                Log.e("LoginActivity", e.getMessage(), e);

            }

            return null;
        }

        @Override
        protected void onPostExecute(String  la) {
            //The returned object of LoginForm that we recieve from postforobject in doInBackground is displayed here.
            //tv.setText(la);
            //Toast.makeText(getActivity(),la,Toast.LENGTH_LONG).show();
            if (la != null) {

                Toast.makeText(getActivity(),la,Toast.LENGTH_LONG).show();

            }
            else
            {
                Toast.makeText(getActivity(),"Null Returned",Toast.LENGTH_LONG).show();
            }
        }
    }
}
