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
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.android.CaptureActivity;
import com.moadd.operatorApp.AddLocationToMachineForm;
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
import static android.content.Context.MODE_PRIVATE;


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
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.android.CaptureActivity;
import com.moadd.operatorApp.fragment.OneByOne;
import com.moaddi.operatorApp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class MasjidMachinePlace extends Fragment {
    Button BarcodePlace, BarcodeMachine, check, connect;
    EditText BarTextMachine, BarTextPlace;
    String contents,userroleid;
    SharedPreferences sp;
    ArrayList<String> al,al1;
    TextView tv;
    String machineText, location,allLocationDetails,machine,place;
    AddLocationToMachineForm altm;

    public MasjidMachinePlace() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_machine_place, container, false);
        userroleid= Login.userRoleId;
        BarcodePlace = (Button) v.findViewById(R.id.PlaceBarcodeRead);
        BarcodeMachine = (Button) v.findViewById(R.id.machineBarcodeRead);
        check = (Button) v.findViewById(R.id.check);
        connect = (Button) v.findViewById(R.id.connect);
        BarTextMachine = (EditText) v.findViewById(R.id.enteredMachineBarcode);
        BarTextPlace = (EditText) v.findViewById(R.id.enteredPlaceBarcode);
        tv = (TextView) v.findViewById(R.id.textview);
        al = new ArrayList<String>();
        al1= new ArrayList<String>();
        sp = getActivity().getSharedPreferences("AllLocksItemsMachines", MODE_PRIVATE);
        altm = new AddLocationToMachineForm();
        new HttpRequestTask1().execute();
        BarcodeMachine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                intent.setAction("com.google.zxing.client.android.SCAN");
                intent.putExtra("SAVE_HISTORY", false);
                startActivityForResult(intent, 0);
            }
        });
        BarcodePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                intent.setAction("com.google.zxing.client.android.SCAN");
                intent.putExtra("SAVE_HISTORY", false);
                startActivityForResult(intent, 1);
            }
        });
       /* check.setOnClickListener(new View.OnClickListener() {
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
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Machine doesn't belong to the Supplier",Toast.LENGTH_SHORT).show();
                    }
                }
                String t= sp.getString("lockDetails","");
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
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Lock doesn't belong to the Supplier",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });*/
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                machineText = BarTextMachine.getText().toString().trim();
                location = BarTextPlace.getText().toString().trim();
                String s = sp.getString("MachineDetails", "");
                place= BarTextPlace.getText().toString().trim();
                machine=BarTextMachine.getText().toString().trim();
                if (!s.equals("") && !BarTextMachine.getText().toString().trim().equals("") && !BarTextPlace.getText().toString().trim().equals("")) {
                   /* try {
                        JSONArray j = new JSONArray(s);
                        for (int i = 0; i < j.length(); i++) {
                            JSONObject l = j.getJSONObject(i);
                            al.add(l.getString("machineSno"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (al.contains(BarTextMachine.getText().toString().trim())) {
                        Toast.makeText(getActivity(), "Machine belongs to the Supplier", Toast.LENGTH_SHORT).show();
                        altm.setStatus(Login.userRoleId);
                        altm.setMachineId(machineText);
                        altm.setLocationId(location);
                        new HttpRequestTask().execute();
                    } else {
                        Toast.makeText(getActivity(), "Machine doesn't belong to the Supplier", Toast.LENGTH_SHORT).show();
                    }*/
                    try {
                        JSONArray j = new JSONArray(s);
                        for (int i = 0; i < j.length(); i++) {
                            JSONObject l = j.getJSONObject(i);
                            al.add(l.getString("machineSno"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(al1.contains(BarTextPlace.getText().toString().trim()) )
                    {
                        if (al.contains(BarTextMachine.getText().toString().trim() ))
                        {
                            try {
                                JSONArray ja=new JSONArray(allLocationDetails);
                                JSONObject jo=ja.getJSONObject(al1.indexOf(place));
                                altm.setLocationId(jo.getLong("locationId")+"");
                                JSONArray jk=new JSONArray(s);
                                JSONObject jb=jk.getJSONObject(al.indexOf(machine));
                                // altm.setMachineId(jb.getLong("machineId")+"");
                                altm.setMachineId(machine);
                                altm.setStatus(Login.userRoleId);
                                new HttpRequestTask().execute();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        else {
                            Toast.makeText(getActivity(), "Invalid Machine Barcode.", Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Invalid Place Barcode.", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getActivity(), "Enter both Barcodes first.", Toast.LENGTH_LONG).show();
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
        } else if (resultCode == 1) {
            if (resultCode == RESULT_OK) {
                contents = data.getStringExtra("result");
                //BarTextLock.setText(contents);
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
                String URL = "https://www.moaddi.com/moaddi/supplier/serviceaddmachinestomasjidlocation.htm";
                //String URL = "http://192.168.0.113:8081/Moaddi2/supplier/machineslist.htm";
                //Create and set object 'l' of bean class LoginForm,which we will POST then
             /*  AddLocationToMachineForm b=new AddLocationToMachineForm();
               b.setLocationId(BarTextPlace.getText().toString().trim());
               b.setMachineId(BarTextMachine.getText().toString().trim());
               b.setStatus(Login.userRoleId);*/
                //Use RestTemplate to POST(within Asynctask)
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                //postforobject method POSTs data to server and brings back LoginForm object format data.
                //String lf = restTemplate.postForObject(URL, altm, String.class);
                String lf = restTemplate.postForObject(URL, altm, String.class);
                return lf;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }
//1514179632640
            return null;
        }

        @Override
        protected void onPostExecute(String lf) {
            Toast.makeText(getActivity(), altm.getLocationId()+"  "+altm.getMachineId()+"  "+altm.getStatus(), Toast.LENGTH_SHORT).show();
            //Toast.makeText(getActivity(),lf,Toast.LENGTH_SHORT).show();
            if (lf != null) {
                tv.setText(lf);
                Toast.makeText(getActivity(), lf, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), lf, Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class HttpRequestTask1 extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                //The link on which we have to POST data and in return it will return some data
                String URL = "https://www.moaddi.com/moaddi/supplier/servicemachinetomasjidlocation.htm";
                //String URL = "http://192.168.0.113:8081/Moaddi2/supplier/machineslist.htm";
                //Create and set object 'l' of bean class LoginForm,which we will POST then
             /*  AddLocationToMachineForm b=new AddLocationToMachineForm();
               b.setLocationId(BarTextPlace.getText().toString().trim());
               b.setMachineId(BarTextMachine.getText().toString().trim());
               b.setStatus(Login.userRoleId);*/
                //Use RestTemplate to POST(within Asynctask)
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                //postforobject method POSTs data to server and brings back LoginForm object format data.
                String lf = restTemplate.postForObject(URL, Login.userRoleId, String.class);
                return lf;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String lf) {
           Toast.makeText(getActivity(),lf,Toast.LENGTH_SHORT).show();
            /*Toast.makeText(getActivity(),Login.userRoleId,Toast.LENGTH_SHORT).show();*/

            if (lf!=null) {
                //Saving all details of the items in separate field so as to use it later for fetching "price" and other details of the items.
                allLocationDetails=lf;
                try {
                    JSONArray j = new JSONArray(lf);
                    for (int i = 0; i < j.length(); i++) {
                        JSONObject l = j.getJSONObject(i);
                        al1.add(l.getLong("locationSno")+"");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Toast.makeText(getActivity(),lf,Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getActivity(), "Null returned", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


