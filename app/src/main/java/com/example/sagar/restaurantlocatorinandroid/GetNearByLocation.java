package com.example.sagar.restaurantlocatorinandroid;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Sagar on 7/18/2016.
 */
public class GetNearByLocation {

    private static final String LOG_TAG = "Google Place";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place/nearbysearch";
    private static final String OUT_JSON = "/json";
    private static final String LOCATION = "?location=";
    private static final String RADIUS = "&radius=500";
    private static final String TYPE = "&type=";
    private static final String API_KEY = "&key=AIzaSyAUOx1zt79BHU9g0uFA00OydezvAd3UU1Q";

    GoogleMap mGoogleMap;
    static ArrayList<Places> arrayList = new ArrayList<>();
    double destlatitude, destlongitude;

    public GetNearByLocation(GoogleMap mGoogleMap, double destlatitude, double destlongitude) {
        this.mGoogleMap = mGoogleMap;
        this.destlatitude = destlatitude;
        this.destlongitude = destlongitude;
    }

    public void findnearbyCafes() {


        ArrayList resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();

        StringBuilder sb = new StringBuilder(PLACES_API_BASE + OUT_JSON + LOCATION);
        sb.append(destlatitude + "," + destlongitude);
        sb.append(RADIUS);
        sb.append(TYPE);
        sb.append("cafe");
        sb.append(API_KEY);
        String urlString = sb.toString();
        Log.d("NEW URL IS", urlString);
        try {
            new CallHTTPServices().execute(urlString);
        } catch (Exception e) {
        }

    }


    class CallHTTPServices extends AsyncTask<String, Void, String> {



        @Override
        protected String doInBackground(String... params) {

            Log.d("URL async", params[0]);
            StringBuilder content = new StringBuilder();
            try {
                HttpURLConnection conn = null;
                URL url = new URL(params[0]);
                Log.d("RECEIVED URL", params[0]);
                conn = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()), 8);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    content.append(line + "\n");
                }
                //Log.d("CONTENT!!", content.toString());
                bufferedReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return content.toString();

        }

        @Override
        protected void onPostExecute(String str) {

            Log.d("Final++++", str);
            super.onPostExecute(str);

          //  parseJSON(str);
            JSONObject object = null;
            try {
                object = new JSONObject(str);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray array = null;
            try {
                array = object.getJSONArray("results");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        if(!arrayList.isEmpty() && arrayList.size()>0){
            Log.d("Clearing the list" , arrayList.toString());
            arrayList.clear();

        }

        Log.d("ArrayL b4 add  places",arrayList.toString());


            for (int i = 0; i < array.length(); i++) {
                try {
                    Places places = Places.extractJSONData((JSONObject) array.get(i));
                    arrayList.add(places);
                } catch (Exception e) {
                }
            }

            Log.d("Arraylist  printing",arrayList.toString());
            Log.d("Arraylist  size",String.valueOf(arrayList.size()));
            if(!arrayList.isEmpty()){

                for (int i = 0; i < arrayList.size(); i++) {
                    if((arrayList.get(i).getName() != "") && (arrayList.get(i).getLatitude()!= TabFragmentOne.destination_latitude) && (arrayList.get(i).getLatitude()!= TabFragmentOne.destination_longitude)){
                        mGoogleMap.addMarker(new MarkerOptions()
                                .title(arrayList.get(i).getName())
                                .position(
                                        new LatLng(arrayList.get(i).getLatitude(), arrayList
                                                .get(i).getLongitude()))
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                .snippet(arrayList.get(i).getVicinity()));
                    } else {
                        //Searchhed restaurant
                        mGoogleMap.addMarker(new MarkerOptions()
                                .title(arrayList.get(i).getName())
                                .position(
                                        new LatLng(arrayList.get(i).getLatitude(), arrayList
                                                .get(i).getLongitude()))
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                .snippet(arrayList.get(i).getVicinity()));
                    }
                }

                Log.d("Arraylist camera size" , String.valueOf(arrayList.size()));
                //Log.d("Moving camera to^^^^^^^", ));


                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(
                                new LatLng(
                                        TabFragmentOne.destination_latitude,
                                        TabFragmentOne.destination_longitude
                                    )
                                 ) // Sets the center of the map to
                        .zoom(16) // Sets the zoom
                        .tilt(30) // Sets the tilt of the camera to 30 degrees
                        .build(); // Creates a CameraPosition from the builder
                mGoogleMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));
               // arrayList.clear();


            MapsActivity.adapter.removeFragment();
                MapsActivity.adapter.addFragment(TabFragmentTwo.newInstance(arrayList),"Section 2");


                MapsActivity.adapter.notifyDataSetChanged();
                //MapsActivity.adapter.getItem(1);

            }



        }

        }

    }



