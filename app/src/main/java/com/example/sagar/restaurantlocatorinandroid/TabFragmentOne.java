package com.example.sagar.restaurantlocatorinandroid;

/**
 * Created by Sagar on 7/18/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class TabFragmentOne extends Fragment implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        OnItemClickListener {

    private static final String ARG_EXAMPLE = "this_is_a_constant";
    private static final float DEFAULTZOOM = 16.0f;
    private String example_data;
    private static CoordinatorLayout coordinatorLayout;
    GoogleMap mGoogleMap;
    static double destination_latitude;
    static double destination_longitude;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    static View myinflater;
    private static final String TAG = "MapsActivity";
    // static Bundle bundle;

    public TabFragmentOne() {

    }

    public static TabFragmentOne newInstance(String example_argmument) {
        TabFragmentOne tabFragmentOne = new TabFragmentOne();
        Bundle args = new Bundle();
        args.putString(ARG_EXAMPLE, example_argmument);
        tabFragmentOne.setArguments(args);
        return tabFragmentOne;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        example_data = getArguments().getString(ARG_EXAMPLE);
        Log.i("Fragment created with ", example_data);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        myinflater = inflater.inflate(R.layout.fragment_one, container, false);
        mGoogleMap = ((MapFragment) getActivity().getFragmentManager()
                .findFragmentById(R.id.map)).getMap();


         coordinatorLayout = (CoordinatorLayout) myinflater.findViewById(R.id
                .coordinatorLayout);
        /**
         * Check if mobile or wifi is connected
         */
        checkForNetworkError();


        /**
         * Check if GPS is connected
         */

        checkforGPSError();


        AutoCompleteTextView autoCompView = (AutoCompleteTextView) myinflater.findViewById(R.id.autoCompleteTextView);

       checkForNetworkError();


        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(getContext(), R.layout.list_item));

        autoCompView.setOnItemClickListener(this);


        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this.getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }

        return myinflater;
    }

    private void checkforGPSError() {

        if (!isGPSEnabled()) {

            Snackbar gpsconnectivitysnackbar = Snackbar.make
                    (
                            myinflater.findViewById(R.id.map),
                            "No GPS connectivity",
                            Snackbar.LENGTH_LONG
                    )
                    .setDuration(6000)
                    .setAction("Enable", new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                            //launch settings tab.
                        }

                    });


            View gpsconenctivitysnackBarView = gpsconnectivitysnackbar.getView();
            gpsconenctivitysnackBarView.setBackgroundColor(Color.parseColor("#cc0000"));
            gpsconnectivitysnackbar.show();
        }
    }

    private void checkForNetworkError() {

        if (!isNetworkConnected()) {
            Snackbar internetconnectivitysnackbar = Snackbar.make
                    (
                            coordinatorLayout,
                            "No internet connectivity",
                            Snackbar.LENGTH_LONG
                    )
                    .setDuration(6000)
                    .setAction("Enable", new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                            //launch settings tab.
                        }

                    });


            View internetconenctivitysnackBarView = internetconnectivitysnackbar.getView();
            internetconenctivitysnackBarView.setBackgroundColor(Color.parseColor("#cc0000"));
            internetconnectivitysnackbar.show();
        }
    }

    private boolean isNetworkConnected() {

        ConnectivityManager conMan = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        //mobile
        NetworkInfo.State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();

        //wifi
        NetworkInfo.State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();

        if (
                (mobile != NetworkInfo.State.CONNECTED)
                        &&
                (wifi != NetworkInfo.State.CONNECTED)
                ) {
            Log.d("Not  Connected to ", " network");

            return false;
        }
        return true;
    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = (LocationManager)
                getContext().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(locationManager.GPS_PROVIDER);
    }


    public void onItemClick(AdapterView adapterView, View view, int position, long id) {
        Log.d("Inside", "onItemClick");
        String str = (String) adapterView.getItemAtPosition(position);
        Log.d("The selected place is", str);
        try {
            getLatLong(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getLatLong(String inputaddress) throws IOException {
        Geocoder mygeocoder = new Geocoder(this.getContext());
        List<android.location.Address> gclist;
        gclist = mygeocoder.getFromLocationName(inputaddress, 1);
        Log.d("gclist", String.valueOf(gclist));
        if (gclist.size() != 0) {
            Address add = gclist.get(0);
            String locality = add.getLocality();
            destination_latitude = add.getLatitude();
            destination_longitude = add.getLongitude();
            Log.d("Dest1 lat", String.valueOf(destination_latitude));
            Log.d("Dest1 lng", String.valueOf(destination_longitude));
            gotoLocation(destination_latitude, destination_longitude, DEFAULTZOOM);
        } else {
            if (ActivityCompat.checkSelfPermission(this.getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            Snackbar snackbar = Snackbar.make(getView(), "No nearby restaurants found here",
                    Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#cc0000"));
            snackbar.show();

        }

    }

    private void gotoLocation(double latitude, double longitude, float zoom) {
        Log.d("inside ", " gotoLocation");
        LatLng ll = new LatLng(latitude, longitude);
        Log.d("Dest lat", String.valueOf(latitude));
        Log.d("Dest lng", String.valueOf(longitude));
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mGoogleMap.moveCamera(update);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(ll);
        markerOptions.title("Searched Restaurant");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

        /**
         * Call to google Place API to get the nearby locations
         */
        GetNearByLocation nearbylocation = new GetNearByLocation(mGoogleMap, latitude, longitude);
        nearbylocation.findnearbyCafes();

    }


    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this.getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("Inside ", " onConnected");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this.getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("Inside ", " onLocationChanged");
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

        //move map camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); //set the view to normal mode

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        Log.d("Inside ", "check permsission");
        if (ContextCompat.checkSelfPermission(this.getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                Log.d("IF Requesting ", " the permission");
                ActivityCompat.requestPermissions(this.getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                Log.d("ELSE Requesting ", " the permission");
                ActivityCompat.requestPermissions(this.getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            Log.d("OUTER", "ELSE");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d("Inside ", " onRequestPermissionsResult");
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this.getActivity(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this.getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
