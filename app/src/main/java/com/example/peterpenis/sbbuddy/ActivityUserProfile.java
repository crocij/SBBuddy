package com.example.peterpenis.sbbuddy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ActivityUserProfile extends AppCompatActivity implements View.OnClickListener {
    private TextView textView;
    Button beBuddyBtn;
    Button buddyBtn;

    public String firstname;
    public String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        buddyBtn = (Button)findViewById(R.id.buddyBtn);
        beBuddyBtn = (Button)findViewById(R.id.beBuddyBtn);
        buddyBtn.setOnClickListener(this);
       beBuddyBtn.setOnClickListener(this);

        textView = (TextView) findViewById(R.id.textViewUsername);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        firstname = preferences.getString("firstname", "");
        email =  preferences.getString("email", "");

        textView.setText("Sali " + firstname);
    }

    public void beBuddy(){

    }

    public void getBuddy(){
        Intent intent = new Intent(ActivityUserProfile.this, Searching_Buddy.class);
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        if (v == buddyBtn) {
            getBuddy();
        }

        else if (v == beBuddyBtn){
            beBuddy();
        }
    }
}
