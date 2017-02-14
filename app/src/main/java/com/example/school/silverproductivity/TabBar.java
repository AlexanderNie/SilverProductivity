package com.example.school.silverproductivity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

//ActionBarActivity
public class TabBar extends Activity implements  FragmentTab5.OnUploadRequestListener, FragTab5Upload.OnCompleteUpload {

    ActionBar.Tab tab1, tab2, tab3, tab4, tab5;
    Fragment fragmentTab1 = new FragmentTab1();
    Fragment fragmentTab2 = new FragmentTab2();
    Fragment fragmentTab3 = new FragMentFrameActivity();
    Fragment fragmentTab4 = new FragmentTab4();
    Fragment fragmentTab5 = new FragmentTab5();
    FragTab5Upload fragUpload = new FragTab5Upload();

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

        tab5 = actionBar.newTab();
        //tab4.setText("Profile");
        tab5.setIcon(R.drawable.icon_action_cam);

        tab1.setTabListener(new MyTabListener(fragmentTab1));
        tab2.setTabListener(new MyTabListener(fragmentTab2));
        tab3.setTabListener(new MyTabListener(fragmentTab3));
        tab4.setTabListener(new MyTabListener(fragmentTab4));
        tab5.setTabListener(new MyTabListener(fragmentTab5));

        actionBar.addTab(tab1);
        actionBar.addTab(tab2);
        actionBar.addTab(tab3);
        actionBar.addTab(tab4);
        actionBar.addTab(tab5);
    }

    @Override
    public void UploadRequest(String filePath, Boolean isImg) {

            // Create fragment and give it an argument for the selected article

            Bundle args = new Bundle();
            args.putString(FragTab5Upload.ARG_FILEPATH, filePath);
            args.putBoolean(FragTab5Upload.ARG_ISIMG, isImg);
            fragUpload.setArguments(args);

            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, fragUpload);

            // Commit the transaction
            transaction.commit();


    }

    @Override
    public void CompleteUpload() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.remove(fragUpload);
        transaction.replace(R.id.fragment_container, fragmentTab5);
        //transaction.addToBackStack(null);
        transaction.commit();
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
