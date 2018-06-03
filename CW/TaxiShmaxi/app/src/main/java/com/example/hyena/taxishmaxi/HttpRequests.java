package com.example.hyena.taxishmaxi;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HttpRequests {

    String url;
    JSONObject data;
    public JSONObject response_object;
    String error;
    int method;

    public HttpRequests(String url, int method, JSONObject data){
        this.url    = url;
        this.method = method;
        this.data   = data;
    }

    public HttpRequests(String url, int method){
        this.url    = url;
        this.method = method;
    }

    public JsonObjectRequest createRequest(){
        return new JsonObjectRequest(method, url, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject json_response) {
                        response_object = json_response;
                        Log.e("Response is -----------: ", response_object + "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError v_error) {
                        error = v_error.toString();
                        Log.e("ERROR:", error + "That didn't work!");
                        try {
                            response_object = new JSONObject("{'error':'error'}");
                        } catch (JSONException e) {
                            Log.e("error_parse", e + " ");
                            e.printStackTrace();
                        }
                    }
                }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };
    }

    public JsonObjectRequest createRequest(final String authorization){
        return new JsonObjectRequest(method, url, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject json_response) {
                        response_object = json_response;
                        Log.e("Response is -----------: ", response_object + "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError v_error) {
                        try {
                            response_object = new JSONObject("{'error':'error'}");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        error = v_error.toString();
                        Log.e("ERROR:", error + "That didn't work!");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + authorization);
                return params;
            }
        };
    }
}
