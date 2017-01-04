package com.example.school.silverproductivity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

//ActionBarActivity
public class TabBar extends Activity {

    ActionBar.Tab tab1, tab2, tab3, tab4;
    Fragment fragmentTab1 = new FragmentTab1();
    Fragment fragmentTab2 = new FragmentTab2();
    Fragment fragmentTab3 = new FragmentTab3();
    Fragment fragmentTab4 = new FragmentTab4();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_bar);

        setTitle("Silver Productivity");

        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        tab1 = actionBar.newTab();
        //tab1.setText("Stories");
        tab1.setIcon(R.drawable.ic_action_picture);

        tab2 = actionBar.newTab();
        //tab2.setText("Friends");
        tab2.setIcon(R.drawable.ic_action_group);

        tab3 = actionBar.newTab();
        //tab3.setText("Alert");
        tab3.setIcon(R.drawable.ic_action_read);

        tab4 = actionBar.newTab();
        //tab4.setText("Profile");
        tab4.setIcon(R.drawable.ic_action_person);

        tab1.setTabListener(new MyTabListener(fragmentTab1));
        tab2.setTabListener(new MyTabListener(fragmentTab2));
        tab3.setTabListener(new MyTabListener(fragmentTab3));
        tab4.setTabListener(new MyTabListener(fragmentTab4));

        actionBar.addTab(tab1);
        actionBar.addTab(tab2);
        actionBar.addTab(tab3);
        actionBar.addTab(tab4);
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tab_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */
}
