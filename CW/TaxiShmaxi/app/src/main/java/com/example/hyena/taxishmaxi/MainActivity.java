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
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;

import static android.graphics.Color.RED;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    JSONObject data;
    String authentication_token, json_data;
    private MapView mapView;
    PointD start_point = new PointD();
    PointD end_point   = new PointD();
    boolean start_f    = true;
    boolean end_f      = false;
    Marker start_m;
    Marker end_m;
    TextView start_e, end_e;
    Button start, end, confirm;
    File token;
    HttpRequests httpRequests;
    RequestQueue queue;
    String url         = "http://192.168.0.106:3000/api/rides";
    String signout_url = "http://192.168.0.106:3000/api/users/sign_out";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        queue                = Volley.newRequestQueue(this);
        authentication_token = getIntent().getStringExtra("authentication_token");

        HashMapToJson hashMapToJson = new HashMapToJson(new HashMap<String, String>()
        {{
            put("token", authentication_token);
        }});
        try {
            json_data = hashMapToJson.to_json().toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        token = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/data_taxi.json");
        OutputStream os = null;
        try {
            os = new FileOutputStream(token);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        writeToFile(json_data, os);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        confirm = findViewById(R.id.confirm);
        start   = findViewById(R.id.start_p);
        end     = findViewById(R.id.end_p);
        start_e = findViewById(R.id.no_start);
        end_e   = findViewById(R.id.no_end);

        start_e.setVisibility(View.GONE);
        end_e.setVisibility(View.GONE);



        final Intent select_ride = new Intent(this, SelectRide.class);
        confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                if (!raise_errors()){
                    url = url +
                            "?start_point_lng="+Double.toString(start_point.x) +
                            "&start_point_lat="+Double.toString(start_point.y);

                    httpRequests = new HttpRequests(url, Request.Method.GET);
                    queue.add(httpRequests.createRequest(authentication_token));
                    new Thread(new Runnable() {
                        public void run() {
                            while (true) {
                                if (httpRequests.response_object != null) {
                                    select_ride.putExtra("disponible_rides", httpRequests.response_object.toString());
                                    select_ride.putExtra("authentication_token", authentication_token);
                                    select_ride.putExtra("start_point_x", start_point.x);
                                    select_ride.putExtra("start_point_y", start_point.y);
                                    select_ride.putExtra("end_point_x", end_point.x);
                                    select_ride.putExtra("end_point_y", end_point.y);
                                    startActivity(select_ride);
                                    break;
                                }
                            }
                        }
                    }).start();
                }

            }
        });

        start.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                start_f = true;
                end_f   = false;
            }
        });

        end.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                start_f = false;
                end_f   = true;
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

    @Override
    protected void onResume()
    {
        super.onResume();
        mapView.onResume();
    }
    @Override
    public void onMapReady(final GoogleMap map)
    {
        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(47.0251124,28.911690500000036));
        CameraUpdate zoom   = CameraUpdateFactory.zoomTo(11);
        map.moveCamera(center);
        map.animateCamera(zoom);

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {

                if (start_f){
                    if (start_m != null){
                        start_m.remove();
                    }
                    start_m = map.addMarker(new MarkerOptions()
                            .position(point)
                            .title("Start")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    start_point.x = point.latitude;
                    start_point.y = point.longitude;
                }
                else{
                    if (end_m != null){
                        end_m.remove();
                    }
                    end_m = map.addMarker(new MarkerOptions().position(point)
                            .position(point)
                            .title("End")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                    end_point.x = point.latitude;
                    end_point.y = point.longitude;
                }
            }
        });
    }


    @Override
    public final void onDestroy()
    {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public final void onLowMemory()
    {
        mapView.onLowMemory();
        super.onLowMemory();
    }

    @Override
    public final void onPause()
    {
        mapView.onPause();
        super.onPause();
    }

    public boolean raise_errors(){
        Boolean error = false;
        start_e.setVisibility(View.GONE);
        end_e.setVisibility(View.GONE);

        if (start_point.x == 0 && start_point.y == 0){
            start_e.setTextColor(RED);
            start_e.setVisibility(View.VISIBLE);
            error = true;
        }
        else if(end_point.x == 0 && end_point.y == 0){
            end_e.setTextColor(RED);
            end_e.setVisibility(View.VISIBLE);
            error = true;
        }

        return error;
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
