package com.android.testappppp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;


import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class Booking_details extends AppCompatActivity {
    Button cnfBook;
    TextView dateT, timeT, capacityT;
    EditText name, phone, address, landmark;
    String dateString, timeString, capacityString;
    int dd, mm, yy;
    int hr, min;
    String Name, Phone, Id;
    //Driver Detail
    String Driver_name = "Some Name", Driver_contact = "any contact", Truck_no = "some no", Truck_id = "Some Id";
    //Shared Prefrense
    final String NAME = "Name", PHONE = "Phone", PASSWORD = "Password", EMAIL = "Email", GENDER = "Gender", ID = "id";
    final String PREFERENCE = "My Preference", LOGIN_STATUS = "LoginStatus";
    SharedPreferences sharedPreferences;
    //    ALERT DIAGLOGBOX
    AlertDialog.Builder builder;
    //        URL
    public static final String Booking_in_truck_URL = "https://puisne-stretcher.000webhostapp.com/admire/admire/API/API/booking_in_truck.php";
    public static final String Insert_in_booking = "https://puisne-stretcher.000webhostapp.com/admire/admire/API/API/InsertInBookingTable.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        Toolbar toolbar=findViewById(R.id.booking_details_toolbar);
        toolbar.setTitle("Booking Details");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Booking_details.this, HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

        dateT = findViewById(R.id.dateID);
        timeT = findViewById(R.id.timeID);
        capacityT = findViewById(R.id.capacityID);
        cnfBook = findViewById(R.id.confirmBookBTN);
        name = findViewById(R.id.nameID);
        phone = findViewById(R.id.phoneNoID);
        builder = new AlertDialog.Builder(this);
        //take name and phone from SharedPreference
        sharedPreferences = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        Name = sharedPreferences.getString(NAME, "default");
        Phone = sharedPreferences.getString(PHONE, "default");
        Id = sharedPreferences.getString(ID, "default");
        //Set Them
        name.setText(Name);
        phone.setText(Phone);
//        BOOKED
        name = findViewById(R.id.nameID);
        phone = findViewById(R.id.phoneNoID);
        address = findViewById(R.id.addressID);
        landmark = findViewById(R.id.landmarkID);

        //        GET INTENT
        Intent intent = getIntent();

        //        DATE
        dd = intent.getIntExtra("dayOfMonth", 0);
        mm = intent.getIntExtra("month", 0);
        yy = intent.getIntExtra("year", 0);

        if (dd < 10 && mm < 10)
            dateString = yy + "-0" + mm + "-0" + dd;
        else if (dd < 10 && mm >= 10)
            dateString = yy + "-" + mm + "-0" + dd;
        else if (dd >= 10 && mm < 10)
            dateString = yy + "-0" + mm + "-" + dd;
        else
            dateString = yy + "-" + mm + "-" + dd;
        //     TIME
        hr = intent.getIntExtra("hour", 0);
        min = intent.getIntExtra("minute", 0);
        if (hr < 10 && min < 10)
            timeString = 0 + "" + hr + ":0" + min;
        else if (hr < 10 && min >= 10)
            timeString = 0 + "" + hr + ":" + min;
        else if (hr >= 10 && min < 10)
            timeString = hr + ":0" + min;
        else
            timeString = hr + ":" + min;

        //       CAPACITY
        capacityString = intent.getStringExtra("capacity");

        dateT.setText(dateString);
        timeT.setText(timeString);
        capacityT.setText(capacityString);

//BOOKING
        cnfBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().isEmpty() || phone.getText().toString().isEmpty() || address.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(), "Enter details", Toast.LENGTH_LONG).show();
                else {
                    builder.setMessage("Do you want to confirm ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    toBooking();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
                                    Toast.makeText(getApplicationContext(), "you choose no action for alertbox",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("Alert!");
                    alert.show();
//                    toBooking();

                }

            }
        });
    }

    public void toBooking() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Booking_in_truck_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("Success")) {
                        Driver_name = jsonObject.getString("driver_name");
                        Driver_contact = jsonObject.getString("driver_contact");
                        Truck_id = jsonObject.getString("truck_id");
                        Truck_no = jsonObject.getString("truck_no");
                        Log.d("Information", Driver_name + " " + Driver_contact + " " + Truck_id + " " + Truck_no);
                        dataSendToOrder();

                    } else {
                        Toast.makeText(getApplication(), "Can't book", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(), "Can't connected", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> Params = new HashMap<String, String>();
                Params.put("user_id", Id);
                Params.put("capacity", capacityString);
                 Params.put("booking_date","2019-12-30");
                 Params.put("booking_time","12:00:00");
                Params.put("departure_date", dateString);
                Params.put("departure_time", timeString);
                Params.put("user_address", address.getText().toString());
                return Params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    public void dataSendToOrder()
    {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Insert_in_booking, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response",response);
                try {
                    JSONObject jsonObject =new JSONObject(response);
                    String status=jsonObject.getString("status");
                    if(status.equals("sucess"))
                    {
                        sendData();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Failure",Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Log.d("Error",e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> Params=new HashMap<String, String>();
                Params.put("user_id",Id);
                Params.put("truck_id",Truck_id);
                Params.put("departure_date",dateString);
                Params.put("departure_time",timeString);
                Params.put("booking_date","2019-12-30");
                Params.put("booking_time","12:00:00");
                Params.put("capacity",capacityString);
                Params.put("name",name.getText().toString());
                Params.put("phone_no",phone.getText().toString());
                Params.put("address",address.getText().toString());
                return Params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    //BACK PRESS
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Booking_details.this, HomeActivity.class);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        startActivity(intent);
        finish();
    }

    //SEND DATA WITH CONFIRMED BOOKING
    public void sendData()
    {
        Intent intent = new Intent(Booking_details.this, Booked.class);
        intent.putExtra("date", dateString);
        intent.putExtra("time", timeString);
        intent.putExtra("capacity", capacityString);

        intent.putExtra("driverName", Driver_name);
        intent.putExtra("driverContact", Driver_contact);
        intent.putExtra("truckId", Truck_id);
        intent.putExtra("truckNumber", Truck_no);


        intent.putExtra("name", name.getText().toString());
        intent.putExtra("phone", phone.getText().toString());
        intent.putExtra("address", address.getText().toString());
        intent.putExtra("landmark", landmark.getText().toString());
        startActivity(intent);
        finish();
    }
}
