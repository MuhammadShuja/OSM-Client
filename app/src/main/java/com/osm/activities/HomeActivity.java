package com.osm.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.osm.API.APIResponse;
import com.osm.R;
import com.osm.fragments.FriendsFragment;
import com.osm.fragments.InboxFragment;
import com.osm.fragments.MergerRequestsFragment;
import com.osm.fragments.ProfileFragment;
import com.osm.API.OSM;
import com.osm.fragments.RequestsFragment;

public class  HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        InboxFragment.OnFragmentInteractionListener,
        FriendsFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener,
        RequestsFragment.OnFragmentInteractionListener,
        MergerRequestsFragment.OnFragmentInteractionListener {

    private Fragment fragment = null;
    private String fragmentTitle = null;
    private Class fragmentClass = null;

    private TextView tvNavName, tvNavUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        tvNavName = (TextView) header.findViewById(R.id.tvNavname);
        tvNavName.setText(OSM.name);
        tvNavUsername = (TextView) header.findViewById(R.id.tvNavUsername);
        tvNavUsername.setText("@"+OSM.username);

        fragmentClass = InboxFragment.class;
        loadFragment();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuNewMerger) {
            fragmentTitle = "Friends";
            fragmentClass = FriendsFragment.class;
            loadFragment();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        fragmentTitle = String.valueOf(item.getTitle());

        if (id == R.id.nav_home) {
            fragmentClass = InboxFragment.class;
        } else if (id == R.id.nav_friends) {
            fragmentClass = FriendsFragment.class;
        } else if (id == R.id.nav_profile) {
            fragmentClass = ProfileFragment.class;
        } else if (id == R.id.nav_logout) {
            OSM.getInstance(getApplicationContext()).logout(new APIResponse.AuthListener() {
                @Override
                public void onSuccess(String response) {
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(HomeActivity.this, AuthActivity.class));
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }

        loadFragment();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadFragment(){
        try{
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
//        fragmentManager.beginTransaction().replace(R.id.mainContainer, fragment).commit();
        if(fragmentTitle != null){
            setTitle(fragmentTitle);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
