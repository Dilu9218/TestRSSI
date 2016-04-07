package com.score.dilushi.rssi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import static android.app.PendingIntent.getActivity;

public class MainActivity extends AppCompatActivity {
String filename="";
    public String nodeMAC;
    RTIPacket rtiPacket;
    Timer t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        generatefilename();
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
        Button cfile=(Button)findViewById(R.id.chngfile);
        Button stopbtn=(Button)findViewById(R.id.stop_button);
        stopbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        cfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatefilename();

            }
        });
        Button b=(Button)findViewById(R.id.ref_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                t = new Timer();
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
        Button stop=(Button)findViewById(R.id.stop_button);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            t.cancel();
                t.purge();
                Toast.makeText(getApplicationContext(),"Stop clicked",Toast.LENGTH_LONG).show();
            }
        });
    }
public void generatefilename(){
    filename="RSSIdata";
    SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
    String format = s.format(new Date());
    filename=filename+format+".txt";
    Toast.makeText(getApplicationContext(),filename,Toast.LENGTH_LONG).show();
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

    //public RTIPacket UpdateRSSI(){
    public String UpdateRSSI(){

        //rtiPacket = new RTIPacket();
        //rtiPacket.addIdRssiPair(1, -20);
        //rtiPacket.addIdRssiPair(2, -25);

        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        //WifiInfo wifiInfo = wifi.getConnectionInfo();
        //nodeMAC = wifiInfo.getMacAddress();
        //rtiPacket.sender_id = nodeMAC;

        // Asanka: we have to scan in each time we want to see new RSSI results
        // for this line to work, we have to add following permissions
        // "android.permission.ACCESS_WIFI_STATE" and "android.permission.CHANGE_WIFI_STATE"
        wifi.startScan();

        String fullpath="";
        for (int i=0;i<wifi.getScanResults().size();i++) {
            ScanResult result0 = wifi.getScanResults().get(i);
            String ssid0 = result0.SSID;

            //String bssid0 = result0.BSSID;
            //Toast.makeText(getApplicationContext(), "This is the toast", Toast.LENGTH_SHORT).show();
            //Integer.valueOf(result0.BSSID);
            int rssi0 = result0.level;
            String rssiString0 = String.valueOf(rssi0);
            fullpath=fullpath+ssid0+","+rssiString0+"\n";  //print time in seconds at last

            //rtiPacket.addIdRssiPair(bssid0, rssi0);
        }
        Syncer(fullpath);
        return fullpath;
        //return rtiPacket;
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
        File unloadfile = new File(sdcard, "/"+filename);
        /*SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        //Give you current date currentDate = date.format(new Date());SimpleDateFormat time = new SimpleDateFormat("hh:mm a");
        //Give you current time currentTime = time.format(new Date());
        //Store like this filename = currentDate +"-"+currentTime +".mp4";*/
        if (!unloadfile.exists()) {
            // unloadfile.delete();
            try {
                unloadfile.createNewFile();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }

        FileWriter fw = null;
        try {
            fw = new FileWriter(unloadfile, true);

            fw.append(s);
            fw.append(" ------------ \n");
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
                //rtiPacket = UpdateRSSI();

                /*try {
                    //client(re);
                    //client("Hello World!");

                    /*
                    rtiPacket = new RTIPacket();
                    rtiPacket.addIdRssiPair(1, -20);
                    rtiPacket.addIdRssiPair(2, -25);
                    client(rtiPacket.getPacketString());

                    client(rtiPacket.getPacketString());

                } catch (Exception e) {
                    e.printStackTrace();
                } */

                return re;
               // return rtiPacket.getPacketString();
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

            String sender_id;
            //LinkedList<Integer> idList;
            LinkedList<String> idStringList;
            LinkedList<Integer> rssiList;

            public RTIPacket() {
                sender_id=nodeMAC;
                //idList = new LinkedList();
                idStringList = new LinkedList();
                rssiList = new LinkedList();
            }

            //public void addIdRssiPair(int id, int rssivalue) {
            public void addIdRssiPair(String id, int rssivalue) {
                //idList.add(id);
                idStringList.add(id);
                rssiList.add(rssivalue);
            }

            private int getNumPairs() {
                return rssiList.size();
            }

            private String getPacketString(){

                String packetString = "";

                packetString = String.valueOf(sender_id) + " " + String.valueOf(getNumPairs());

                int size = rssiList.size();

                for(int index=0; index<size; index++) {
                    packetString = packetString + " " + String.valueOf(idStringList.get(index)) + " "
                            + String.valueOf(rssiList.get(index))+"\n";
                }

                return packetString;
            }
        }
}
