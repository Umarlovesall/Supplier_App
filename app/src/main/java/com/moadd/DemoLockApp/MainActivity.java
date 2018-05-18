package com.moadd.DemoLockApp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.moadd.DemoLockApp.fragment.About;
import com.moadd.DemoLockApp.fragment.AllLocks;
import com.moadd.DemoLockApp.fragment.AllMachines;
import com.moadd.DemoLockApp.fragment.HomeFragment;
import com.moadd.DemoLockApp.fragment.LockSettings;
import com.moadd.DemoLockApp.fragment.Synchronize;
import com.moaddi.operatorApp.R;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sp;
    SharedPreferences.Editor et;
    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    Toolbar toolbar;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    private DataModel[] drawerItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(MainActivity.this)) {
                // Do stuff here
            }
            else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + this.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //flag=1;
            }
        }
        sp=getSharedPreferences("Credentials",MODE_PRIVATE);
        et=sp.edit();
        mTitle = mDrawerTitle = getTitle();
        mNavigationDrawerItemTitles= getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame,new HomeFragment()).commit();
        setupToolbar();

        createMenuOption();

        getSupportActionBar().setHomeButtonEnabled(true);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //toolbar.setNavigationIcon(R.drawable.ic_toolbar);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.list_view_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setupDrawerToggle();

    }

    private void createMenuOption() {


        drawerItem = new DataModel[6];
        drawerItem[0] = new DataModel(R.drawable.ic_home_black_24dp, "Home");
        drawerItem[1] = new DataModel(R.mipmap.synchronization_button_with_two_arrows, "Synchronize");
        drawerItem[2] = new DataModel(R.mipmap.machine, "Machines");
        drawerItem[3] = new DataModel(R.drawable.locks, "Locks");
        drawerItem[4] = new DataModel(R.drawable.aboutus, "About App");
        drawerItem[5] = new DataModel(R.drawable.logouuuuuuu, "Logout");
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    private void selectItem(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new Synchronize();
                break;
            case 2:
                fragment = new AllMachines();
                break;
            case 3:
                fragment = new AllLocks();
                break;
            case 4:
                fragment = new About();
                break;
            case 5:
                Intent in=new Intent(MainActivity.this,Login.class);
                startActivity(in);
                Toast.makeText(this,"Successfully Logged Out",Toast.LENGTH_SHORT).show();
                finish();
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame, fragment).commit();
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(mNavigationDrawerItemTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);

        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    void setupDrawerToggle(){
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
      /*  if (flag!=1) {
            Intent in = new Intent(MainActivity.this, EnterPassword.class);
            startActivity(in);
        }*/
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        et.putInt("LoginStatus",0).apply();

    }
}
