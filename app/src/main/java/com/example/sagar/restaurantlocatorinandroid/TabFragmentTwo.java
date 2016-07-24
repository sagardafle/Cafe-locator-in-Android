package com.example.sagar.restaurantlocatorinandroid;

/**
 * Created by Sagar on 7/18/2016.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class TabFragmentTwo extends Fragment {

    private static final String ARG_EXAMPLE = "this_is_a_constant";
    private String example_data;
    private static RestaurantAdapter mAdapter;
    private static RecyclerView recyclerView;
    private static ArrayList<Places> mPlacesList;
    public TabFragmentTwo() {

    }

    public static TabFragmentTwo newInstance(ArrayList<Places> restaurantList) {
        Log.d("checking","fragment is getting called "+restaurantList.size());
        TabFragmentTwo fragment = new TabFragmentTwo();
        mPlacesList = restaurantList;
        for(Places places : restaurantList){
            Log.d("checking",places.toString());
        }

        Collections.sort(restaurantList);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_two,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.expense_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }
    public void updateUI() {
        Log.d("Inside*************** ", " updateUI");
       /* if (mAdapter == null) {*/
            mAdapter = new RestaurantAdapter(mPlacesList);
            Log.d("Adapter class " , mAdapter.toString());
            recyclerView.setAdapter(mAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        /*} else {
            Log.d("Else Adapter class " , mAdapter.toString());
            mAdapter.notifyDataSetChanged();
        }*/
    }

    private static class RestaurantListHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView mNameTextView;

        private Places mPlaces;

        public RestaurantListHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mNameTextView = (TextView) itemView.findViewById(R.id.nameTextView);

        }

        public void bindExpense(Places places) {
            mPlaces = places;
            mNameTextView.setText(mPlaces.getName());

        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
//            Intent intent = new Intent(getActivity(), ExpenseDetail.class);
//            intent.putExtra("Expense_Id",mPlaces.getId());
//            startActivity(intent);
        }


    }


    private class RestaurantAdapter extends RecyclerView.Adapter<RestaurantListHolder> {
        private ArrayList<Places> mRestaurantLists;
        private RestaurantAdapter mRestaurantAdapterobj ;

        //static RestaurantAdapter instance;



        public RestaurantAdapter(ArrayList<Places> restaurants) {
            mRestaurantLists = restaurants;

        }

        @Override
        public RestaurantListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_restaurants, parent, false);
            return new RestaurantListHolder(view);
        }

        @Override
        public void onBindViewHolder(RestaurantListHolder holder, int position) {
            Places restaurants = mRestaurantLists.get(position);
            holder.bindExpense(restaurants);
        }

        @Override
        public int getItemCount() {
            return mRestaurantLists.size();
        }

    }
}
