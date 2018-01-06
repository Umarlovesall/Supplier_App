package com.moadd.operatorApp.fragment;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.android.CaptureActivity;
import com.moadd.operatorApp.BarcodeResultSend;
import com.moadd.operatorApp.ConnectedItemPojo;
import com.moadd.operatorApp.ItemLockform1;
import com.moadd.operatorApp.Login;
import com.moadd.operatorApp.wifiHotSpots;
import com.moaddi.operatorApp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class OneByOne extends Fragment {
    TextView dp2,dp1;
    ArrayList<ItemLockform1> history;
    MyAdapter m;
    ListView lv;
    DatePickerDialog datePickerDialog;
    wifiHotSpots hotutil;
    String message = "";
    ServerSocket serverSocket;
    String msgReply;
    SharedPreferences sp,allItemsDetails;
    String sentPassword,contents;
    Button barLock,barItem,check,setup;
    EditText LockText,ItemText,quantity;
    ArrayList<String> al;
    WifiManager wm;
    ItemLockform1 ilf;
    public OneByOne() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_one_by_one, container, false);
        wm = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        history=new ArrayList<ItemLockform1>();
        al=new ArrayList<String>();
        lv= (ListView) v.findViewById(R.id.listview);
        m=new MyAdapter();
        lv.setAdapter(m);
        ilf=new ItemLockform1();
        sp=getActivity().getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        allItemsDetails=getActivity().getSharedPreferences("AllLocksItemsMachines",MODE_PRIVATE);
        quantity= (EditText) v.findViewById(R.id.enteredQuantity);
        barLock= (Button) v.findViewById(R.id.lockBarcodeRead);
        barItem= (Button) v.findViewById(R.id.itemBarcodeRead);
        check= (Button) v.findViewById(R.id.check);
        setup=(Button)v.findViewById(R.id.setup);
        LockText = (EditText) v.findViewById(R.id.enteredLockBarcode);
        ItemText= (EditText) v.findViewById(R.id.enteredItemBarcode);
        hotutil=new wifiHotSpots(getActivity());
        ilf.setUserRoleId(Long.valueOf(Login.userRoleId));
        //ilf.setUserRoleId(Long.valueOf("21"));
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
              // new HttpRequestTask2().execute();
            }
        });
        setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!quantity.getText().toString().equals(""))
                {
                  if (!LockText.getText().toString().equals("") && !ItemText.getText().toString().equals(""))
                  {
                    if (!dp1.getText().toString().equals("Select Production Date")&& !dp2.getText().toString().equals("Select Expiry Date"))
                    {
                        //hotutil.startHotSpot(true);
                        new HttpRequestTask().execute();
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
                        String x=""+(int) (Math.random() * 10000);
                        msgReply="~"+x;
                        sentPassword = "Password : "+ msgReply.replace('~',' ').trim();
                        messageFromClient= "Password Successfully Sent : " +msgReply;
                        ilf.setPassword(x);
                        // Toast.makeText(getActivity(), "Supplier ID matched",Toast.LENGTH_LONG).show();
                        //Here send all that data to website too
                    }
                    else if (messageFromClient.equals("SUCCESS3"))
                    {
                        msgReply="DISCONNECT";
                       /* //Close Hotspot
                        hotutil.startHotSpot(false);
                        //Switch ON wifi
                        wm.setWifiEnabled(true);*/
                        messageFromClient= "Data Exchange Successful : " + messageFromClient;
                        new HttpRequestTask2().execute();
                        //Toast.makeText(getActivity(), "Password sent Successfully",Toast.LENGTH_LONG).show();
                        //Here save all that data to send later to the website too
                        //new HttpRequestTask2().execute();
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
                   /* message += "#" + count + " from " + socket.getInetAddress()
                            + ":" + socket.getPort() + "\n"
                            + "Message from client: " + messageFromClient + "\n";*/
                    message = "#" + count + " from " + socket.getInetAddress()
                            + ":" + socket.getPort() + "\n"
                            + "Message from client: " + messageFromClient + "\n";

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
                            /*if (message.contains("SUCCESS3"))
                            {
                                new HttpRequestTask2().execute();
                            }*/
                            //For Data showing in listview down below :
                           /* if (message.contains("SUCCESS3"))
                            {
                                //Here set the data in the below listview
                                ConnectedItemPojo c=new ConnectedItemPojo();
                                c.setLockBarcode(LockText.getText().toString().trim());
                                c.setItemBarcode(ItemText.getText().toString().trim());
                                c.setQuantity(quantity.getText().toString().trim());
                                String str= allItemsDetails.getString("ItemsDetails","");
                                try {
                                    JSONArray j = new JSONArray(str);
                                    for (int i=0;i<j.length();i++)
                                    {
                                        JSONObject l =j.getJSONObject(i);
                                        if (ItemText.getText().toString().trim().equals(l.getString("itemBarcode")))
                                        {
                                            c.setPrice(""+Integer.parseInt(quantity.getText().toString().trim())*Integer.parseInt(l.getString("price")));
                                            history.add(c);
                                            m.notifyDataSetChanged();
                                        }
                                    }
                                    //aa.notifyDataSetChanged();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }*/
                            //password.setText(sentPassword);
                            /*If (message.contains(SUCCESS3))
                            {
                                SEND PASSWORD AND ITEM DETAILS TO THE WEBSITE
                            }*/

                        }
                    });

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
   /* private class HttpRequestTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                //The link on which we have to POST data and in return it will return some data
                //String URL = "https://www.moaddi.com/moaddi/supplier/serviessupplieritemsthroughbarcode1.htm";
                String URL = "http://192.168.0.102:8080/Moaddi1/supplier/serviessupplieritemsthroughbarcode1.htm";
                //Create and set object 'l' of bean class LoginForm,which we will POST then
                ItemLockform1 ilf=new ItemLockform1();
                ilf.setUserRoleId(Long.valueOf(Login.userRoleId));
                ilf.setExpireDate(dp2.getText().toString());
                ilf.setProductionDate(dp1.getText().toString());
                ilf.setIquantity(Long.valueOf(quantity.getText().toString()));
                //b.setUserRoleId(Login.userRoleId);
               // b.setUserRoleId("21");
                //Use RestTemplate to POST(within Asynctask)
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                //postforobject method POSTs data to server and brings back LoginForm object format data.
                String lf = restTemplate.postForObject(URL, ilf, String.class);
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
            //Toast.makeText(getActivity(),lf,Toast.LENGTH_LONG).show();
            if (lf!=null) {
                //Saving all details of the items in separate field so as to use it later for fetching "price" and other details of the items.

            }
            else
            {

            }

        }
    }*/
   public class HttpRequestTask extends AsyncTask<Void, Void,String > {
       // String a=null;
       String la=null;
       @Override
       public  String doInBackground(Void... params) {
           try {
               //The link on which we have to POST data and in return it will return some data
               String URL = "https://www.moaddi.com/moaddi/supplier/servieschecklock.htm";
               //String URL = "http://192.168.0.102:8080/Moaddi1/supplier/servieschecklock.htm";
               BarcodeResultSend l = new BarcodeResultSend();
               l.setUserRoleId(Login.userRoleId);
               //l.setUserRoleId("21");
               l.setBarcode(LockText.getText().toString().trim());
               //Use RestTemplate to POST(within Asynctask)
               RestTemplate restTemplate = new RestTemplate();
               restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
               la = restTemplate.postForObject(URL,l,String.class);
               return la;
           } catch (Exception e) {
               Log.e("LoginActivity", e.getMessage(), e);

           }

           return null;
       }

       @Override
       protected void onPostExecute(String  la) {
           //The returned object of LoginForm that we recieve from postforobject in doInBackground is displayed here.
          // Toast.makeText(getActivity(),la,Toast.LENGTH_LONG).show();
           if (la!=null)
           {
               String arr[]=la.split("/");
               ilf.setLockId(Long.valueOf(arr[0]));
               new HttpRequestTask1().execute();
           }
           else if(la.equals("lockIsNotThere"))
           {
               Toast.makeText(getActivity(),"Lock Doesn't belong to this Supplier",Toast.LENGTH_LONG).show();
           }
           else
           {
               Toast.makeText(getActivity(),"Connection Error",Toast.LENGTH_LONG).show();
           }
       }

   }
    public class HttpRequestTask1 extends AsyncTask<Void, Void,String > {
        // String a=null;
        String la=null;
        @Override
        public  String doInBackground(Void... params) {
            try {
                //The link on which we have to POST data and in return it will return some data
                String URL = "https://www.moaddi.com/moaddi/supplier/serviescheckitemprice.htm";
                //String URL = "http://192.168.0.102:8080/Moaddi1/supplier/serviescheckitemprice.htm";
                BarcodeResultSend l = new BarcodeResultSend();
                l.setUserRoleId(Login.userRoleId);
                //l.setUserRoleId("21");
                l.setBarcode(ItemText.getText().toString().trim());
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                la = restTemplate.postForObject(URL,l,String.class);
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

                if (la.equals("ItemIsNotThere"))
                {
                    Toast.makeText(getActivity(),"Item Doesn't belong to this Supplier",Toast.LENGTH_LONG).show();
                }
                else if(la.equals(""))
                {
                    Toast.makeText(getActivity(),"Poor Connection",Toast.LENGTH_LONG).show();
                }
                else {
                    try {
                        Toast.makeText(getActivity(), la, Toast.LENGTH_LONG).show();
                        JSONObject j = new JSONObject(la);
                        ilf.setPrice(j.getDouble("price"));
                        ilf.setItemId(j.getLong("itemId"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //new HttpRequestTask2().execute();
                    hotutil.startHotSpot(true);
                }


        }

    }
    public class HttpRequestTask2 extends AsyncTask<Void, Void,String > {
        // String a=null;
        String la=null;
        @Override
        public  String doInBackground(Void... params) {
            try {
                String URL = "https://www.moaddi.com/moaddi/supplier/serviesitemlock.htm";
                //String URL = "http://192.168.0.107:8080/Moaddi1/supplier/serviesitemlock.htm";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                ilf.setExpireDate(dp2.getText().toString().substring(9));
                ilf.setProductionDate(dp1.getText().toString().substring(13));
                ilf.setIquantity(Long.valueOf(quantity.getText().toString()));
               /* ilf.setUserRoleId(Long.valueOf("21"));
                ilf.setPassword("abcde");
                ilf.setProductionDate("10-5-2010");
                ilf.setExpireDate("10-2-2017");
                ilf.setIquantity(Long.valueOf("5"));
                ilf.setItemId(Long.valueOf("42"));
                ilf.setLockId(Long.valueOf("43"));
                ilf.setPrice(10.00);*/
               /* history.add(ilf);
                m.notifyDataSetChanged();*/
                la = restTemplate.postForObject(URL,ilf,String.class);
                return la;
            } catch (Exception e) {
                Log.e("LoginActivity", e.getMessage(), e);

            }
            return null;
        }
        @Override
        protected void onPostExecute(String  la)
        {
           /* history.add(ilf);
            m.notifyDataSetChanged();*/
            Toast.makeText(getActivity(),""+ilf.getProductionDate()+" "+ilf.getExpireDate()+" "+ilf.getItemId()+" "+ilf.getLockId()+" "+ilf.getPrice()+" "+ilf.getPassword(),Toast.LENGTH_LONG).show();
            Toast.makeText(getActivity(),la,Toast.LENGTH_LONG).show();
            if (la!=null)
            {
if (la.equals("success") || la.equals("success1"))
{
    Toast.makeText(getActivity(),"Item and lock connection successfull",Toast.LENGTH_LONG).show();
    history.add(ilf);
    m.notifyDataSetChanged();
}
else
{
    Toast.makeText(getActivity(),la,Toast.LENGTH_SHORT).show();
}

            }
            else
            {
                Toast.makeText(getActivity(),"Poor Internet Connection",Toast.LENGTH_LONG).show();
            }

        }
    }
    public class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return al.size();
        }

        @Override
        public Object getItem(int i) {
            return al.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View vs=getActivity().getLayoutInflater().inflate(R.layout.row_one_by_one,null);
            ItemLockform1 lp = history.get(i);
            TextView tv1 = (TextView) vs.findViewById(R.id.tv1);
            TextView tv2 = (TextView) vs.findViewById(R.id.tv2);
            TextView tv3 = (TextView) vs.findViewById(R.id.tv3);
            TextView tv4 = (TextView) vs.findViewById(R.id.tv4);
            TextView tv5 = (TextView) vs.findViewById(R.id.tv5);
            tv1.setText(LockText.getText().toString().trim());
            tv2.setText(ItemText.getText().toString().trim());
            tv3.setText(quantity.getText().toString().trim());
            tv4.setText(""+lp.getPrice());
            tv5.setText("");
            return vs;
        }
    }
}
