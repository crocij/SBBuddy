package com.example.peterpenis.sbbuddy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String LOGIN_URL = "https://www.robinbisping.com/hackathon/users.php?action=login";

    public static final String KEY_EMAIL="email";
    public static final String KEY_PASSWORD="password";
    public static final String KEY_NAME="user";

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;

    private String email;
    private String id;
    private String firstname;
    private String karma;
    private String status;
    private String last_login;
    private String registered;
    private String lastname;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(this);
    }


    private void userLogin() {
        email = editTextUsername.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();




        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!response.trim().equals("0")){

                            try {
                                JSONObject a = new JSONObject(response);
                                email = a.getString("email");
                                password = a.getString("password");
                                firstname = a.getString("firstname");
                                lastname = a.getString("lastname");
                                karma = a.getString("karma");
                                status = a.getString("status");
                                last_login = a.getString("last_login");
                                registered = a.getString("registered");
                                id = a.getString("id");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            SharedPreferences userData = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                            SharedPreferences.Editor dataEditor = userData.edit();

                            dataEditor.putString("email", email);
                            dataEditor.putString("password", password);
                            dataEditor.putString("firstname", firstname);
                            dataEditor.putString("lastname", lastname);
                            dataEditor.putString("id", id);
                            dataEditor.putString("karma", karma);
                            dataEditor.putString("status", status);
                            dataEditor.putString("last_login", last_login);
                            dataEditor.putString("registered", registered);
                            dataEditor.apply();

                            openProfile();
                        }else{
                            Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(KEY_EMAIL,email);
                map.put(KEY_PASSWORD,password);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void openProfile(){
        Log.i("hüssli", id);
        Intent intent = new Intent(this, ActivityUserProfile.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v){
        userLogin();
    }
}
