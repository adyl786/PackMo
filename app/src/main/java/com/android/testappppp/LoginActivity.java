package com.android.testappppp;


import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity {
    Button loginButton;
    TextView createAccount_textView;
    EditText email, password;
    String email_string, password_string;
    ProgressDialog progressDoalog;

    public static final String Login_URL = "https://puisne-stretcher.000webhostapp.com/admire/admire/API/API/user_login.php";
    //SHARED PREFERENCE
    SharedPreferences sharedPreferences;
    final String NAME = "Name", PHONE = "Phone", PASSWORD = "Password", EMAIL = "Email", GENDER = "Gender",ID="id";
    final String PREFERENCE = "My Preference",LOGIN_STATUS="LoginStatus";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar=findViewById(R.id.login_toolbar);
        toolbar.setTitle("Login");

        progressDoalog = new ProgressDialog(LoginActivity.this);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        sharedPreferences = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
//PROGRESS BAR
//        progressBar=findViewById(R.id.progress_bar);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (email.getText().toString().isEmpty()) {

                    Toast.makeText(getApplicationContext(), "Enter email address", Toast.LENGTH_LONG).show();
                    email.requestFocus();
                    email.setError("Enter Email");

                } else if (password.getText().toString().isEmpty()) {
//                    Toast.makeText(getApplicationContext(), "Enter password", Toast.LENGTH_SHORT).show();
                    password.requestFocus();
                    password.setError("Enter Password");
                } else {
//                    PROGRESS
//                    progressBar.setVisibility(View.VISIBLE);
//                    progressDoalog.setMessage("Its loading....");
                    progressDoalog.setTitle("Loading....");
                    progressDoalog.setCanceledOnTouchOutside(false);
//                    progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDoalog.show();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Login_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Response",response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                if (status.equals("failed")) {
                                    Toast.makeText(getApplicationContext(), "EmailId or password is InValid", Toast.LENGTH_SHORT).show();
//                                    progressBar.setVisibility(View.INVISIBLE);
                                    progressDoalog.dismiss();
                                }
                                else {
                                    String Name, Phone, Gender,Email,Id;
                                    Name = jsonObject.getString("name");
                                    Phone = jsonObject.getString("phone");
                                    Gender = jsonObject.getString("gender");
                                    Email=jsonObject.getString("email");
                                    Id=jsonObject.getString("id");
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(NAME, Name);
                                    editor.putString(PHONE, Phone);
                                    editor.putString(EMAIL,Email);
                                    editor.putString(PASSWORD,password.getText().toString() );
                                    editor.putString(GENDER, Gender);
                                    editor.putString(ID,Id);
                                    editor.putString(LOGIN_STATUS,"LogIn");
                                    editor.commit();
                                    progressDoalog.dismiss();
//                                    progressBar.setVisibility(View.INVISIBLE);
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            } catch (JSONException e) {
                                Log.d("Error",e.getMessage());
                            }

                        }
                    },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("Error",error.getMessage());
                                    progressDoalog.dismiss();
                                    Toast.makeText(getApplicationContext(),"Can't Connected",Toast.LENGTH_SHORT).show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> param = new HashMap<String, String>();
                            param.put("u_email", email.getText().toString());
                            param.put("u_pass", password.getText().toString());
                            return param;
                        }
                    };
                    RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);
                }
            }
        });

        createAccount_textView = findViewById(R.id.createAccountText);
        createAccount_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CreateAccount.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                finish();
            }
        });

    }
}
