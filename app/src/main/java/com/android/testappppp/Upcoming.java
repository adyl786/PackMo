package com.android.testappppp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Upcoming extends Fragment {
    final String NAME = "Name", PHONE = "Phone", PASSWORD = "Password", EMAIL = "Email", GENDER = "Gender",ID="id";
    final String PREFERENCE = "My Preference",LOGIN_STATUS="LoginStatus";
    SharedPreferences sharedPreferences;
    String user_id;

    RecyclerView recyclerView;
    ArrayList<OldBookingModel> arrayList;
    MyListAdapter myListAdapter;
    RequestQueue Rq;
    Context context;
    public static final String UPCOMING_URL="https://puisne-stretcher.000webhostapp.com/admire/admire/API/API/upcoming_datail.php";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_upcoming, container, false);
        arrayList=new ArrayList<OldBookingModel>();
        recyclerView=view.findViewById(R.id.Rv_upcoming);
        sharedPreferences=view.getContext().getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        user_id=sharedPreferences.getString(ID,"default");
        context=view.getContext();
        Rq= Volley.newRequestQueue(view.getContext());
        loadAdapter();
        myListAdapter=new MyListAdapter(arrayList,view.getContext());
        recyclerView.setAdapter(myListAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        return view;
    }
    public void loadAdapter()
    {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, UPCOMING_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response",response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("my_order");
                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject J=jsonArray.getJSONObject(i);
                        String name, capacity, address, date;
                        name = J.getString("name");
                        capacity = J.getString("capacity");
                        address = J.getString("address");
                        date = J.getString("departure_date");
                        OldBookingModel model = new OldBookingModel(name, address, capacity, date);
                        arrayList.add(model);
                    }
                    myListAdapter=new MyListAdapter(arrayList,context);
                    recyclerView.setAdapter(myListAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> Params=new HashMap<String,String>();
                Params.put("user_id",user_id);
                return Params;
            }
        };
        Rq.add(stringRequest);
    }
}
