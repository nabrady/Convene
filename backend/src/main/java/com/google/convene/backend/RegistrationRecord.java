package com.google.convene.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * The Objectify object model for device registrations we are persisting
 */
@Entity
public class RegistrationRecord {

    @Id
    Long id;

    @Index
    private String regId;
    // you can add more fields...
    @Index
    private String fbId;
    @Index
    private double locationLatitude;
    @Index
    private double locationLongitude;

    public RegistrationRecord() {
        fbId = null;
        locationLatitude = 32;
        locationLongitude = -121;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public void setLocationLatitude(double latitude){
        this.locationLatitude = latitude;
    }

    public double getLocationLatitude(){
        return locationLatitude;
    }

    public void setLocationLongitude(double longitude){
        this.locationLongitude = longitude;
    }

    public double getLocationLongitude(){return locationLongitude;}
}
