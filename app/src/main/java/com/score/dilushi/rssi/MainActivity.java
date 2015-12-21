package com.score.dilushi.rssi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button b=(Button)findViewById(R.id.ref_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Updation().execute();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String UpdateRSSI(){
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        String fullpath="";
        for (int i=0;i<wifi.getScanResults().size();i++) {
            ScanResult result0 = wifi.getScanResults().get(i);
            String ssid0 = result0.SSID;
            int rssi0 = result0.level;
            String rssiString0 = String.valueOf(rssi0);
            fullpath=fullpath+" "+ssid0+" -> "+rssiString0+"\n";
        }
        return fullpath;
        //textStatus.append("\n" + ssid0 + "   " + rssiString0);
    }

    class Updation extends AsyncTask <Void,Integer,String>{

        @Override
        protected String doInBackground(Void... voids) {
            return UpdateRSSI();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            TextView tv=(TextView)findViewById(R.id.res);
            tv.setText(s);

        }
    }
}
