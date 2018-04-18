package com.moadd.operatorApp.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import com.google.zxing.client.android.CaptureActivity;
import com.moadd.operatorApp.AddToFastListPojo;
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

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class SupplierItems extends Fragment implements NetworkStateReceiver.NetworkStateReceiverListener {
    public  static String clickedItem;
    Button add;
    int i,j;
    ImageView iv;
    ArrayList<String> al,selected;
    EditText searchBox;
    ListView lv;
    ArrayAdapter aa;
    String selectedItemsForFastList="";
    AddToFastListPojo b;
    String ItemsDetails,contents;
    private NetworkStateReceiver networkStateReceiver;
    //DatabaseLocksOperator db;
    ImageView show;
    SharedPreferences sp;
    SharedPreferences.Editor et;
    public SupplierItems() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_supplier_items, container, false);
        add= (Button) v.findViewById(R.id.fastlist);
        lv = (ListView) v.findViewById(R.id.listview);
        al = new ArrayList<String>();
        selected=new ArrayList<String>();
        aa = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,al);
        lv.setAdapter(aa);
        searchBox = (EditText) v.findViewById(R.id.searchBox);
        iv= (ImageView) v.findViewById(R.id.iv);
        sp=getActivity().getSharedPreferences("SupplierItemsandLocks",MODE_PRIVATE);
        et=sp.edit();
        b=new AddToFastListPojo();
        show= (ImageView) v.findViewById(R.id.showlocks);
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        /*db=new DatabaseLocksOperator(this);
        db.open();
        if (db.getLocksData()!=null) {
            lockDetails = db.getLocksData();
        }*/
        ItemsDetails=sp.getString("SupplierItemsDetails",null);
        new HttpRequestTask().execute();
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                intent.setAction("com.google.zxing.client.android.SCAN");
                intent.putExtra("SAVE_HISTORY", false);
                startActivityForResult(intent, 0);
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (selected.contains(al.get(position)))
                {
                    selected.remove(al.get(position));
                    //Change color
                    view.setBackgroundColor(Color.parseColor("#293d68"));
                }
                else if (!selected.contains(al.get(position)))
                {
                    selected.add(al.get(position));
                    //Change color
                    view.setBackgroundColor(Color.parseColor("#02fff6"));
                }
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
    /*    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
    add.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (selected.size()!=0) {
                for (j = 0; j < selected.size(); j++) {
                   /* b.setBarcode(selected.get(j));
                    b.setUserRoleId(Login.userRoleId);
                    Toast.makeText(getActivity(), b.getBarcode()+"  "+b.getUserRoleId(), Toast.LENGTH_SHORT).show();*/
                   selectedItemsForFastList= selectedItemsForFastList+","+selected.get(j);
                }
                selectedItemsForFastList=selectedItemsForFastList.substring(1);
                b.setBarcodeList(selectedItemsForFastList);
                b.setUserRoleId(Login.userRoleId);
                new HttpRequestTask1().execute();
                selectedItemsForFastList="";
            }
            else
            {
                Toast.makeText(getActivity(), "First select items by clicking on them.", Toast.LENGTH_SHORT).show();
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
                //The link on which we have to POST data and in return it will return some data
                // String URL = "https://www.moaddi.com/moaddi/supplier/serviessupplieritemsthroughbarcode1.htm";
                String URL = "https://www.moaddi.com/moaddi/supplier/servicesupplieritemslist.htm";
                //Create and set object 'l' of bean class LoginForm,which we will POST then
                //b.setUserRoleId("21");
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
            //The returned object of LoginForm that we recieve from postforobject in doInBackground is displayed here.
            //tv.setText(lf.getUsername()+lf.getPassword());
           // Toast.makeText(getActivity(),lf,Toast.LENGTH_LONG).show();
            if (lf!=null) {
                //Saving all details of the items in separate field so as to use it later for fetching "price" and other details of the items.
                try {
                    JSONArray j = new JSONArray(lf);
                    for (i=0;i<j.length();i++)
                    {
                        JSONObject l =j.getJSONObject(i);
                        al.add(l.getString("itemBarcode"));
                    }
                    aa.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Toast.makeText(getActivity(),lf,Toast.LENGTH_LONG).show();
                ItemsDetails = lf;
                et.putString("SupplierItemsDetails",lf).commit();
                //db.updateLockData(lockDetails);
            }
            else if (ItemsDetails!=null)
            {
                try {
                    JSONArray j = new JSONArray(ItemsDetails);
                    for (i=0;i<j.length();i++)
                    {
                        JSONObject l =j.getJSONObject(i);
                        al.add(l.getString("itemBarcode"));
                    }
                    aa.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(getActivity(), "Null returned", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class HttpRequestTask1 extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                //The link on which we have to POST data and in return it will return some data
                // String URL = "https://www.moaddi.com/moaddi/supplier/serviessupplieritemsthroughbarcode1.htm";
                String URL = "https://www.moaddi.com/moaddi/supplier/serviceadditemtofastlist.htm";
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
            Toast.makeText(getActivity(),b.getBarcodeList()+  " "+ b.getUserRoleId() , Toast.LENGTH_SHORT).show();

            if (lf!=null) {
                //Toast.makeText(getActivity(), lf, Toast.LENGTH_SHORT).show();
                if (j==selected.size())
                {
                    Toast.makeText(getActivity(), "Selected items successfully added to the fast list", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(getActivity(), "Null returned", Toast.LENGTH_SHORT).show();
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
