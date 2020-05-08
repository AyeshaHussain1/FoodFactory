package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import fragment.About_Us;
import fragment.Dashboard;
import fragment.Profile;
import fragment.Recipies;
import fragment.Review;

public class Home_Page extends AppCompatActivity {
    private DrawerLayout objectDrawerLayout;
    private FirebaseAuth mAuth;
    private FirebaseAuth objectFirebaseAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseUser firebaseAuth;
    private Toolbar objectToolbar;
    private TextView name;
    private NavigationView objectNavigationView;
    private ImageView profile;
    private EditText Email;
    private EditText Pwd;
    About_Us about_us;
    Recipies recipies;
    Review review;
   Dashboard dash_board;
    Profile myprofile;

    public static Context contextOfApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__page);
        connection();
        contextOfApplication = getApplicationContext();
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

    private void connection() {
        try {
            mAuth = FirebaseAuth.getInstance();
            objectDrawerLayout = findViewById(R.id.drawer);
            objectToolbar = findViewById(R.id.ToolBar);
            objectNavigationView = findViewById(R.id.Nav);
            View header = objectNavigationView.getHeaderView(0);
            profile = header.findViewById(R.id.profile);
            Email = findViewById(R.id.Email_login);
            Pwd = findViewById(R.id.password_login);
            name = header.findViewById(R.id.name);
            review = new Review();
            myprofile = new Profile();
            dash_board=new Dashboard();
            recipies = new Recipies();
            about_us = new About_Us();
            //////// //get name of user in header
            firebaseAuth = FirebaseAuth.getInstance().getCurrentUser();
            if (firebaseAuth != null)
            {

                name.setText(firebaseAuth.getDisplayName());
            }
            mAuth = FirebaseAuth.getInstance();
            GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
            if (signInAccount != null) {
                name.setText(signInAccount.getDisplayName());
            }
            //////////////
            profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeFragment(myprofile);
                    Toast.makeText(Home_Page.this, "My Profile", Toast.LENGTH_SHORT).show();
                    closeMyDrawer();
                }
            });
            changeFragment(dash_board);
            objectNavigationView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.Review:
                                    changeFragment(review);
                                    Toast.makeText(Home_Page.this, "Review page", Toast.LENGTH_SHORT).show();
                                    closeMyDrawer();
                                    return true;
                                case R.id.Recipies:
                                    changeFragment(recipies);
                                    Toast.makeText(Home_Page.this, "Recipies", Toast.LENGTH_SHORT).show();
                                    closeMyDrawer();
                                    return true;
                                case R.id.about:
                                    changeFragment(about_us);
                                    Toast.makeText(Home_Page.this, "About us", Toast.LENGTH_SHORT).show();
                                    closeMyDrawer();
                                    return true;
                                case R.id.Dashboard:
                                    changeFragment(dash_board);
                                    Toast.makeText(Home_Page.this, "Home screen", Toast.LENGTH_SHORT).show();
                                    closeMyDrawer();
                                    return true;
                                case R.id.profile:
                                    changeFragment(myprofile);
                                    Toast.makeText(Home_Page.this, "My Profile", Toast.LENGTH_SHORT).show();
                                    closeMyDrawer();
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    }
            );
            setUpHamBurgerIcon();
        } catch (Exception e) {
            Toast.makeText(this, "connection:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpHamBurgerIcon() {
        try {
            ActionBarDrawerToggle objectActionBarDrawerToggle = new ActionBarDrawerToggle(
                    this,
                    objectDrawerLayout,
                    objectToolbar,
                    R.string.open
                    , R.string.close
            );

            objectActionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.
                    color.white));

            objectActionBarDrawerToggle.syncState();
        } catch (Exception e) {
            Toast.makeText(this, "setUpHamBurgerIcon:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void closeMyDrawer() {
        try {
            objectDrawerLayout.closeDrawer(GravityCompat.START);
        } catch (Exception e) {
            Toast.makeText(this, "closeMyDrawer:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static Context getContextOfApplication() {
        return contextOfApplication;
    }

    public void Logout(MenuItem nav_logout) {
        FirebaseAuth.getInstance().signOut();
        try {
            GoogleSignIn.getClient(getApplicationContext(),
                    new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()

            ).signOut();
        } catch (Exception ex) {
            Toast.makeText(Home_Page.this, "Logging Out Error", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(Home_Page.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), Login_Page.class);
        startActivity(intent);
    }


    public void profile(MenuItem profile) {
        changeFragment(myprofile);
        Toast.makeText(Home_Page.this, "Opening Profile", Toast.LENGTH_SHORT).show();
        closeMyDrawer();

    }


}

