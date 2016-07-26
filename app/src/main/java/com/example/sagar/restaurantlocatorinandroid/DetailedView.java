package com.example.sagar.restaurantlocatorinandroid;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Sagar on 7/25/2016.
 */
public class DetailedView extends Activity {

    private RatingBar mRatingsView;
    private ImageView mPriceLevel;
    private TextView detailedname;
    private ImageView detailedmainrestaurantimage;
    ImageView detailedopennow;
    TextView detailedvicinity;
    String isOpen ;
    private static final String API_KEY = "AIzaSyAUOx1zt79BHU9g0uFA00OydezvAd3UU1Q";


    @Override
    protected void onCreate(Bundle savedInstanceState){
        String recyclerelement =  getIntent().getStringExtra("ItemPosition");
         int elementid = Integer.valueOf(recyclerelement);
        //Log.d("elementid" ,elementid);

        ArrayList<Places> restaurantList = new ArrayList<Places>();
        restaurantList = (ArrayList<Places>) getIntent().getSerializableExtra("PlacesList");

        Log.d("Received List%%%" , restaurantList.toString());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailedmodulefragment);
        detailedname = (TextView)findViewById(R.id.detailednameTextView);
        detailedopennow= (ImageView) findViewById(R.id.detailedopennow);
        mPriceLevel = (ImageView) findViewById((R.id.detailedcost));
        detailedvicinity = (TextView) findViewById(R.id.detailedVicinityView);
        mRatingsView = (RatingBar) findViewById(R.id.detailedlistrating);
        detailedmainrestaurantimage = (ImageView) findViewById(R.id.detailmainimage);

        Log.d("Data%%" , restaurantList.get(elementid).toString());
        detailedname.setText(restaurantList.get(elementid).getName());
        detailedname.setTextColor(getResources().getColor(R.color.black));


        detailedvicinity.setText(restaurantList.get(elementid).getVicinity());
        detailedvicinity.setTextColor(getResources().getColor(R.color.fblue));
        float ratingvalue = (Float) restaurantList.get(elementid).getRating();

        mRatingsView.setRating(ratingvalue);
       // mRatingsView.setNumStar(5);

        /**
         * Set the Cost
         */


        if(restaurantList.get(elementid).getPrice_level() == 1){
            mPriceLevel.setImageDrawable(getResources().getDrawable(R.drawable.ic_sentiment_satisfied_black_24dp));

        } else  if(restaurantList.get(elementid).getPrice_level() == 2){
            mPriceLevel.setImageDrawable(getResources().getDrawable(R.drawable.ic_sentiment_dissatisfied_black_24dp));
        }
         else {
            mPriceLevel.setImageDrawable(getResources().getDrawable(R.drawable.ic_sentiment_neutral_black_24dp));
        }

        /**
         * Display if the restaurant is open
         */

        if(restaurantList.get(elementid).getOpennow() == "true"){
            detailedopennow.setImageDrawable(getResources().getDrawable(R.drawable.open_sign));

        } else {
            detailedopennow.setImageDrawable(getResources().getDrawable(R.drawable.is_not_open));
        }


        String restaurantpic ;
        int price_level = restaurantList.get(elementid).getPrice_level();
        String photoreference = restaurantList.get(elementid).getPhotoreference();
        if(photoreference==null){
            Log.d("NO ", "Photo found");
            restaurantpic = "http://il1.picdn.net/shutterstock/videos/1679470/thumb/1.jpg?i10c=img.resize(height:160)";
        } else {
            restaurantpic = "https://maps.googleapis.com/maps/api/place/photo?" +
                    "maxwidth=800" +
                    "&photoreference=" +photoreference +
                    "&key="+API_KEY;
        }

        Log.d("Loading restaurantpic" , restaurantpic);

        Glide
                .with(getApplicationContext())
                .load(restaurantpic)
                .centerCrop()
                .crossFade()
                .into(detailedmainrestaurantimage);
    }




}
