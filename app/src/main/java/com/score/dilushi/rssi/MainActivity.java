package com.score.dilushi.rssi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

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

                Timer t = new Timer();
                t.scheduleAtFixedRate(new TimerTask() {

                                          @Override
                                          public void run() {
                                              //Called each time when 1000 milliseconds (1 second) (the period parameter)
                                              new Updation().execute();
                                          }

                                      },
//Set how long before to start calling the TimerTask (in milliseconds)
                        0,
//Set the amount of time between each execution (in milliseconds)
                        500);
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
/*
    //Declare the timer
    Timer t = new Timer();
    //Set the schedule function and rate
    t.scheduleAtFixedRate(new TimerTask() {

        @Override
        public void run() {
            //Called each time when 1000 milliseconds (1 second) (the period parameter)
        }

    }, 0, 1000);
*/
    public String UpdateRSSI(){
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        // Asanka: we have to scan in each time we want to see new RSSI results
        // for this line to work, we have to add following permissions
        // "android.permission.ACCESS_WIFI_STATE" and "android.permission.CHANGE_WIFI_STATE"
        wifi.startScan();

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

    public void client(String str) throws IOException {
        final String host = null;
        int port;
        byte[] send_data;

        /** Called when the activity is first created. */
        TextView txt5,txt1;
       // byte[] send_data = new byte[1024];
        byte[] receiveData = new byte[1024];
        String modifiedSentence;
        Button bt1,bt2,bt3,bt4;

        //str = "sgdhfhdfd";
        DatagramSocket client_socket = new DatagramSocket(10500);
        InetAddress IPAddress =  InetAddress.getByName("192.168.43.97");

        //while (true)
        // {
        send_data = str.getBytes();
        //System.out.println("Type Something (q or Q to quit): ");

        DatagramPacket send_packet = new DatagramPacket(send_data,str.length(), IPAddress, 10500);
        client_socket.setSoTimeout(500);
        client_socket.send(send_packet);

        //chandra
        //DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        //client_socket.receive(receivePacket);

        client_socket.close();

        // }
    }


    public void Syncer(String s) {
        File sdcard = Environment.getExternalStorageDirectory();
        File unloadfile = new File(sdcard, "/RSSIDATA2.txt");
        /*SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        //Give you current date currentDate = date.format(new Date());SimpleDateFormat time = new SimpleDateFormat("hh:mm a");
        //Give you current time currentTime = time.format(new Date());
        //Store like this filename = currentDate +"-"+currentTime +".mp4";*/
        if (!unloadfile.exists()) {
            // unloadfile.delete();
            try {
                unloadfile.createNewFile();
            } catch (Exception e) {

            }
        }

        FileWriter fw = null;
        try {
            fw = new FileWriter(unloadfile, true);

            fw.append(s);
            fw.append(" -------  ----- \n");
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


        class Updation extends AsyncTask<Void, Integer, String> {

            @Override
            protected String doInBackground(Void... voids) {
                String re = UpdateRSSI();
                try {
                    client(re);
                    //client("Hello World!");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return re;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                TextView tv = (TextView) findViewById(R.id.res);
                s = s + "\n" + Calendar.getInstance().getTime().toString();
                //WriteFile(s, getApplicationContext());
                //Syncer(s);

                tv.setText(s);

            }
        }

        class RTIPacket {

            int sender_id;
            int num_pairs;
            LinkedList<Integer> IdRssiPairList;

            public RTIPacket(int sender, int count) {
                sender_id=sender;
                num_pairs=count;
                IdRssiPairList = new LinkedList();
            }

            public void addIdRssiPair(int id, int rssivalue) {
                IdRssiPairList.add(id);
                IdRssiPairList.add(rssivalue);
            }

            private String getPacketString(){

                return "Packet";
            }
        }

        class IdRssiPair {
            int node_id;
            int rssi;
        }

}
