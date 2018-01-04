package com.moadd.operatorApp.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.client.android.CaptureActivity;
import com.moaddi.operatorApp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class MachineAndLocks extends Fragment {
    Button BarcodeLock,BarcodeMachine,check,connect;
    EditText BarTextMachine,BarTextLock;
    String contents;
    SharedPreferences sp;
    ArrayList<String> al;
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
        check= (Button) v.findViewById(R.id.check);
        connect= (Button) v.findViewById(R.id.connect);
        BarTextMachine= (EditText) v.findViewById(R.id.enteredMachineBarcode);
        BarTextLock= (EditText) v.findViewById(R.id.enteredLockBarcode);
        al = new ArrayList<String>();
        sp=getActivity().getSharedPreferences("AllLocksItemsMachines",MODE_PRIVATE);
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
        check.setOnClickListener(new View.OnClickListener() {
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
        });
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
}
