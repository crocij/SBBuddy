package com.example.peterpenis.sbbuddy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String REGISTER_URL = "https://www.robinbisping.com/hackathon/users.php?action=register";

    public static final String KEY_PRENAME = "firstname";
    public static final String KEY_NAME = "lastname";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";


    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextLastname;

    private Button buttonRegister;
    private Button buttonLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextLastname = (EditText) findViewById(R.id.editTextLastName);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextEmail= (EditText) findViewById(R.id.editTextEmail);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);

        buttonRegister.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);
    }

    private void registerUser(){
        final String prename = editTextUsername.getText().toString().trim();
        final String lastname = editTextLastname.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("1")){
                            Toast.makeText(MainActivity.this,"Registrierung efolgreich",Toast.LENGTH_LONG).show();

                            SharedPreferences userData = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                            SharedPreferences.Editor dataEditor = userData.edit();

                            dataEditor.putString("firstname", prename);
                            dataEditor.putString("email", email);
                            dataEditor.putString("password", password);
                            dataEditor.putString("lastname", lastname);
                            dataEditor.apply();
                            nextActivity();

                        }
                        else{
                            System.out.println(response);
                            Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_EMAIL, email);
                params.put(KEY_PASSWORD,password);
                params.put(KEY_PRENAME, prename);
                params.put(KEY_NAME, lastname);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void loginUser(){
        Intent login = new Intent(this, LoginActivity.class);
        startActivity(login);
    }

    private void nextActivity() {
        Intent intent = new Intent(this, ActivityUserProfile.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if(v == buttonRegister){
            registerUser();
        }
        if(v == buttonLogin){
           loginUser();
        }
    }
}
