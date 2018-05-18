package com.moadd.DemoLockApp.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.moadd.DemoLockApp.Login;
import com.moadd.DemoLockApp.WifiStatus;
import com.moadd.DemoLockApp.wifiHotSpots;
import com.moaddi.operatorApp.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements NetworkStateReceiver.NetworkStateReceiverListener{
    BroadcastReceiver receiver;
    private NetworkStateReceiver networkStateReceiver;
    WifiManager wm;
    wifiHotSpots hotutil;
    Button wifi;
    SharedPreferences AllDetails;
    SharedPreferences.Editor AllDetailsEt;
    TextView data,p2,p4,p3,p1,barcode;
    Button type,settings;
    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_home, container, false);
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        getActivity().registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
        wifi= (Button) v.findViewById(R.id.wifi);
        p2= (TextView) v.findViewById(R.id.p2);
        p4= (TextView) v.findViewById(R.id.p4);
        p1= (TextView) v.findViewById(R.id.p1);
        p3= (TextView) v.findViewById(R.id.p3);
        data= (TextView) v.findViewById(R.id.data);
        barcode= (TextView) v.findViewById(R.id.tv);
        type= (Button) v.findViewById(R.id.type);
        settings= (Button) v.findViewById(R.id.settings);
        AllDetails=getActivity().getSharedPreferences(Login.selectedlockBarcode,Context.MODE_PRIVATE);
        AllDetailsEt=AllDetails.edit();
        data.setMovementMethod(new ScrollingMovementMethod());
        wm=(WifiManager)  getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        getActivity().registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Lock_Setup_Details fragment = new Lock_Setup_Details();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        if (Login.category.equals("Operator"))
        {
            type.setText("Operator");
        }
        else
        {
            type.setText("Supplier");
        }
        if (wm.isWifiEnabled())
        {
            wifi.setText("Click to turn OFF Wi Fi");
            p2.setBackgroundColor(Color.parseColor("#0000ff"));
        }
        else
        {
            wifi.setText("Click to turn ON Wi Fi");
            p2.setBackgroundColor(Color.parseColor("#293d68"));
        }
        if(!AllDetails.getString("Password Of the Lock","").equals(""))
        {
         p4.setBackgroundColor(Color.parseColor("#00ff00"));
        }
        else
        {
            p4.setBackgroundColor(Color.parseColor("#ff0000"));

        }
        //Permissions for switching on/off wifi access :
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(getActivity())) {
                // Do stuff here
            } else {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
        hotutil = new wifiHotSpots(getActivity());
        String barcode_data = Login.selectedlockBarcode;
        barcode.setText(barcode_data);
        // barcode image
        Bitmap bitmap = null;
        ImageView iv = (ImageView) v.findViewById(R.id.barcodeImage);

        try {

            bitmap = encodeAsBitmap(barcode_data, BarcodeFormat.CODE_128, 600, 300);
            iv.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }
        wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wm.isWifiEnabled())
                {
                    disconnectWifi();
                    wifi.setText("Click to turn ON Wi Fi");
                    p2.setBackgroundColor(Color.parseColor("#293d68"));
                }
                else
                {
                    connectToWifi();
                    wifi.setText("Click to turn OFF Wi Fi");
                    p2.setBackgroundColor(Color.parseColor("#0000ff"));
                }
            }
        });
        return v;
    }
    Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }
    public class MyClientTask extends AsyncTask<Void, Void, Void> {

        String dstAddress;
        int dstPort;
        String response = "";
        String msgToServer;

        MyClientTask(String addr, int port, String msgTo) {
            dstAddress = addr;
            dstPort = port;
            msgToServer = msgTo;
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Socket socket = null;
            DataOutputStream dataOutputStream = null;
            DataInputStream dataInputStream = null;

            try {
                socket = new Socket(dstAddress, dstPort);
                dataOutputStream = new DataOutputStream(
                        socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());

                if (msgToServer != null) {
                    dataOutputStream.writeUTF(msgToServer);
                }

                response = dataInputStream.readUTF();

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
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

                if (dataInputStream != null) {
                    try {
                        dataInputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
           /*if (response.equals("DISCONNECT"))
           {
               //Disconnect wifi and switch it off
           }
           if(response.equals("RESET"))
           {
               //Reset All data except barcode details
               Toast.makeText(getApplicationContext(),"Reset Complete",
                       Toast.LENGTH_SHORT).show();
               SuccessfullDataExchange s = new SuccessfullDataExchange("192.168.43.1",5000,"SUCCESSRESET");
               s.execute();
           }
           else
           {
               if (response.equals("9839386601"))
               {
                   SuccessfullDataExchange s = new SuccessfullDataExchange("192.168.43.1",5000,"SUCCESS1");
                   s.execute();
               }
               else
               {
                   SuccessfullDataExchange s = new SuccessfullDataExchange("192.168.43.1",5000,"FAIL1");
                   s.execute();
               }
           }*/
            if (response.equals(Login.selectedSecretNo))
            {
                SuccessfullDataExchange s = new SuccessfullDataExchange("192.168.43.1",5000,"SUCCESS1");
                s.execute();
                data.setText(data.getText()+" \n "+"Sent :"+" \n "+Login.selectedlockBarcode+" \n "+"Recieved :"+" \n "+"Secret Number : "+response);
                AllDetailsEt.putString("LockSerialNumber",Login.selectedlockBarcode).apply();
            }
            else
            {
                SuccessfullDataExchange s = new SuccessfullDataExchange("192.168.43.1",5000,"FAIL1");
                s.execute();
             /*   if (response!=null) {
                    data.setText(data.getText() + " \n " + "Sent :" + " \n " + "A020A065EBF3" + " \n " + "Recieved :" + " \n " + "Wrong Secret Number : " + response);
                }*/
            }
            super.onPostExecute(result);
        }
    }
    public class SuccessfullDataExchange extends AsyncTask<Void, Void, Void> {

        String dstAddress;
        int dstPort;
        String response = "";
        String msgToServer;

        SuccessfullDataExchange(String addr, int port, String msgTo) {
            dstAddress = addr;
            dstPort = port;
            msgToServer = msgTo;
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Socket socket = null;
            DataOutputStream dataOutputStream = null;
            DataInputStream dataInputStream = null;

            try {
                socket = new Socket(dstAddress, dstPort);
                dataOutputStream = new DataOutputStream(
                        socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());

                if (msgToServer != null) {
                    dataOutputStream.writeUTF(msgToServer);
                }

                response = dataInputStream.readUTF();

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
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

                if (dataInputStream != null) {
                    try {
                        dataInputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // textResponse.setText(response);
            if (response.equals("DISCONNECT"))
            {
                //Disconnect wifi and switch off
                data.setText(data.getText()+" \n "+"Sent :"+" \n "+"SUCCESS7/SUCCESSRESET/SUCCESS3/FAIL1"+" \n "+"Recieved :"+" \n "+"DISCONNECT");
                wm.setWifiEnabled(false);
            }
            else if (response!=null && response.charAt(0)=='&')
            {
                //parse and save data of the setup
                SuccessfullDataExchange s = new SuccessfullDataExchange("192.168.43.1",5000,"SUCCESS2");
                s.execute();
                //Toast.makeText(Client.this,response,Toast.LENGTH_LONG).show();
                data.setText(data.getText()+" \n "+"Sent :"+" \n "+"SUCCESS1"+" \n "+"Recieved :"+" \n "+"Date,Time and WifiLimit : "+response);
                if(AllDetails.getString("Date,Time and WifiLimit","").equals(""))
                {
                    AllDetailsEt.putString("Date,Time and WifiLimit", response.replace('&', ' ').trim()).apply();
                }
            }
            else if (response!=null && response.charAt(0)=='%')
            {
                //parse and save data of the setup
                SuccessfullDataExchange s = new SuccessfullDataExchange("192.168.43.1",5000,"SUCCESS3");
                s.execute();
                //Toast.makeText(Client.this,response,Toast.LENGTH_LONG).show();
                data.setText(data.getText()+" \n "+"Sent :"+" \n "+"SUCCESS2"+" \n "+"Recieved :"+" \n "+"Operator Id and App Id : "+response);
                if (!AllDetails.getString("Operator Id and App Id","").equals(""))
                {
                    AllDetailsEt.putString("Operator Id and App Id", response.replace('%', ' ').trim()).apply();
                }
            }
            else if (response!=null && response.charAt(0)=='$')
            {
                //parse and save data of the setup
                SuccessfullDataExchange s = new SuccessfullDataExchange("192.168.43.1",5000,"SUCCESS4");
                s.execute();
                //Toast.makeText(Client.this,response,Toast.LENGTH_LONG).show();
                data.setText(data.getText()+" \n "+"Sent :"+" \n "+"SUCCESS3"+" \n "+"Recieved :"+" \n "+"Operator Hotspot Details : "+response);
                if (!AllDetails.getString("Operator Hotspot Details","").equals(""))
                {
                    AllDetailsEt.putString("Operator Hotspot Details", response.replace('$', ' ').trim()).apply();
                }
            }
            else if (response!=null && response.charAt(0)=='@')
            {
                //parse and save data of the setup
                SuccessfullDataExchange s = new SuccessfullDataExchange("192.168.43.1",5000,"SUCCESS5");
                s.execute();
                //Toast.makeText(Client.this,response,Toast.LENGTH_LONG).show();
                data.setText(data.getText()+" \n "+"Sent :"+" \n "+"SUCCESS4"+" \n "+"Recieved :"+" \n "+"Supplier Hotspot Details : "+response);
                if (!AllDetails.getString("Supplier Hotspot Details","").equals("")) {
                    AllDetailsEt.putString("Supplier Hotspot Details", response.replace('@', ' ').trim()).apply();
                }
            }
            else if (response!=null && response.charAt(0)=='!')
            {
                //parse and save data of the setup
                SuccessfullDataExchange s = new SuccessfullDataExchange("192.168.43.1",5000,"SUCCESS6");
                s.execute();
                //Toast.makeText(Client.this,response,Toast.LENGTH_LONG).show();
                data.setText(data.getText()+" \n "+"Sent :"+" \n "+"SUCCESS5"+" \n "+"Recieved :"+" \n "+"Connected Suppliers IDs and App IDs : "+response);
                if (!AllDetails.getString("Connected Suppliers IDs and App IDs","").equals("")){
                AllDetailsEt.putString("Connected Suppliers IDs and App IDs",response.replace('!',' ').trim()).apply();
                }
            }
            //Responses for Supplier App :
            else if (response!=null && response.charAt(0)=='^')
            {
                //parse and save data of the setup
                SuccessfullDataExchange s = new SuccessfullDataExchange("192.168.43.1",5000,"SUCCESS2");
                s.execute();
                //Toast.makeText(Client.this,response,Toast.LENGTH_LONG).show();
                data.setText(data.getText()+" \n "+"Sent :"+" \n "+"SUCCESS1"+" \n "+"Recieved :"+" \n "+"Supplier IDs(Coming via supplier app connection) : "+response);
              if(AllDetails.getString("Supplier IDs(Coming via supplier app connection)","").equals("")) {
                  AllDetailsEt.putString("Supplier IDs(Coming via supplier app connection)", response.replace('^', ' ').trim()).apply();
              }
            }
            else if (response!=null && response.charAt(0)=='~')
            {
                //parse and save data of the setup
                SuccessfullDataExchange s = new SuccessfullDataExchange("192.168.43.1",5000,"SUCCESS3");
                s.execute();
                //Toast.makeText(Client.this,response,Toast.LENGTH_LONG).show();
                data.setText(data.getText()+" \n "+"Sent :"+" \n "+"SUCCESS2"+" \n "+"Recieved"+" \n "+"Password Of the Lock : "+response);
                if (AllDetails.getString("Password Of the Lock","").equals("")) {
                    AllDetailsEt.putString("Password Of the Lock", response.replace('~', ' ').trim()).apply();
                }
            }
            else if (response.equals("RESET"))
            {
                SuccessfullDataExchange s = new SuccessfullDataExchange("192.168.43.1",5000,"SUCCESSRESET");
                s.execute();
                data.setText(data.getText()+" \n "+"Sent :"+" \n "+"SUCCESS1"+" \n "+"Recieved :"+" \n "+"Reset command : "+response);
                AllDetailsEt.putString("Connected Suppliers IDs and App IDs","").apply();
                AllDetailsEt.putString("Date,Time and WifiLimit","").apply();
                AllDetailsEt.putString("Password Of the Lock","").apply();
                AllDetailsEt.putString("Supplier IDs(Coming via supplier app connection)","").apply();
                AllDetailsEt.putString("Supplier Hotspot Details","").apply();
                AllDetailsEt.putString("Operator Hotspot Details","").apply();
                AllDetailsEt.putString("Operator Id and App Id","").apply();
            }
            else if (response!=null && response.charAt(0)=='-')
            {
                SuccessfullDataExchange s = new SuccessfullDataExchange("192.168.43.1",5000,"SUCCESS7");
                s.execute();
                data.setText(data.getText()+" \n "+"Sent :"+" \n "+"SUCCESS6"+" \n "+"Recieved :"+" \n "+"Barcode Image transfer done");
              /* InputStream stream = new ByteArrayInputStream(Base64.decode(response.replace('-',' ').trim().getBytes(), Base64.DEFAULT));
                Bitmap bitmap = BitmapFactory.decodeStream(stream);
                iv.setImageBitmap(bitmap);*/
                // barcode image
                Toast.makeText(getActivity(),response.replace('-',' ').trim(),Toast.LENGTH_SHORT).show();
              /*  Bitmap bitmap = null;
                try {

                    bitmap = encodeAsBitmap(barcode_data, BarcodeFormat.CODE_128, 600, 300);
                    iv.setImageBitmap(bitmap);

                } catch (WriterException e) {
                    e.printStackTrace();
                }
*/
               /* try {
                    JSONObject j=new JSONObject(response.replace('-',' ').trim());
                    InputStream stream = new ByteArrayInputStream(Base64.decode(j.getString("data").getBytes(), Base64.DEFAULT));
                    Bitmap bitmap = BitmapFactory.decodeStream(stream);
                    iv.setImageBitmap(bitmap);
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/

            }
            super.onPostExecute(result);
        }
    }
    public void connectToWifi(){
        try{
            WifiManager wifiManager = (WifiManager) super.getActivity().getApplicationContext().getSystemService(android.content.Context.WIFI_SERVICE);
            WifiConfiguration wc = new WifiConfiguration();
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            wc.SSID = "333";
            //wc.preSharedKey = "\"PASSWORD\"";
            wc.status = WifiConfiguration.Status.ENABLED;
            wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            wc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            wifiManager.setWifiEnabled(true);
            int netId = wifiManager.addNetwork(wc);
           /* if (netId == -1) {
                netId = getExistingNetworkId(SSID);
            }*/
          /*  wifiManager.disconnect();
            wifiManager.enableNetwork(netId, true);
            wifiManager.reconnect();*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void disconnectWifi(){
        try{
            WifiManager wifiManager = (WifiManager) super.getActivity().getApplicationContext().getSystemService(android.content.Context.WIFI_SERVICE);
            WifiConfiguration wc = new WifiConfiguration();
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            wc.SSID = "333";
            //wc.preSharedKey = "\"PASSWORD\"";
            wc.status = WifiConfiguration.Status.ENABLED;
            wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            wc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            wifiManager.setWifiEnabled(false);
            int netId = wifiManager.addNetwork(wc);
           /* if (netId == -1) {
                netId = getExistingNetworkId(SSID);
            }*/
          /*  wifiManager.disconnect();
            wifiManager.enableNetwork(netId, true);
            wifiManager.reconnect();*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void joinFriend(final WifiStatus wifiStatus, final wifiHotSpots hotutil) {
        if (wifiStatus.checkWifi(wifiStatus.IS_WIFI_ON)) {
            hotutil.scanNetworks();
            List<ScanResult> results = hotutil.getHotspotsList();
            for (ScanResult result : results) {
                //Toast.makeText(getApplicationContext(), result.SSID + " " + result.level,
                //        Toast.LENGTH_SHORT).show();
                if (result.SSID.equalsIgnoreCase("SSID")) {

                    Toast.makeText(getActivity(), result.SSID + " Found SSID" + result.level,
                            Toast.LENGTH_SHORT).show();
                    hotutil.connectToHotspot("333", "");
                    try {
                        getActivity().unregisterReceiver(receiver);
                        break;
                    } catch (Exception e) {
                        //error as trying to do unregistering twice?
                    }
                }
                //hotutil.stopScan();
            }
        }
        else {
            if (hotutil.isWifiApEnabled())
                hotutil.startHotSpot(false);
            //start wifi.
            wifiStatus.checkWifi(wifiStatus.WIFI_ON);

            receiver = new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {
                    // TODO Auto-generated method stub
                    final String action = intent.getAction();
                    if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                        List<ScanResult> results = hotutil.getHotspotsList();
                        for (ScanResult result : results) {
                            //Toast.makeText(getApplicationContext(), result.SSID + " " + result.level,
                            //        Toast.LENGTH_SHORT).show();
                            if (result.SSID.equalsIgnoreCase("SSID")) {
                                Toast.makeText(getActivity(), "Found SSID", Toast.LENGTH_SHORT).show();
                                if (!hotutil.isConnectToHotSpotRunning)
                                    hotutil.connectToHotspot("SSID", "");
                                try {
                                    getActivity().unregisterReceiver(receiver);
                                    break;
                                } catch (Exception e) {
                                    //trying to unregister twice? need vary careful about this.
                                }

                            }
                        }
                    }
                }

            };
            IntentFilter mIntentFilter = new IntentFilter();
            mIntentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
            getActivity().registerReceiver(receiver, mIntentFilter);
        }
    }

    @Override
    public void networkAvailable() {

        MyClientTask myClientTask = new MyClientTask("192.168.43.1",5000,"A020A605EBF3");
        myClientTask.execute();
    }

    @Override
    public void networkUnavailable() {

    }
    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            if (level<=82)
            {
p3.setBackgroundColor(Color.parseColor("#FFBF00"));
Toast.makeText(getActivity(),"Lock Batter Low",Toast.LENGTH_SHORT).show();
            }
            else
            {
               // p3.setBackgroundColor(Color.parseColor("#00ff00"));
                p3.setBackgroundResource(R.drawable.textview_border);
            }
        }
    };
}
