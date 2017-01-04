package com.example.school.silverproductivity;

import android.location.Geocoder;
import android.location.Criteria;
import android.location.LocationManager;
import android.location.Location;
import android.location.Address;
import android.content.Context;

import java.io.IOException;
import java.util.List;

public class CityLocation {
    private static Geocoder geocoder;
    private static String cityName;

    public static String getCNBylocation(Context context){

        geocoder = new Geocoder(context);
        LocationManager locationManager;
        String serviceName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager)context.getSystemService(serviceName);
        String provider = LocationManager.NETWORK_PROVIDER;
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(false);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        Location location = locationManager.getLastKnownLocation(provider);
        String queryed_name = updateWithNewLocation(location);
        if((queryed_name != null) && (0 != queryed_name.length())){
            cityName = queryed_name;
        }
        return cityName;
    }

    private static String updateWithNewLocation(Location location) {
        String mCityName = "";
        double lat = 0;
        double lng = 0;
        List<Address> addList = null;
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
        } else {

            System.out.println("No location Info");
        }

        try {
            addList = geocoder.getFromLocation(lat, lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addList != null && addList.size() > 0) {
            for (int i = 0; i < addList.size(); i++) {
                Address add = addList.get(i);
                mCityName += add.getLocality();
            }
        }
        return mCityName;
    }
}
