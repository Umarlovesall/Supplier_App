package com.moadd.DemoLockApp.fragment;


import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.client.android.CaptureActivity;
import com.moadd.DemoLockApp.Login;
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

//import com.moadd.operatorApp.DatabaseLocksOperator;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllLocks extends Fragment implements NetworkStateReceiver.NetworkStateReceiverListener {
    public  static String clickedItem;
    int i;
    ArrayList<String> al,alSecretNo;
    EditText searchBox;
    ListView lv;
    ArrayAdapter aa;
    String lockDetails,contents;
    private NetworkStateReceiver networkStateReceiver;
    //DatabaseLocksOperator db;
   ImageView show,iv;
    SharedPreferences sp;
    SharedPreferences.Editor et;
    public AllLocks() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_all_locks, container, false);
        lv = (ListView) v.findViewById(R.id.listview);
        al = new ArrayList<String>();
        alSecretNo=new ArrayList<String>();
        aa = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,al);
        lv.setAdapter(aa);
        searchBox = (EditText) v.findViewById(R.id.searchBox);
        iv= (ImageView) v.findViewById(R.id.iv);
        sp=getActivity().getSharedPreferences("SupplierItemsandLocks",MODE_PRIVATE);
        et=sp.edit();
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        lockDetails=sp.getString("lockDetails",null);
        new HttpRequestTask().execute();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Login.selectedlockBarcode=al.get(position);
                Login.selectedSecretNo=alSecretNo.get(position);
                HomeFragment fragment = new HomeFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
       iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                intent.setAction("com.google.zxing.client.android.SCAN");
                intent.putExtra("SAVE_HISTORY", false);
                startActivityForResult(intent, 0);
            }
        });
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
     /*  lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               clickedItem=al.get(position);
               LockDetails fragment = new LockDetails();
               FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
               ft.replace(R.id.frame, fragment);
               ft.addToBackStack(null);
               ft.commit();
           }
       });*/
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                contents = data.getStringExtra("result");
                searchBox.setText(contents);
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
               //String URL = "http://192.168.0.102:8082/webServices/"+Login.category.toLowerCase()+"/lockslist.htm";
               String URL ="https://www.moaddi.com/moaddi/"+Login.category.toLowerCase()+"/lockslist.htm";
                //String URL ="http://192.168.0.108:8082/webservices/"+Login.category.toLowerCase()+"/lockslist.htm";
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

            //Toast.makeText(getActivity(),lf,Toast.LENGTH_SHORT).show();
            //The returned object of LoginForm that we recieve from postforobject in doInBackground is displayed here.
            //tv.setText(lf.getUsername()+lf.getPassword());
            if (lf!=null) {
                //Saving all details of the lock in separate field so as to use it later for fetching "price" and other details of the items.
                et.putString("lockDetails",lf).apply();
                try {
                    JSONArray j = new JSONArray(lf);
                    for (i=0;i<j.length();i++)
                    {
                        JSONObject l =j.getJSONObject(i);
                        al.add(l.getString("lockSno"));
                        alSecretNo.add(l.getString("lockSecreteCode"));
                    }
                    aa.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Toast.makeText(getActivity(),lf,Toast.LENGTH_LONG).show();
                lockDetails = lf;
                et.putString("lockDetails",lf).commit();
                //db.updateLockData(lockDetails);
            }
            else if (lockDetails!=null)
            {
                try {
                    JSONArray j = new JSONArray(lockDetails);
                    for (i=0;i<j.length();i++)
                    {
                        JSONObject l =j.getJSONObject(i);
                        al.add(l.getString("lockSno"));
                        alSecretNo.add(l.getString("lockSecreteCode"));
                    }
                    aa.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public void networkAvailable() {
        //new HttpRequestTask().execute();
    }

    @Override
    public void networkUnavailable() {

    }

}
