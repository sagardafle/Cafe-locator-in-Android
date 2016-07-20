package com.example.sagar.restaurantlocatorinandroid;

/**
 * Created by Sagar on 7/18/2016.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TabFragmentTwo extends Fragment {

    private static final String ARG_EXAMPLE = "this_is_a_constant";
    private String example_data;

    public TabFragmentTwo() {

    }

    public static TabFragmentTwo newInstance(String example_argmument) {
        TabFragmentTwo tabFragmentTwo = new TabFragmentTwo();
        Bundle args = new Bundle();
        args.putString(ARG_EXAMPLE, example_argmument);
        tabFragmentTwo.setArguments(args);
        return tabFragmentTwo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        example_data = getArguments().getString(ARG_EXAMPLE);
        Log.i("Fragment created with ", example_data);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String value = getArguments().getString("YourKey");
        if(value != null){
            Log.d("Received Fragment Data" ,value);
        }else{
            Log.d("Received Fragment Data" ,"is empty");
        }

        return inflater.inflate(R.layout.fragment_two, container, false);

    }
}
