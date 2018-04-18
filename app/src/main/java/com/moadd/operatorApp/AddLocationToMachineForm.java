package com.moadd.operatorApp;

/**
 * Created by moadd on 25-Feb-18.
 */

public class AddLocationToMachineForm {

    private String locationId;
    private String machineId;
    private String status;
    private String country;
    private String city;
    private String address;
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getLocationId() {
        return locationId;
    }
    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }
    public String getMachineId() {
        return machineId;
    }
    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }




}