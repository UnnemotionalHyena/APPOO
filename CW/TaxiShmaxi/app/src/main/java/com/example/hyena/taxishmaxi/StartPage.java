package com.example.hyena.taxishmaxi;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import static android.graphics.Color.RED;

public class StartPage extends AppCompatActivity {

    public TextView email_field, password_field, error_msg, invalid_pass, signUp;
    public Button logIn;
    public String email_regexp = "(.*\\@.*\\.\\w{2,3}\\z)";
    HttpRequests httpRequests;
    String url = "http://192.168.0.106:3000/api/users/sign_in";
    RequestQueue queue;
    String authentication_token;
    HashMapToJson hashmap;
    String json_data;
    JSONObject file_data = new JSONObject();
    File token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        email_field    = findViewById(R.id.email);
        password_field = findViewById(R.id.r_password);
        error_msg      = findViewById(R.id.Error);
        invalid_pass   = findViewById(R.id.error_password);
        logIn          = findViewById(R.id.login_btn);
        signUp         = findViewById(R.id.signup);

        queue          = Volley.newRequestQueue(this);

        error_msg.setTextColor(RED);
        invalid_pass.setTextColor(RED);
        error_msg.setVisibility(View.GONE);
        invalid_pass.setVisibility(View.GONE);

        token = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/data_taxi.json");
        try {
            extract_from_file(token);
            if (file_data.getString("token") != null){
                authentication_token = file_data.getString("token");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final Intent main_intent = new Intent(this, MainActivity.class);

        if (authentication_token != null){
            main_intent.putExtra("authentication_token", authentication_token);
            startActivity(main_intent);
        }

        logIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                if (!raise_errors()){
                    hashmap = new HashMapToJson(new HashMap<String, String>()
                        {{
                            put("email", email_field.getText().toString());
                            put("password", password_field.getText().toString());
                        }});

                    try {
                        httpRequests = new HttpRequests(url, Request.Method.POST, hashmap.to_json());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    queue.add(httpRequests.createRequest());

                    new Thread(new Runnable() {
                        public void run() {
                            while (true) {
                                if (httpRequests.response_object != null) {
                                    try {
                                        authentication_token = httpRequests.response_object.getString("authentication_token");
                                        Log.e("token", authentication_token + "");
                                        main_intent.putExtra("authentication_token", authentication_token);
                                        startActivity(main_intent);
                                        finish();
                                        break;
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }).start();
                }
            }
        });

        final Intent signup_intent = new Intent(this, SignUp.class);
        signUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                startActivity(signup_intent);
                finish();
            }
        });
    }

    public boolean raise_errors(){
        boolean status = false;
        error_msg.setVisibility(View.GONE);
        invalid_pass.setVisibility(View.GONE);

        if (password_field.getText().toString().isEmpty() ||
                password_field.getText().toString().length() < 6){
            invalid_pass.setText("Password must contain at least 6 characters");
            invalid_pass.setVisibility(View.VISIBLE);
            status = true;
        }
        if (!email_field.getText().toString().matches(email_regexp)){
            error_msg.setText("Wrong Email");
            error_msg.setVisibility(View.VISIBLE);
            status = true;
        }
        return status;
    }

    public void extract_from_file(File file_name) throws FileNotFoundException, JSONException {
        InputStream is = new FileInputStream(file_name);
        json_data = inputStreamToString(is);
        if (json_data != null) {
            file_data = new JSONObject(json_data);
        }
    }

    public String inputStreamToString(InputStream inputStream) {
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, bytes.length);
            return new String(bytes);
        } catch (IOException e) {
            return null;
        }
    }
}
