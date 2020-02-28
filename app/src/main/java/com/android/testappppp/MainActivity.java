package com.android.testappppp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static int STO=3000;
    final String NAME = "Name", PHONE = "Phone", PASSWORD = "Password", EMAIL = "Email", GENDER = "Gender";
    final String PREFERENCE = "My Preference", LOGIN_STATUS = "LoginStatus";
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences=getSharedPreferences(PREFERENCE,MODE_PRIVATE);
        String Login_status=sharedPreferences.getString(LOGIN_STATUS,"LogOut");
        if(Login_status.equals("LogOut"))
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {


                    Intent intent= new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();


                }
            },STO);
        }
        else
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {


                    Intent intent= new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();


                }
            },STO);
        }

    }
}
