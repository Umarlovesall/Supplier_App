package com.moadd.operatorApp.fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.android.CaptureActivity;
import com.moadd.operatorApp.AppDetailsPojo;
import com.moadd.operatorApp.LockFormPojo;
import com.moadd.operatorApp.Login;
import com.moaddi.operatorApp.R;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.moadd.operatorApp.fragment.HomeFragment.flag;


/**
 * A simple {@link Fragment} subclass.
 */
public class LockFormCase extends Fragment implements AdapterView.OnItemSelectedListener {
    String item="",contents;
    Button readBarcode,submit,history,serialNumber;
    TextView barcode;
    EditText other;
    String sno,complaint;
    LockFormPojo l;
    public LockFormCase() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_lock_form_case, container, false);
        l=new LockFormPojo();
        Spinner spinner = (Spinner) v.findViewById(R.id.listOfProblems);
        readBarcode= (Button) v.findViewById(R.id.bar);
        submit= (Button) v.findViewById(R.id.submit);
        history= (Button) v.findViewById(R.id.history);
        serialNumber= (Button) v.findViewById(R.id.serial);
        barcode= (TextView) v.findViewById(R.id.lockBarcode);
        other= (EditText) v.findViewById(R.id.otherReason);
        spinner.setOnItemSelectedListener(this);
        ArrayList <String> categories = new ArrayList();
        categories.add("Operator Problem");
        categories.add("Low Battery");
        categories.add("Other");
        //serialNumber.setText("Sno."+(int) (Math.random() * 100000000));
        ArrayAdapter dataAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        readBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment.flag=1;
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                intent.setAction("com.google.zxing.client.android.SCAN");
                intent.putExtra("SAVE_HISTORY", false);
                startActivityForResult(intent, 0);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!other.getText().toString().equals(""))
                {
                    complaint=other.getText().toString();
                }
                if (barcode.getText()!=null)
                {
                    if (complaint!=null)
                    {
                new HttpRequestTask().execute();
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Select or Type your issue first",Toast.LENGTH_SHORT).show();

                    }
                }
                else
                {
                    Toast.makeText(getActivity(),"Scan a lock first",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        item = parent.getItemAtPosition(position).toString();
        complaint=item;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                contents = data.getStringExtra("result");
                barcode.setText(contents);
                sno=""+(int) (Math.random() * 100000000);
                serialNumber.setText("Sno."+sno);
                l.setComplaintSno(sno);
                l.setLockBarcode(contents);
              //  new HttpRequestTask().execute();

            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
                Toast.makeText(getActivity(), "No Output", Toast.LENGTH_LONG).show();
            }
        }
    }
    private class HttpRequestTask extends AsyncTask<Void, Void,String> {
        // String a=null;
        String la=null;
        @Override
        public  String doInBackground(Void... params) {
            try {
                //The link on which we have to POST data and in return it will return some data
                //String URL = "http://192.168.0.109:8081/Moaddi2/supplier/setLockComplainent.htm";
                String URL = "https://www.moaddi.com/moaddi/supplier/setLockComplainent.htm";
                RestTemplate restTemplate = new RestTemplate();
                l.setUserRoleId(Login.userRoleId);
                //l.setUserRoleId("21");
                l.setComplaint(complaint);
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                la = restTemplate.postForObject(URL,l,String.class);
                return la;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String m) {
            Toast.makeText(getActivity(),m,Toast.LENGTH_LONG).show();
        }
    }
}
