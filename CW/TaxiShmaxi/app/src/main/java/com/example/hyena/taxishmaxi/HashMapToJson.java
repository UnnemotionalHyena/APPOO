package com.example.hyena.taxishmaxi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class HashMapToJson {
    Map<String, String> hashmap;

    public HashMapToJson(Map<String, String> hashmap){
        this.hashmap = hashmap;
    }

    public JSONObject to_json() throws JSONException {
        String json_string = "{";
        for (Map.Entry<String, String> entry : hashmap.entrySet()) {
            json_string += entry.getKey() + ":";
            json_string += entry.getValue() + ",";
        }

        return new JSONObject(json_string.substring(0, json_string.length() - 1) + "}");
    }
}
