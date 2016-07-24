package com.example.sagar.restaurantlocatorinandroid;

import android.util.Log;

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

    public  static Places jsonToPontoReferencia(JSONObject pontoReferencia) {
        try {
            Log.d("jsonToPontoReferencia", pontoReferencia.toString());
            Places result = new Places();
            JSONObject geometry = (JSONObject) pontoReferencia.get("geometry");
            JSONObject location = (JSONObject) geometry.get("location");
            result.setLatitude((Double) location.get("lat"));
            result.setLongitude((Double) location.get("lng"));
            result.setIcon(pontoReferencia.getString("icon"));
            result.setName(pontoReferencia.getString("name"));
            result.setVicinity(pontoReferencia.getString("vicinity"));
            result.setId(pontoReferencia.getString("id"));

            if (pontoReferencia.has("rating")) {
                Log.d("Ratings value!!!" , String.valueOf( pontoReferencia.get("rating")));
                result.setRating(Float.valueOf(String.valueOf(pontoReferencia.get("rating"))));
            }
            else {
                result.setRating(new Float(0.0));
            }


            return result;
        } catch (JSONException ex) {
            Logger.getLogger(Places.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }



    @Override
    public String toString() {
        return "Place{" + "id=" + id + ", icon=" + icon + ", name=" + name + ", latitude=" + latitude + ", longitude=" + longitude +  ", rating=" + rating+ '}';
    }


    @Override
    public int compareTo(Object RestaurantList) {
        Places obj = (Places) RestaurantList;
        return obj.getRating().compareTo(this.getRating());
    }
}

