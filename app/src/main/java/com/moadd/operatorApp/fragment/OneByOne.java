package com.moadd.operatorApp.fragment;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.android.CaptureActivity;
import com.moadd.operatorApp.wifiHotSpots;
import com.moaddi.operatorApp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class OneByOne extends Fragment {
    TextView dp2,dp1;
    DatePickerDialog datePickerDialog;
    wifiHotSpots hotutil;
    String message = "";
    ServerSocket serverSocket;
    String msgReply;
    SharedPreferences sp;
    String sentPassword,contents;
    Button barLock,barItem,check,setup;
    EditText LockText,ItemText,quantity;
    ArrayList<String> al;
    WifiManager wm;
    public OneByOne() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_one_by_one, container, false);
        wm = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        al=new ArrayList<String>();
        sp=getActivity().getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        quantity= (EditText) v.findViewById(R.id.enteredQuantity);
        barLock= (Button) v.findViewById(R.id.lockBarcodeRead);
        barItem= (Button) v.findViewById(R.id.itemBarcodeRead);
        check= (Button) v.findViewById(R.id.check);
        setup=(Button)v.findViewById(R.id.setup);
        LockText = (EditText) v.findViewById(R.id.enteredLockBarcode);
        ItemText= (EditText) v.findViewById(R.id.enteredItemBarcode);
        hotutil=new wifiHotSpots(getActivity());
       // hotutil.startHotSpot(true);
        Thread socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();
        dp1 = (TextView) v.findViewById(R.id.Production);
        dp2 = (TextView) v.findViewById(R.id.Expiry);
        dp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text

                                dp1.setText("Production : "+dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        dp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text

                                dp2.setText("Expiry : "+dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        barLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                intent.setAction("com.google.zxing.client.android.SCAN");
                intent.putExtra("SAVE_HISTORY", false);
                startActivityForResult(intent, 0);
            }
        });
        barItem.setOnClickListener(new View.OnClickListener() {
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
                String s= sp.getString("lockDetails","");
                if (!s.equals("") && !LockText.getText().toString().trim().equals(""))
                {
                    try {
                        JSONArray j = new JSONArray(s);
                        for (int i=0;i<j.length();i++)
                        {
                            JSONObject l =j.getJSONObject(i);
                            al.add(l.getString("lockSno"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (al.contains(LockText.getText().toString().trim()))
                    {
                        Toast.makeText(getActivity(),"Lock belongs to the Supplier",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Lock doesn't belong to the Supplier",Toast.LENGTH_SHORT).show();
                    }
                }
                String t= sp.getString("ItemsDetails","");
                if (!t.equals("") && !ItemText.getText().toString().equals(""))
                {
                    try {
                        JSONArray j = new JSONArray(t);
                        for (int i=0;i<j.length();i++)
                        {
                            JSONObject l =j.getJSONObject(i);
                            al.add(l.getString("itemBarcode"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (al.contains(ItemText.getText().toString().trim()))
                    {
                        Toast.makeText(getActivity(),"Item belongs to the Supplier",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Item doesn't belong to the Supplier",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!quantity.getText().toString().equals(""))
                {
                  if (!LockText.getText().toString().equals("") )//&& !ItemText.getText().toString().equals(""))
                  {
                    if (!dp1.getText().toString().equals("Select Production Date")&& !dp2.getText().toString().equals("Select Expiry Date"))
                    {
                        hotutil.startHotSpot(true);
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Enter Production and Expiry date",Toast.LENGTH_SHORT).show();
                    }
                  }
                  else {
                      Toast.makeText(getActivity(),"Enter Both Barcodes First",Toast.LENGTH_SHORT).show();
                  }
                }
                else {
                    Toast.makeText(getActivity(),"Enter Quantity",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return  v;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                contents = data.getStringExtra("result");
                LockText.setText(contents);
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
                Toast.makeText(getActivity(), "No Output", Toast.LENGTH_LONG).show();
            }
        }
        else if (resultCode ==1)
        {
            if (resultCode == RESULT_OK) {
                contents = data.getStringExtra("result");
                ItemText.setText(contents);
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
                Toast.makeText(getActivity(), "No Output", Toast.LENGTH_LONG).show();
            }
        }
    }
    private class SocketServerThread extends Thread {

        static final int SocketServerPORT = 5000;
        int count = 0;

        @Override
        public void run() {
            Socket socket = null;
            DataInputStream dataInputStream = null;
            DataOutputStream dataOutputStream = null;

            try {
                serverSocket = new ServerSocket(SocketServerPORT);
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // info.setText("I'm waiting here: "+ serverSocket.getLocalPort());
                    }
                });
                while (true) {
                    socket = serverSocket.accept();
                    dataInputStream = new DataInputStream(
                            socket.getInputStream());
                    dataOutputStream = new DataOutputStream(
                            socket.getOutputStream());

                    String messageFromClient = "";

                    //If no message sent from client, this code will block the program
                    messageFromClient = dataInputStream.readUTF();
                   /* if (messageFromClient.length()==10)
                    {
                        //hotutil.shredAllWifi();
                        //static as of now
                        msgReply ="9839386601";
                       // msgReply= "Hotspot Name : "+transfer.getString("OpHotspot",null)+"\n"+"Hotspot Password : "+transfer.getString("OpHotpassword",null)+"\n"+"Password Requirement : "+transfer.getString("OpPwNeeded",null)+"\n"+"Operator Setup Status : "+transfer.getInt("OperatorSetupStatus",0);
                    }*/
                    //else if (HomeFragment.al.contains(messageFromClient))
                    if (messageFromClient.equals("SUCCESS1"))
                    {
                        msgReply= "^"+sp.getString("userId","")+"*"+sp.getString("appId","");
                        messageFromClient= "Supplier Id and App Id Successfully Sent : " +msgReply;
                        // password.setText(msgReply);
                    }
                    //else if (messageFromClient.equals("SUCCESS2"))
                    else if (messageFromClient.contains("SUCCESS2"))
                    {
                        msgReply="~"+(int) (Math.random() * 10000);
                        sentPassword = "Password : "+ msgReply.replace('~',' ').trim();
                        messageFromClient= "Password Successfully Sent : " +msgReply;
                        // Toast.makeText(getActivity(), "Supplier ID matched",Toast.LENGTH_LONG).show();
                        //Here send all that data to website too
                    }
                    else if (messageFromClient.equals("SUCCESS3"))
                    {
                        msgReply="DISCONNECT";
                        messageFromClient= "Data Exchange Successful : " + messageFromClient;
                        //Toast.makeText(getActivity(), "Password sent Successfully",Toast.LENGTH_LONG).show();
                        //Close Hotspot
                        hotutil.startHotSpot(false);
                        //Switch ON wifi
                        wm.setWifiEnabled(true);
                        //Here send all that data to website too
                    }
                    else if (messageFromClient.equals("1234567890"))//(Case for serial number)
                    {
                        //Toast.makeText(getActivity(), "Error in connection",Toast.LENGTH_LONG).show();
                        //Send serial number to the website and based on response(Barcode image and barcode setup status of the lock),send details to the lock
                        msgReply="9839386601";
                    }
                    else if (messageFromClient.equals("FAIL1"))
                    {
                        messageFromClient = "Secret Numbers don't match : " + messageFromClient;
                        msgReply="DISCONNECT";
                    }
                    else if (messageFromClient.equals("FAIL2"))
                    {
                        messageFromClient = "Unauthorized Supplier : " + messageFromClient;
                        msgReply="DISCONNECT";
                    }
                    else if (messageFromClient.equals("FAIL3"))
                    {
                        messageFromClient = "Password sending failed : " + messageFromClient;
                        msgReply="DISCONNECT";
                    }
                    else
                    {
                        messageFromClient = "Unexpected response from the lock : " + messageFromClient;
                        msgReply ="DISCONNECT";
                    }
                    count++;
                    message += "#" + count + " from " + socket.getInetAddress()
                            + ":" + socket.getPort() + "\n"
                            + "Message from client: " + messageFromClient + "\n";

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
                            //password.setText(sentPassword);
                            /*If (message.contains(SUCCESS3))
                            {
                                SEND PASSWORD AND ITEM DETAILS TO THE WEBSITE
                            }*/

                        }
                    });

                    /*String msgReply = "Hotspot Name : "+" Umar "+"\n" + "Hotspot Password : "+" 1234ab "+"\n"+"Password Requirement : "+" Yes "+"\n";*/
                    dataOutputStream.writeUTF(msgReply);

                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                final String errMsg = e.toString();
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),errMsg,Toast.LENGTH_LONG).show();
                    }
                });

            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (dataInputStream != null) {
                    try {
                        dataInputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if (dataOutputStream != null) {
                    try {
                        dataOutputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }

    }
}
