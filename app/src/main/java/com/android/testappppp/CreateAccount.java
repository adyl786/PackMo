package com.android.testappppp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreateAccount extends AppCompatActivity {

    Button createButton, customDialogButton;
    TextView backToLogin_textView, customDialogText;
    EditText name, email, phone, password, cnfpassword;
    final Context context = this;
    RadioGroup gender;
    RadioButton male, female, other;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String genderString;
    public static final String Register_URL = "https://puisne-stretcher.000webhostapp.com/admire/admire/API/API/user_register.php\n";
    String dateString;
    //String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\\\S+$).{4,}$";
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create);

        Toolbar toolbar=findViewById(R.id.create_account_toolbar);
        toolbar.setTitle("Create Account");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccount.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        cnfpassword = findViewById(R.id.confirm_password);
        phone = findViewById(R.id.phone);
        //RADIO
        gender = findViewById(R.id.gender);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        other = findViewById(R.id.other);
        createButton = findViewById(R.id.created);
        backToLogin_textView = findViewById(R.id.backToLogin);
        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genderString = "Male";
            }
        });
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genderString = "Female";
            }
        });
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genderString = "Other";
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().isEmpty()) {
                    name.setError("Enter name");
//                    Toast.makeText(getApplicationContext(), "Enter name", Toast.LENGTH_SHORT).show();

                } else if (email.getText().toString().isEmpty()) {
                    email.setError("Enter Email");
//                    Toast.makeText(getApplicationContext(), "Enter Email", Toast.LENGTH_SHORT).show();
                } else if (!(email.getText().toString().matches(emailPattern))) {
                    email.setError("Enter Valid Email");
//                    Toast.makeText(getApplicationContext(), "Enter Valid Email", Toast.LENGTH_SHORT).show();

                } else if (phone.getText().toString().isEmpty()) {
                    phone.setError("Enter Phone Number");
//                    Toast.makeText(getApplicationContext(), "Enter Phone Number", Toast.LENGTH_SHORT).show();

                } else if (phone.getText().toString().length() < 10) {
                    phone.setError("Enter valid phone number");
                } else if (password.getText().toString().isEmpty()) {
                    password.setError("Enter Password");
//                    Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_SHORT).show();

                } else if (cnfpassword.getText().toString().isEmpty()) {
                    cnfpassword.setError("Enter Confirmation Password");
//                    Toast.makeText(getApplicationContext(), "Enter Confirmation Password", Toast.LENGTH_SHORT).show();
                } else if (!(password.getText().toString().equals(cnfpassword.getText().toString()))) {
                    cnfpassword.requestFocus();
                    Toast.makeText(getApplicationContext(), "Password not matched", Toast.LENGTH_SHORT).show();
                } else if (gender.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Please select your gender", Toast.LENGTH_SHORT).show();
                } else {
                    Calendar calendar = Calendar.getInstance();
                    int dd = calendar.get(Calendar.DATE);
                    int mm = calendar.get(Calendar.MONTH);
                    mm += 1;
                    int yy = calendar.get(Calendar.YEAR);
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);
                    dateString = dd + "/" + mm + "/" + yy + "  " + hour + ":" + minute;
                    mm--;


                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Register_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("mytag", response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                if (status.equals("sucess")) {
                                    Toast.makeText(getApplicationContext(), "Insert Succesfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(CreateAccount.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else if (status.equals("user already created"))
                                    Toast.makeText(getApplicationContext(), "Already Exist", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(getApplicationContext(), "Something went wrong...\ntry again", Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("mhytag", error.toString());

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> param = new HashMap<String, String>();
                            param.put("u_name", name.getText().toString());
                            param.put("u_email", email.getText().toString());
                            param.put("u_pass", password.getText().toString());
                            param.put("u_phone", phone.getText().toString());
                            param.put("u_gender", genderString);
                            param.put("u_date", dateString);
                            return param;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);

                }
            }

        });
        backToLogin_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccount.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
//        BACK TO LOGIN
        backToLogin_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccount.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CreateAccount.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }
}