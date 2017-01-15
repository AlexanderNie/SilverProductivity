package com.example.school.silverproductivity;


import android.graphics.drawable.BitmapDrawable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class Attraction implements ClusterItem {
    public final String name;
    public final BitmapDrawable profilePhoto;
    private final LatLng mPosition;

    public Attraction(LatLng position, String name, BitmapDrawable pictureResource) {
        this.name = name;
        profilePhoto = pictureResource;
        mPosition = position;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

}
