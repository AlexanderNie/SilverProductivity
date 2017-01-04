package com.example.school.silverproductivity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;

public class MyTabListener implements ActionBar.TabListener {
    Fragment fragment;

    public MyTabListener(Fragment fragment){
        this.fragment = fragment;
    }

    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft){
        ft.replace(R.id.fragment_container, fragment);
    }

    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft){
        ft.remove(fragment);
    }

    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft){
        // nothing done here
    }
}