package com.example.hyena.taxishmaxi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.util.HashMap;

import static android.graphics.Color.RED;

public class SignUp extends AppCompatActivity {

    public TextView email_field, password_field, password_r_field, error_msg, invalid_pass, invalid_r_password, login;
    public Button signup;
    public String email_regexp = "(.*\\@.*\\.\\w{2,3}\\z)";
    String url = "http://192.168.0.106:3000/api/users/sign_up";
    RequestQueue queue;

    HttpRequests httpRequests;
    String authentication_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        queue = Volley.newRequestQueue(this);

        email_field        = findViewById(R.id.email);
        password_field     = findViewById(R.id.password);
        password_r_field   = findViewById(R.id.r_password);
        error_msg          = findViewById(R.id.Error);
        invalid_pass       = findViewById(R.id.error_password);
        invalid_r_password = findViewById(R.id.error_r_password);
        login              = findViewById(R.id.login);
        signup             = findViewById(R.id.signup);


        error_msg.setTextColor(RED);
        invalid_pass.setTextColor(RED);
        invalid_r_password.setTextColor(RED);
        error_msg.setVisibility(View.GONE);
        invalid_pass.setVisibility(View.GONE);
        invalid_r_password.setVisibility(View.GONE);

        final Intent main_intent = new Intent(this, MainActivity.class);
        signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                if (!raise_errors()){
                    HashMapToJson hashmap = new HashMapToJson(new HashMap<String, String>()
                    {{
                        put("email", email_field.getText().toString());
                        put("password", password_field.getText().toString());
                        put("password_confirmation", password_r_field.getText().toString());
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

        final Intent signup_intent = new Intent(this, StartPage.class);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                startActivity(signup_intent);
                finish();
            }
        });
    }

    public boolean raise_errors(){
        String password = password_field.getText().toString();
        boolean status  = false;
        error_msg.setVisibility(View.GONE);
        invalid_pass.setVisibility(View.GONE);
        invalid_r_password.setVisibility((View.GONE));

        if (password.isEmpty() || password.length() < 6){
            invalid_pass.setText("Password must contain at least 6 characters");
            invalid_pass.setVisibility(View.VISIBLE);
            status = true;
        }
        if (!email_field.getText().toString().matches(email_regexp)){
            error_msg.setText("Wrong Email");
            error_msg.setVisibility(View.VISIBLE);
            status = true;
        }
        if (!password_r_field.getText().toString().equals(password)){
            invalid_r_password.setText("Passwords must match");
            invalid_r_password.setVisibility(View.VISIBLE);
            status = true;
        }

        return status;
    }
}
