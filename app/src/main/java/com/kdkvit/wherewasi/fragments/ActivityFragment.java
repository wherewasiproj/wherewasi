package com.kdkvit.wherewasi.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.core.util.Pair;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.kdkvit.wherewasi.MapActivity;
import com.kdkvit.wherewasi.R;
import com.kdkvit.wherewasi.adapters.LocationsTabsAdapter;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import models.LocationsGroup;
import models.MyLocation;
import utils.DatabaseHandler;

import static com.kdkvit.wherewasi.services.LocationService.BROADCAST_CHANNEL;
import static com.kdkvit.wherewasi.utils.General.checkIfLocationInGroup;


public class ActivityFragment extends Fragment {
    BroadcastReceiver receiver;
    DatabaseHandler db;

    TimeLineFragment timeLineFragment = new TimeLineFragment(new TimeLineFragment.TimeLineLocationListener() {
        @Override
        public void onClick(int position) {
            onTLLocationClick(position);
        }
    });

    //MapsFragment mapsFragment = new MapsFragment();

    public static List<LocationsGroup> locations = new ArrayList<>();

    boolean dbInit = false;
    Handler handler;
    private TabLayout tabLayout;

    private View rootView;
    private DrawerLayout drawerLayout;


    public ActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_activity, container, false);
        handler = new Handler();
        db = new DatabaseHandler(rootView.getContext());

        assert getFragmentManager() != null;
//        LocationsTabsAdapter locationsTabsAdapter = new LocationsTabsAdapter(getFragmentManager(),1);
//        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.locations_view_pager);
//        tabLayout = (TabLayout) rootView.findViewById(R.id.locations_tab_layout);
//
//        locationsTabsAdapter.addFragment(timeLineFragment,"TimeLine");
//        //locationsTabsAdapter.addFragment(mapsFragment,"Map");
//
//        viewPager.setAdapter(locationsTabsAdapter);
//        tabLayout.setupWithViewPager(viewPager);
//        TextView tv = rootView.findViewById(R.id.title_test);
//        tv.setText("hello world");


        getFragmentManager().beginTransaction().replace(R.id.activity_fragment_container, timeLineFragment).commit();

        final FiltersFragment filtersFragment = new FiltersFragment(new FiltersFragment.FiltersCallback() {
            @Override
            public void onClear() {

            }

            @Override
            public void onFilter(Date start, Date end, int minTime, boolean onlyInteractions) {
                drawerLayout.closeDrawer(GravityCompat.END);
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        if(end!=null){
                            end.setTime(end.getTime() + 24 * 60 * 60 * 1000); //Add 24 hours to include the day
                        }
                        locations = db.getAllLocations(start,end,minTime,onlyInteractions);

                        dbInit = true;
                        handler.post(()-> {
                            timeLineFragment.updateTimeLineAdapter();
                            //mapsFragment.setMapPointers();
                        });
                    }
                }.start();
            }

        });

        getFragmentManager().beginTransaction().replace(R.id.activity_fragment_container, timeLineFragment).commit();
        getFragmentManager().beginTransaction().replace(R.id.activity_filters_fragment_container, filtersFragment).commit();

        initReceiver();

        drawerLayout = rootView.findViewById(R.id.activity_filters_drawer);
        ImageView filterBtn = rootView.findViewById(R.id.header_right_icon);

        getLocationsHistory();

        return rootView;
    }

    public void openDrawer(){
        drawerLayout.openDrawer(GravityCompat.END);
    }

    private  void getLocationsHistory(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                locations = db.getAllLocations(null,null,0,false);

                dbInit = true;
                handler.post(()-> {
                    timeLineFragment.updateTimeLineAdapter();
                    //mapsFragment.setMapPointers();
                });
            }
        }.start();
    }

    private void onTLLocationClick(int position) {
//        tabLayout.getTabAt(1).select();
//        mapsFragment.focus(locations.get(position).getLocations().get(0),false);
        Intent intent = new Intent(rootView.getContext(), MapActivity.class);
        intent.putExtra("locations_group",position);
        startActivity(intent);
    }

    private void initReceiver() {
        IntentFilter filter = new IntentFilter(BROADCAST_CHANNEL);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String command = intent.getStringExtra("command");
                if (command != null) {
                    switch (command) {
                        case "new_location":
                            if(dbInit){
//                                MyLocation location = (MyLocation) intent.getSerializableExtra("location");
//                                if(locations.size() == 0 || !checkIfLocationInGroup(locations.get(0),location)) {
//                                    locations.add(0,new LocationsGroup());
//                                }
//                                locations.get(0).addLocation(location);
//                                timeLineFragment.updateTimeLineAdapter();
                                //mapsFragment.setMapPointers();
                            }
                        case "location_changed":
                            if(dbInit) {
//                                MyLocation location = (MyLocation)intent.getSerializableExtra("location");
//                                Log.i("changed","location");
//                                locations.get(0).getLocations().remove(0);
//                                locations.get(0).getLocations().add(0,location);
//                                timeLineFragment.updateTimeLineAdapter();
                                //mapsFragment.setMapPointers();
                            }
                            break;
                        case "close":
                            break;
                    }
                }
            }
        };
        LocalBroadcastManager.getInstance(rootView.getContext()).registerReceiver(receiver, filter);

    }

    @Override
    public void onDestroyView() {
        LocalBroadcastManager.getInstance(rootView.getContext()).unregisterReceiver(receiver);
        super.onDestroyView();
    }
}