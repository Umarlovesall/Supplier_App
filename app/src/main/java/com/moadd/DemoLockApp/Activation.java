package com.moadd.DemoLockApp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.moaddi.operatorApp.R;


public class Activation extends AppCompatActivity {
    EditText userId,password,activationKey;
    SharedPreferences sp;
    Button activate,restore;
    int n;
    private static final int REQUEST_READ_PHONE_STATE = 0;
    int x=1;
    String userRoleId,IMEI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);
        activate= (Button) findViewById(R.id.activate);
        restore= (Button) findViewById(R.id.restore);
        sp=getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        userId= (EditText) findViewById(R.id.userId);
        password= (EditText) findViewById(R.id.password);
        activationKey= (EditText) findViewById(R.id.ActivationKey);
        // Getting IMEI number for the phone :
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
            //x=2;
        } else {
            //TODO
            x=2;
        }
        if (x==2) {
         /*  TelephonyManager mngr = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
            IMEI= mngr.getDeviceId();*/
            TelephonyManager mTelephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (mTelephony.getDeviceId() != null){
                IMEI= mTelephony.getDeviceId();
            }else{
                IMEI = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }
        activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          // new HttpRequestTask().execute();
            }
        });
        restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(Activation.this,Restore.class);
                startActivity(in);
                finish();
            }
        });
    }
}
