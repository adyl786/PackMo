package com.android.testappppp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class Booked extends AppCompatActivity {
    TextView date, time, capacity;
    TextView name, address, phone, landmark;
    String dateString, timeString, capacityString;
    String nameString, addressString, phoneString, landmarkString;
    TextView driverName,driverContact,truckNumber;
    String D_name,D_contact,T_number,T_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked);
        Toolbar toolbar=findViewById(R.id.booked_toolbar);
        toolbar.setTitle("Booked");

        date = findViewById(R.id.dateID);
        time = findViewById(R.id.timeID);
        capacity = findViewById(R.id.capacityID);
        name = findViewById(R.id.name);

        driverName=findViewById(R.id.driverNameID);
        driverContact=findViewById(R.id.driverContact);
        truckNumber=findViewById(R.id.TruckNumber);

        address = findViewById(R.id.addressID);
        phone = findViewById(R.id.phoneNo);
        landmark = findViewById(R.id.landmarkID);
        Intent intent = getIntent();
//    GET INTENT
        dateString = intent.getStringExtra("date");
        timeString = intent.getStringExtra("time");
        capacityString = intent.getStringExtra("capacity");

        nameString = intent.getStringExtra("name");
        phoneString = intent.getStringExtra("phone");
        addressString = intent.getStringExtra("address");
        landmarkString = intent.getStringExtra("landmark");
//TRUCK & DRIVER DETAILS
        D_name=intent.getStringExtra("driverName");
        D_contact=intent.getStringExtra("driverContact");
        T_number=intent.getStringExtra("truckNumber");
        T_id=intent.getStringExtra("truckId");

//SET TEXT
        date.setText(dateString);
        time.setText(timeString);
        capacity.setText(capacityString);

        name.setText(nameString);
        //       LANDMARK CONDITION
        if (landmarkString.isEmpty()) {
            addressString=addressString.replaceAll("( )+", " ");
            addressString=addressString.replaceAll("(?m)^\\s*$[\n\r]{1,}", "");

            address.setText(addressString);
        } else {
            addressString = addressString + "," + landmarkString;
            address.setText(addressString);
        }

        phone.setText(phoneString);

        driverName.setText(driverName.getText()+D_name);
        driverContact.setText(driverContact.getText()+D_contact);
        truckNumber.setText(truckNumber.getText()+T_number);

    }

    //    BACK PRESS METHOD
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Booked.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

}
