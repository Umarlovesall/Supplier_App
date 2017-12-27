package com.moadd.operatorApp.fragment;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.moadd.operatorApp.BarcodeResultSend;
//import com.moadd.operatorApp.DatabaseLocksOperator;
import com.moadd.operatorApp.Login;
import com.moadd.operatorApp.MainActivity;
import com.moadd.operatorApp.Recievers.NetworkStateReceiver;
import com.moaddi.operatorApp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.moadd.operatorApp.MainActivity.CURRENT_TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllMachines extends Fragment implements NetworkStateReceiver.NetworkStateReceiverListener {
    public  static String clickedItem;
    int i;
    ArrayList<String> al;
    EditText searchBox;
    ListView lv;
    ArrayAdapter aa;
    String MachineDetails;
    private NetworkStateReceiver networkStateReceiver;
    //DatabaseLocksOperator db;
    ImageView show;
    SharedPreferences sp;
    SharedPreferences.Editor et;
    public AllMachines() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_all_machines, container, false);
        lv = (ListView) v.findViewById(R.id.listview);
        al = new ArrayList<String>();
        aa = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,al);
        lv.setAdapter(aa);
        searchBox = (EditText) v.findViewById(R.id.searchBox);
        sp=getActivity().getSharedPreferences("Locks",MODE_PRIVATE);
        et=sp.edit();
        show= (ImageView) v.findViewById(R.id.showlocks);
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        /*db=new DatabaseLocksOperator(this);
        db.open();
        if (db.getLocksData()!=null) {
            lockDetails = db.getLocksData();
        }*/
        MachineDetails=sp.getString("MachineDetails",null);
        new HttpRequestTask().execute();
        //Toast.makeText(getActivity(),lockDetails,Toast.LENGTH_LONG).show();
       /* searchBox.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                getActivity().aa.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });*/
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                aa.getFilter().filter(s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickedItem=al.get(position);
                LockDetails fragment = new LockDetails();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG).addToBackStack(null);
                fragmentTransaction.commitAllowingStateLoss();
            }
        });
        return v;
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                //The link on which we have to POST data and in return it will return some data
                String URL = "https://www.moaddi.com/moaddi/supplier/serviesmachines.htm";
                //Create and set object 'l' of bean class LoginForm,which we will POST then
                BarcodeResultSend b=new BarcodeResultSend();
                b.setUserRoleId(Login.userRoleId);
                //Use RestTemplate to POST(within Asynctask)
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                //postforobject method POSTs data to server and brings back LoginForm object format data.
                String lf = restTemplate.postForObject(URL, b, String.class);
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
            try {
                JSONArray j = new JSONArray(lf);
                for (i=0;i<j.length();i++)
                {
                    JSONObject l =j.getJSONObject(i);
                    al.add(l.getString("machineSno"));
                }
                aa.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Toast.makeText(getActivity(),lf,Toast.LENGTH_LONG).show();
            if (lf!=null) {
                MachineDetails = lf;
                et.putString("MachineDetails",lf).commit();
                //db.updateLockData(lockDetails);
            }
        }
    }
    @Override
    public void networkAvailable() {
        new HttpRequestTask().execute();
    }

    @Override
    public void networkUnavailable() {

    }

}
