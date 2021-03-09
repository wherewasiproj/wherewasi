package com.kdkvit.wherewasi.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.kdkvit.wherewasi.R;

import static com.kdkvit.wherewasi.MainActivity.user;

public class MainFragment extends Fragment {

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    private View rootView;

    public MainFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        TextView nameTV = rootView.findViewById(R.id.name_tv);
        nameTV.setText(getResources().getString(R.string.hello_name)+user.getName());
        drawerLayout = (DrawerLayout) rootView.findViewById(R.id.drawer_layout);
        navigationView = rootView.findViewById(R.id.navigation_view);

        ImageButton menuBtn = rootView.findViewById(R.id.menu_btn);
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawer(GravityCompat.START);
                Toast.makeText(getContext(), ""+item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        return rootView;
    }


}