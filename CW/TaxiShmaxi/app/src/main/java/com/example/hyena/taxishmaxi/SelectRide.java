package com.example.hyena.taxishmaxi;


import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TimePicker;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SelectRide extends AppCompatActivity {
    PointD start_p = new PointD(), end_p = new PointD();
    HttpRequests httpRequests;
    String signout_url = "http://192.168.0.106:3000/api/users/sign_out";
    String url         = "http://192.168.0.106:3000/api/drivers?time=";
    String authentication_token;
    ListView list_view;
    File token;
    RequestQueue queue;
    JSONArray rides;
    String disponible_rides;
    Intent finish_intent;
    String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_ride);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        token = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/data_taxi.json");
        queue = Volley.newRequestQueue(this);

        authentication_token = getIntent().getStringExtra("authentication_token");
        disponible_rides     = getIntent().getStringExtra("disponible_rides");
        start_p.x            = getIntent().getDoubleExtra("start_point_x", 0.0);
        start_p.y            = getIntent().getDoubleExtra("start_point_y", 0.0);
        end_p.x              = getIntent().getDoubleExtra("end_point_x", 0.0);
        end_p.y              = getIntent().getDoubleExtra("end_point_y", 0.0);

        Log.e("disponible_rides", disponible_rides + "");
        finish_intent = new Intent(this, FInish.class);

        try {
            create_rides_list();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final Intent select_driver = new Intent(this, SelectDriver.class);
        Button create_new = findViewById(R.id.create_n);
        create_new.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(SelectRide.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time = selectedHour + ":" + selectedMinute;
                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        final String date = df.format(c);
                        url = url + date + " " + time;
                        Log.e("date: ", url);

                        httpRequests = new HttpRequests(url, Request.Method.GET);
                        queue.add(httpRequests.createRequest(authentication_token));

                        new Thread(new Runnable() {
                            public void run() {
                                while (true) {
                                    if (httpRequests.response_object != null) {
                                        select_driver.putExtra("disponible_drivers", httpRequests.response_object.toString());
                                        select_driver.putExtra("authentication_token", authentication_token);
                                        select_driver.putExtra("start_point_x", start_p.x);
                                        select_driver.putExtra("start_point_y", start_p.y);
                                        select_driver.putExtra("end_point_x", end_p.x);
                                        select_driver.putExtra("end_point_y", end_p.y);
                                        select_driver.putExtra("time", date + " " + time);
                                        startActivity(select_driver);
                                        finish();
                                        break;
                                    }
                                }
                            }
                        }).start();
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
    }

    public void create_rides_list() throws JSONException, ParseException {
        JSONObject d_rides = new JSONObject(disponible_rides);
        rides              = d_rides.getJSONArray("rides");
        list_view          = findViewById(R.id.listview);

        final HashMap<String, String> hash_data = new HashMap<>();
        for (int i = 0; i < rides.length(); i++) {
            JSONObject obj = rides.getJSONObject(i);
            hash_data.put(obj.getString("driver_name") + " Rating: " + obj.getString("driver_rating"), obj.getString("time"));
        }

        List<HashMap<String, String>> listItems = new ArrayList<>();
        SimpleAdapter adapter = new SimpleAdapter(this, listItems, R.layout.list_item,
                new String[]{"First Line", "Second Line"},
                new int[]{R.id.text1, R.id.text2});

        Iterator it = hash_data.entrySet().iterator();
        while(it.hasNext()){
            HashMap<String, String> resultMap = new HashMap<>();
            Map.Entry pair = (Map.Entry)it.next();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            Date parsedDate = dateFormat.parse(pair.getValue().toString());
            SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy HH:mm");

            Log.e("string", fmtOut.format(parsedDate) + " " );

            resultMap.put("First Line", pair.getKey().toString());
            resultMap.put("Second Line", fmtOut.format(parsedDate));
            listItems.add(resultMap);
        }
        list_view.setAdapter(adapter);

        list_view.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap item = (HashMap) list_view.getItemAtPosition(position);
                String ride_id = "";
                for (int i = 0; i < rides.length(); i++) {
                    JSONObject obj = null;
                    try {
                        obj = rides.getJSONObject(i);

                        if ((obj.getString("driver_name") + " Rating: " + obj.getString("driver_rating")).equals(item.get("First Line"))){
                            ride_id = obj.getString("id");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                String url = "http://192.168.0.106:3000/api/rides/" + ride_id + "/accept_ride";
                Log.e("URL", url);
                httpRequests = new HttpRequests(url, Request.Method.POST);
                queue.add(httpRequests.createRequest(authentication_token));
                startActivity(finish_intent);
                finish();
                Log.e("You selected : ", ""+ item);
            }
        });
    }

    public void create_drivers_list() throws JSONException, ParseException {
        JSONObject d_rides = new JSONObject(disponible_rides);
        rides              = d_rides.getJSONArray("rides");
        list_view          = findViewById(R.id.listview);

        final HashMap<String, String> hash_data = new HashMap<>();
        for (int i = 0; i < rides.length(); i++) {
            JSONObject obj = rides.getJSONObject(i);
            hash_data.put(obj.getString("driver_name") + " Rating: " + obj.getString("driver_rating"), obj.getString("time"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int res_id = item.getItemId();
        if(res_id == R.id.action_settings){
            final Intent start_page = new Intent(this, StartPage.class);
            httpRequests    = new HttpRequests(signout_url, Request.Method.DELETE);
            OutputStream os = null;
            try {
                os = new FileOutputStream(token);
                writeToFile("", os);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            queue.add(httpRequests.createRequest(authentication_token));
            startActivity(start_page);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void writeToFile(String data, OutputStream os) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(os);
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            return;
        }
    }
}
