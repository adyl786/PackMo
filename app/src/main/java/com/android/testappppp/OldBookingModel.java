package com.android.testappppp;

public class OldBookingModel
{
    private String Name,Address,Capacity,Date;

    public OldBookingModel() {
    }

    public OldBookingModel(String name, String address, String capacity, String date)
    {
        Name = name;
        Address = address;
        Capacity = capacity;
        Date = date;
    }

    public String getName() {
        return Name;
    }

    public String getAddress() {
        return Address;
    }

    public String getCapacity() {
        return Capacity;
    }

    public String getDate() {
        return Date;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setCapacity(String capacity) {
        Capacity = capacity;
    }

    public void setDate(String date) {
        Date = date;
    }
}
