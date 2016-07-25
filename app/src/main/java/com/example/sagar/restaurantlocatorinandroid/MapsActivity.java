package com.example.sagar.restaurantlocatorinandroid;


import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity {

    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    private static final String TAG = "MapsActivity";
    private ViewPager viewPager;
    private TabLayout tabLayout;
    static ViewPagerAdapter adapter;
    static ConnectivityManager connectivity;
    private int[] tabIcons = {R.drawable.ic_menu_map,R.drawable.ic_menu_list};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp);

        connectivity = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
    }

    private void setupViewPager(ViewPager viewPager) {

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(TabFragmentOne.newInstance("this data is for fragment 1"), "One");
        adapter.addFragment(TabFragmentTwo.newInstance(new ArrayList<Places>()), "Two");
        viewPager.setAdapter(adapter);

    }


        public static boolean isConnectedToInternet(){
            if (connectivity != null)
            {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null)
                    for (int i = 0; i < info.length; i++)
                        if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        {
                            return true;
                        }

            }
            return false;
        }
    }



