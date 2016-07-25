package com.example.sagar.restaurantlocatorinandroid;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Sagar on 7/18/2016.
 */

public class Places implements Serializable,Comparable{
    private String id;

    private String icon;
    private String name;
    private String vicinity;
    private Double latitude;
    private Double longitude;
    private Float rating;

    public String getPhotoreference() {
        return photoreference;
    }

    public void setPhotoreference(String photoreference) {
        this.photoreference = photoreference;
    }

    private String photoreference;

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public  static Places extractJSONData(JSONObject jsonObject) {
        try {
            Log.d("extractJSONData", jsonObject.toString());
            Places result = new Places();

            JSONObject geometry = (JSONObject) jsonObject.get("geometry");
            JSONObject location = (JSONObject) geometry.get("location");
            result.setLatitude((Double) location.get("lat"));
            result.setLongitude((Double) location.get("lng"));
            result.setIcon(jsonObject.getString("icon"));
            result.setName(jsonObject.getString("name"));
            result.setVicinity(jsonObject.getString("vicinity"));

            if(jsonObject.has("id")) {
                result.setId(jsonObject.getString("id"));
            }


            if (jsonObject.has("rating")) {
                Log.d("Ratings value!!!" , String.valueOf( jsonObject.get("rating")));
                result.setRating(Float.valueOf(String.valueOf(jsonObject.get("rating"))));
            }
            else {
                result.setRating(new Float(0.0));
            }


            if(jsonObject.has("photos")) {
                JSONArray photosdata = (JSONArray) jsonObject.getJSONArray("photos");
                Log.d("photosdata" ,photosdata.toString());

                String photo_reference = ((JSONObject)photosdata.get(0)).get("photo_reference").toString();
                result.setPhotoreference(photo_reference);
                Log.d("photo_reference" , photo_reference);
            } else {
                Log.d("photosdata" , "not found");
            }

            return result;
        } catch (JSONException ex) {
            Logger.getLogger(Places.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }



    @Override
    public String toString() {
        return "Place{" + "id=" + id + ", icon=" + icon + ", name=" + name + ", latitude=" + latitude + ", longitude=" + longitude +  ", rating=" + rating+  ", photoreference=" +photoreference +'}';
    }


    @Override
    public int compareTo(Object RestaurantList) {
        Places obj = (Places) RestaurantList;
        return obj.getRating().compareTo(this.getRating());
    }
}

