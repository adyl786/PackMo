package com.android.testappppp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    View Header;
    String Name, Email;
    SharedPreferences sharedPreferences;
    TextView Tname, Temail;
    final String NAME = "Name", PHONE = "Phone", PASSWORD = "Password", EMAIL = "Email", GENDER = "Gender";
    final String PREFERENCE = "My Preference", LOGIN_STATUS = "LoginStatus";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.old_activity_home);

        NavigationView navigationView;


        navigationView = findViewById(R.id.nav_view);


        Header = navigationView.getHeaderView(0);
        Tname = Header.findViewById(R.id.UserId);
        Temail = Header.findViewById(R.id.emailId);
        sharedPreferences = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        Name = sharedPreferences.getString(NAME, "default");
        Email = sharedPreferences.getString(EMAIL, "default");
        Tname.setText(Name);
        Temail.setText(Email);
        Log.d("mytag", Name + Email);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        TextView EmailId = header.findViewById(R.id.emailId);
        EmailId.setText(Email);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();

                break;
            case R.id.nav_oldBooking:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Old_Booking()).commit();
                break;

            case R.id.nav_upcoming:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Upcoming()).commit();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    // BACK PRESS METHOD
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // SELECT OPTION FROM MENU TO DO FUNCTION

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString(LOGIN_STATUS,"LogOut");
                editor.putString(NAME,"");
                editor.putString(EMAIL,"");
                editor.putString(PASSWORD,"");
                editor.putString(PHONE,"");
                editor.putString(GENDER,"");
                editor.commit();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();


        }
        return super.onOptionsItemSelected(item);

    }

    //SHOW OPTION MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

}
