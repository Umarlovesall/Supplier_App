package com.moadd.DemoLockApp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.moaddi.operatorApp.R;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

public class Login extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
SharedPreferences sp;
SharedPreferences.Editor et;
EditText userId,password;
Button login;
String selectedItem;
ArrayList<String> IdList;
Spinner type,ids;
LockLoginPojo b;
public static String userRoleId,Id,category,selectedlockBarcode="XXXXXXXXX";
public static String selectedSecretNo="123456789";
ArrayAdapter dataAdapter1;
String item,complaint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        password = (EditText) findViewById(R.id.password);
        userId= (EditText) findViewById(R.id.userId);
        login = (Button) findViewById(R.id.login);
        b=new LockLoginPojo();
        sp=getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        et=sp.edit();
         ids = (Spinner)findViewById(R.id.listOfIds);
         type = (Spinner)findViewById(R.id.select);
        ids.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               selectedItem=IdList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        type.setOnItemSelectedListener( this);
        ArrayList<String> categories = new ArrayList();
        IdList = new ArrayList();
        categories.add("Select account type");
        categories.add("Operator");
        categories.add("Supplier");
        ArrayAdapter dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(dataAdapter);
        dataAdapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, IdList);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ids.setAdapter(dataAdapter1);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!password.getText().toString().equals(null)&& selectedItem!=null) {
                    b.setUserid(selectedItem);
                    b.setPassword(password.getText().toString().trim());
                    b.setUserRole(complaint);
                    new HttpRequestTask1().execute();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Id/Password fields can't be empty",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        item = parent.getItemAtPosition(position).toString();
        complaint=item;
        category=item;
        IdList.clear();
        dataAdapter1.notifyDataSetChanged();
        if (complaint.equals("Operator") || complaint.equals("Supplier"))
        {
            new HttpRequestTask().execute();
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private class HttpRequestTask extends AsyncTask<Void, Void,String> {
        // String a=null;
        String la=null;
        @Override
        public  String doInBackground(Void... params) {
            try {
                //The link on which we have to POST data and in return it will return some data
                //String URL = "https://www.moaddi.com/moaddi/servicegetidsbasedonrole.htm";
                //String URL="http://192.168.0.108:8082/webservices/servicegetidsbasedonrole.htm";
                String URL="https://www.moaddi.com/moaddi/servicegetidsbasedonrole.htm";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                la = restTemplate.postForObject(URL,item,String.class);
                return la;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String m) {
         //   Toast.makeText(getApplicationContext(), m, Toast.LENGTH_LONG).show();
if (m!=null)
{
    m=m.replace('[',' ').replace('"',' ').replace(']',' ').replace(',',' ').trim();
    String arr[]=m.split(" ");
    for (int i=0;i<arr.length;i++)
    {
        if (!arr[i].equals(null) && !arr[i].equals("")) {
            IdList.add(arr[i]);
        }
    }
    dataAdapter1.notifyDataSetChanged();
}
        }
    }
    private class HttpRequestTask1 extends AsyncTask<Void, Void,String> {
        // String a=null;
        String la=null;
        @Override
        public  String doInBackground(Void... params) {
            try {
                //The link on which we have to POST data and in return it will return some data
                String URL = "https://www.moaddi.com/moaddi/serviceforloginapp.htm";
               //String URL="http://192.168.0.110:8082/webservices/serviceforloginapp.htm";
                //String URL="https://www.moaddi.com/moaddi/serviceforloginapp.htm";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                la = restTemplate.postForObject(URL,b,String.class);
                return la;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }
            return null;
        }
        @Override
        protected void onPostExecute(String m) {
           // Toast.makeText(getApplicationContext(), m, Toast.LENGTH_LONG).show();
            if (!m.equals("") && !m.equals(null))
            {
                userRoleId = m;
                Id=selectedItem;
                Intent in=new Intent(Login.this,MainActivity.class);
                startActivity(in);
                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Incorrect UserId/Password.", Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    protected void onRestart()
    {
        super.onRestart();
    }

}