package com.moadd.DemoLockApp.fragment;

import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.moadd.DemoLockApp.Login;
import com.moaddi.operatorApp.R;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import static android.content.Context.MODE_PRIVATE;
/**
 * A simple {@link Fragment} subclass.
 */
public class Synchronize extends Fragment implements NetworkStateReceiver.NetworkStateReceiverListener {
    int i;
    String s = "";
    SharedPreferences sp;
    SharedPreferences.Editor et;
    SharedPreferences locks;
    SharedPreferences.Editor locksEt;
    private NetworkStateReceiver networkStateReceiver;
ImageView iv;
TextView tv;
    public Synchronize() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_synchronize, container, false);
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        getActivity().registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
        sp = getActivity().getSharedPreferences("AllLocksItemsMachines", MODE_PRIVATE);
        et = sp.edit();
        locks = getActivity().getSharedPreferences("SupplierItemsandLocks", MODE_PRIVATE);
        locksEt = locks.edit();
        iv= (ImageView) v.findViewById(R.id.iv);
        tv= (TextView) v.findViewById(R.id.tv);
        new AllLocks().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new AllMachines().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return v;
    }

    @Override
    public void networkAvailable() {
        new AllLocks().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new AllMachines().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void networkUnavailable() {
        Toast.makeText(getActivity(), "Internet Needed for Synchronization.", Toast.LENGTH_SHORT).show();
    }


    private class AllLocks extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                String URL ="https://www.moaddi.com/moaddi/"+Login.category.toLowerCase()+"/lockslist.htm";
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

            if (lf != null) {
                locksEt.putString("lockDetails", lf).apply();
               // Toast.makeText(getActivity(), "All locks synced", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class AllMachines extends AsyncTask<Void, Integer, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                //The link on which we have to POST data and in return it will return some data
                String URL ="https://www.moaddi.com/moaddi/"+Login.category.toLowerCase()+"/machineslist.htm";
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
            if (lf != null) {
                et.putString("machinesAllData", lf).apply();
                iv.setBackgroundResource(R.drawable.tickticks);
                Toast.makeText(getActivity(), "All data synced", Toast.LENGTH_SHORT).show();
                tv.setText("All locks and machines data synced!");
            }
        }

    }
}
