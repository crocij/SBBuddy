package com.example.peterpenis.sbbuddy;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by PeterPenis on 12.03.2016.
 */
public class GetBuddyData extends Thread {



    String url = "https://www.robinbisping.com/hackathon/users.php?action=user_from_id";
    public static String KEY_wert1 = "user_id";



    public void send(final Searching_Buddy s, final String buddy_id, final callbackGet callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(s, String.valueOf(error), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_wert1, buddy_id);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(s);
        requestQueue.add(stringRequest);

    }

    public interface callbackGet{

        void onSuccess(String s);

    }
}
