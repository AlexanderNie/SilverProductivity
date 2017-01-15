package com.example.school.silverproductivity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2017/1/15.
 */

public class AttractionMapActivity extends BaseDemoActivity implements ClusterManager.OnClusterClickListener<Attraction>, ClusterManager.OnClusterInfoWindowClickListener<Attraction>, ClusterManager.OnClusterItemClickListener<Attraction>, ClusterManager.OnClusterItemInfoWindowClickListener<Attraction>{

    private ClusterManager<Attraction> mClusterManager;
    private Random mRandom = new Random(1984);

    /**
     * Draws profile photos inside markers (using IconGenerator).
     * When there are multiple people in the cluster, draw multiple photos (using MultiDrawable).
     */
    private class AttractionRenderer extends DefaultClusterRenderer<Attraction> {
        private final IconGenerator mIconGenerator = new IconGenerator(getApplicationContext());
        private final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());
        private final ImageView mImageView;
        private final ImageView mClusterImageView;
        private final int mDimension;

        public AttractionRenderer() {
            super(getApplicationContext(), getMap(), mClusterManager);

            View multiProfile = getLayoutInflater().inflate(R.layout.multi_profile, null);
            mClusterIconGenerator.setContentView(multiProfile);
            mClusterImageView = (ImageView) multiProfile.findViewById(R.id.image);

            mImageView = new ImageView(getApplicationContext());
            mDimension = (int) getResources().getDimension(R.dimen.custom_profile_image);
            mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
            int padding = (int) getResources().getDimension(R.dimen.custom_profile_padding);
            mImageView.setPadding(padding, padding, padding, padding);
            mIconGenerator.setContentView(mImageView);
        }

        @Override
        protected void onBeforeClusterItemRendered(Attraction Attraction, MarkerOptions markerOptions) {
            // Draw a single Attraction.
            // Set the info window to show their name.
            mImageView.setImageDrawable(Attraction.profilePhoto);
            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(Attraction.name);
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<Attraction> cluster, MarkerOptions markerOptions) {
            // Draw multiple people.
            // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
            List<Drawable> profilePhotos = new ArrayList<Drawable>(Math.min(4, cluster.getSize()));
            int width = mDimension;
            int height = mDimension;
            Drawable target = null;
            if (cluster.getItems().size() <5)
                target = getResources().getDrawable(R.drawable.bubble_lessthan5);
            else if (cluster.getItems().size() >=5 && cluster.getItems().size() <10)
                target = getResources().getDrawable(R.drawable.bubble_5to10);
            else if (cluster.getItems().size() >=10 && cluster.getItems().size() <20)
                target = getResources().getDrawable(R.drawable.bubble_10to20);
            else
                target = getResources().getDrawable(R.drawable.bubble_morethan20);

            for (Attraction p : cluster.getItems()) {
                // Draw 4 at most.
                if (profilePhotos.size() == 4) break;
                //Drawable drawable = p.profilePhoto;
                Drawable drawable = target;
                drawable.setBounds(0, 0, width, height);
                profilePhotos.add(drawable);
            }
            MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
            multiDrawable.setBounds(0, 0, width, height);

            mClusterImageView.setImageDrawable(multiDrawable);
            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }
    }

    @Override
    public boolean onClusterClick(Cluster<Attraction> cluster) {
        // Show a toast with some info when the cluster is clicked.
        String firstName = cluster.getItems().iterator().next().name;
        Toast.makeText(this, cluster.getSize() + " (including " + firstName + ")", Toast.LENGTH_SHORT).show();

        // Zoom in the cluster. Need to create LatLngBounds and including all the cluster items
        // inside of bounds, then animate to center of the bounds.

        // Create the builder to collect all essential cluster items for the bounds.
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
        // Get the LatLngBounds
        final LatLngBounds bounds = builder.build();

        // Animate camera to the bounds
        try {
            Log.d("test==============", "the bound is" +  bounds.toString());;
            getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<Attraction> cluster) {
        // Does nothing, but you could go to a list of the users.
    }

    @Override
    public boolean onClusterItemClick(Attraction item) {
        // Does nothing, but you could go into the user's profile page, for example.
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(Attraction item) {
        // Does nothing, but you could go into the user's profile page, for example.
    }

    @Override
    protected void startDemo() {
        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(1.290270, 103.851959), 15.0f));

        mClusterManager = new ClusterManager<Attraction>(this, getMap());
        mClusterManager.setRenderer(new AttractionRenderer());
        getMap().setOnCameraIdleListener(mClusterManager);
        getMap().setOnMarkerClickListener(mClusterManager);
        getMap().setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

        addItems();
        mClusterManager.cluster();
    }

    private void addItems() {
        // http://www.flickr.com/photos/sdasmarchives/5036248203/
        for(Attraction a : AttractionLoader.getAttractionList())
            mClusterManager.addItem(a);
//
//        // http://www.flickr.com/photos/usnationalarchives/4726917149/
//        mClusterManager.addItem(new Attraction(position(), "Gran", R.drawable.gran));
//
//        // http://www.flickr.com/photos/nypl/3111525394/
//        mClusterManager.addItem(new Attraction(position(), "Ruth", R.drawable.ruth));
//
//        // http://www.flickr.com/photos/smithsonian/2887433330/
//        mClusterManager.addItem(new Attraction(position(), "Stefan", R.drawable.stefan));
//
//        // http://www.flickr.com/photos/library_of_congress/2179915182/
//        mClusterManager.addItem(new Attraction(position(), "Mechanic", R.drawable.mechanic));
//
//        // http://www.flickr.com/photos/nationalmediamuseum/7893552556/
//        mClusterManager.addItem(new Attraction(position(), "Yeats", R.drawable.yeats));
//
//        // http://www.flickr.com/photos/sdasmarchives/5036231225/
//        mClusterManager.addItem(new Attraction(position(), "John", R.drawable.john));
//
//        // http://www.flickr.com/photos/anmm_thecommons/7694202096/
//        mClusterManager.addItem(new Attraction(position(), "Trevor the Turtle", R.drawable.turtle));
//
//        // http://www.flickr.com/photos/usnationalarchives/4726892651/
//        mClusterManager.addItem(new Attraction(position(), "Teach", R.drawable.teacher));
    }

    private LatLng position() {
        return new LatLng(random(51.6723432, 51.38494009999999), random(0.148271, -0.3514683));
    }

    private double random(double min, double max) {
        return mRandom.nextDouble() * (max - min) + min;
    }
}
