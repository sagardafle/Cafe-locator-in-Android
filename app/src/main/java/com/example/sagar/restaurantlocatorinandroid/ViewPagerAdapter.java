package com.example.sagar.restaurantlocatorinandroid;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Sagar on 7/18/2016.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private ArrayList<String> mFragmentListTitles = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override public Fragment getItem(int position) {
        if(position==0){
            Log.d("checking","fragment 0 is getting called");
            return mFragmentList.get(position);
        }
        else{
            Log.d("checking","inside else");
/*            if(GetNearByLocation.arrayList!=null && GetNearByLocation.arrayList.size()!=0 ){*/
                Log.d("checking","fragment 1 is getting created with list");
                TabFragmentTwo tabFragmentTwo = TabFragmentTwo.newInstance(GetNearByLocation.arrayList);
              //  tabFragmentTwo.updateUI();
                return tabFragmentTwo;
           /* }
            else{
                Log.d("checking","fragment 1 is getting created with blank");
                return TabFragmentTwo.newInstance(new ArrayList<Places>());
            }*/
        }

    }

    @Override public int getCount() {
        return 2;
    }

    public void addFragment(Fragment fragment, String title){

        mFragmentList.add(fragment);
        mFragmentListTitles.add(title);
    }

    public void removeFragment(){
        mFragmentList.remove(1);
    }

    @Override public CharSequence getPageTitle(int position) {
        // return mFragmentListTitles.get(position);
        return null;
    }
}