package com.example.school.silverproductivity;


import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class Attraction implements ClusterItem {
    public final String name;
    public final int type;
    private final LatLng mPosition;

    public Attraction(LatLng position, String name, int type) {
        this.name = name;
        this.type = type;
        mPosition = position;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

}
