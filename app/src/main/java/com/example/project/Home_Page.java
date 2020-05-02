package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import fragment.Dashboard;
import fragment.Restaurant;

public class Home_Page extends AppCompatActivity {
    private DrawerLayout objectDrawerLayout;
    private Toolbar objectToolbar;
    private NavigationView objectNavigationView;
    private ImageView profile;
    Restaurant restaurant;
    Dashboard dashboard;
    public static Context contextOfApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__page);
        connection();
        contextOfApplication = getApplicationContext();
    }

    private void connection() {
        try {
            objectDrawerLayout = findViewById(R.id.drawer);
            objectToolbar = findViewById(R.id.ToolBar);
            objectNavigationView = findViewById(R.id.Nav);
            View header = objectNavigationView.getHeaderView(0);
            profile = header.findViewById(R.id.profile);
            restaurant = new Restaurant();
            dashboard = new Dashboard();
            objectNavigationView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.Restaurant:
                                    changeFragment(restaurant);
                                    Toast.makeText(Home_Page.this, "Restaurants", Toast.LENGTH_SHORT).show();
                                    return true;
                            }

                            return false;
                        }
                    }
            );
        } catch (Exception e) {
            Toast.makeText(this, "connection:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void changeFragment(Fragment object) {
        try {
            FragmentTransaction objectFragmentTransaction = getSupportFragmentManager().beginTransaction();
            objectFragmentTransaction.replace(R.id.F_layout, object);
            objectFragmentTransaction.commit();
        } catch (Exception ex) {
            Toast.makeText(this, "ChangeFragment: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static Context getContextOfApplication() {
        return contextOfApplication;
    }
}



//
//    private void setUpHamBurgerIcon() {
//        try {
//            ActionBarDrawerToggle objectActionBarDrawerToggle = new ActionBarDrawerToggle(
//                    this,
//                    objectDrawerLayout,
//                    objectToolbar,
//                    R.string.open
//                    , R.string.close
//            );
//
//            objectActionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.
//                    color.white));
//
//            objectActionBarDrawerToggle.syncState();
//        } catch (Exception e) {
//            Toast.makeText(this, "setUpHamBurgerIcon:" + e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }
//    private void closeMyDrawer() {
//        try {
//            objectDrawerLayout.closeDrawer(GravityCompat.START);
//        } catch (Exception e) {
//            Toast.makeText(this, "closeMyDrawer:" + e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }
