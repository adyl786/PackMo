package com.android.testappppp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;


import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.testappppp.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;


public class HomeFragment extends Fragment {
    Button bookedButton;
    int dayFrom, monthFrom, yearFrom;

    int hourFrom, minuteFrom;
    TextView fakeText;
    String capacity[];
    Spinner spinner;
    String cap;
    TextView availabilityStatus;
    int h1, m1, d1, mo1, y1;
    public final String URL="https://puisne-stretcher.000webhostapp.com/admire/admire/API/API/booking.php";
    View view2;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        view2=view;
        availabilityStatus=view.findViewById(R.id.statusID);
        bookedButton = view.findViewById(R.id.bt);
//        SPINNER
        spinner = view.findViewById(R.id.capacityID);
        capacity = new String[]{"<--Select Capacity-->", "2500KG", "5000KG", "7500KG", "10000KG"};
        final ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, capacity);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
//FAKE TEXT
        fakeText = view.findViewById(R.id.fakeTextID);


        final EditText dateEditText = view.findViewById(R.id.date);
        final EditText timeEditText = view.findViewById(R.id.time);
//     DATE
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int date = calendar.get(Calendar.DATE), month = calendar.get(Calendar.MONTH), year = calendar.get(Calendar.YEAR);
                d1 = date;
                mo1 = month + 1;
                y1 = year;
                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month++;
                        dateEditText.setText(dayOfMonth + "/" + month + "/" + year);
                        dayFrom = dayOfMonth;
                        monthFrom = month;
                        yearFrom = year;

                    }
                }, year, month, date);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + 2*24*60*60*1000);
                datePickerDialog.show();
                dateEditText.setError(null);
            }
        });
//        TIME

        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                final int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                h1 = hour;
                m1 = minute;

                TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeEditText.setText(hourOfDay + ":" + minute);
                        hourFrom = hourOfDay;
                        minuteFrom = minute;
                    }
                }, hour, minute, true);
                timePickerDialog.show();
                timeEditText.setError(null);
            }
        });

//SPINNER
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    cap = capacity[position];
                    bookedButton.setEnabled(false);
                    sendCapacity();
                }
                else cap=null;
                { fakeText.setError(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//ON CLICK
        bookedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dateEditText.getText().toString().isEmpty()) {
                    dateEditText.setError("Select Date");
                } else if (timeEditText.getText().toString().isEmpty()) {
                    timeEditText.setError("Select Time");
                } else if (cap == null) {

                    fakeText.setError("Select Capacity");
                } else if (!checkTime()) {
                    Toast.makeText(getContext(), "Select time must be after current time", Toast.LENGTH_LONG).show();

                } else {


                    Intent intent = new Intent(getActivity(), Booking_details.class);
                    intent.putExtra("dayOfMonth", dayFrom);
                    intent.putExtra("month", monthFrom);
                    intent.putExtra("year", yearFrom);
                    intent.putExtra("hour", hourFrom);
                    intent.putExtra("minute", minuteFrom);

                    intent.putExtra("capacity", cap);
                    startActivity(intent);
                    getActivity().finish();
                }

            }
        });


        return view;
    }

    private void sendCapacity() {
        boolean B;
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response",response.toString());
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String status=jsonObject.getString("status");
                    if(status.equals("sucess"))
                    {
                        availabilityStatus.setText("Available");
                        bookedButton.setEnabled(true);
                    }
                    else
                    {
                        availabilityStatus.setText("Not Available");
                        bookedButton.setEnabled(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Connection Problem",Toast.LENGTH_SHORT).show();
                bookedButton.setEnabled(false);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> Param=new HashMap<String, String>();
                Param.put("capacity",cap);
                return Param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(view2.getContext());
        requestQueue.add(stringRequest);
    }

    public boolean checkTime() {
        if (d1 != dayFrom || mo1 != monthFrom || y1 != yearFrom) {
            return true;
        } else {
            int CurrentTime = h1 * 60 + m1;
            int BookingTime = hourFrom * 60 + minuteFrom;
            if (BookingTime - CurrentTime > 90)
                return true;
            else
                return false;
        }
    }
}