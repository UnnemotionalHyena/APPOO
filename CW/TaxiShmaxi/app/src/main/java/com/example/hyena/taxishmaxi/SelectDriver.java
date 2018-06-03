package com.example.hyena.taxishmaxi;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SelectDriver extends AppCompatActivity {

    HttpRequests httpRequests;
    String signout_url = "http://192.168.0.106:3000/api/users/sign_out";
    File token;
    RequestQueue queue;
    String authentication_token, disponible_drivers, time, driver_id;
    PointD start_p = new PointD(), end_p = new PointD();
    JSONArray drivers;
    ListView list_view;
    Intent finish_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_driver);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        authentication_token = getIntent().getStringExtra("authentication_token");
        disponible_drivers   = getIntent().getStringExtra("disponible_drivers");
        time                 = getIntent().getStringExtra("time");
        start_p.x            = getIntent().getDoubleExtra("start_point_x", 0.0);
        start_p.y            = getIntent().getDoubleExtra("start_point_y", 0.0);
        end_p.x              = getIntent().getDoubleExtra("end_point_x", 0.0);
        end_p.y              = getIntent().getDoubleExtra("end_point_y", 0.0);

        token = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/data_taxi.json");
        queue = Volley.newRequestQueue(this);
        finish_intent = new Intent(this, FInish.class);
        try {
            create_rides_list();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void create_rides_list() throws JSONException, ParseException {
        JSONObject d_rides = new JSONObject(disponible_drivers);
        drivers            = d_rides.getJSONArray("drivers");
        list_view          = findViewById(R.id.listview);

        final HashMap<String, String> hash_data = new HashMap<>();
        for (int i = 0; i < drivers.length(); i++) {
            JSONObject obj = drivers.getJSONObject(i);
            hash_data.put(obj.getString("name"), "Rating: " + obj.getString("rating"));
        }

        List<HashMap<String, String>> listItems = new ArrayList<>();
        SimpleAdapter adapter = new SimpleAdapter(this, listItems, R.layout.list_item,
                new String[]{"First Line", "Second Line"},
                new int[]{R.id.text1, R.id.text2});

        Iterator it = hash_data.entrySet().iterator();
        while(it.hasNext()){
            HashMap<String, String> resultMap = new HashMap<>();
            Map.Entry pair = (Map.Entry)it.next();

            resultMap.put("First Line", pair.getKey().toString());
            resultMap.put("Second Line", pair.getValue().toString());
            listItems.add(resultMap);
        }
        list_view.setAdapter(adapter);

        list_view.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap item = (HashMap) list_view.getItemAtPosition(position);

                for (int i = 0; i < drivers.length(); i++) {
                    JSONObject obj = null;
                    try {
                        obj = drivers.getJSONObject(i);
                        if ((obj.getString("name")).equals(item.get("First Line")) &&
                                ("Rating: " + obj.getString("rating")).equals(item.get("Second Line"))){
                            driver_id = obj.getString("id");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                String url = "http://192.168.0.106:3000/api/rides";

                HashMapToJson hashmap = new HashMapToJson(new HashMap<String, String>()
                {{
                    put("driver_id", driver_id);
                    put("start_point_lng", Double.toString(start_p.x));
                    put("start_point_lat", Double.toString(start_p.y));
                    put("end_point_lng", Double.toString(end_p.x));
                    put("end_point_lat", Double.toString(end_p.y));
                    put("time", "\""+ time + "\"");
                }});

                try {
                    httpRequests = new HttpRequests(url, Request.Method.POST, hashmap.to_json());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                queue.add(httpRequests.createRequest(authentication_token));
                startActivity(finish_intent);
                finish();
            }
        });
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
