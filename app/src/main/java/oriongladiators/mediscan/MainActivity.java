package oriongladiators.mediscan;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionButton fab;

    ImageButton fab_1, fab_2, fab_3, fab_4, fab_5, fab_6;

    Animation fab_open, fab_close, rotate_forward, rotate_backward;

    boolean isFabOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CardView cardViewOne = (CardView) findViewById(R.id.card_view1);
        CardView cardViewFour = (CardView) findViewById(R.id.card_view4);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotation_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotation_backward);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab_1 = (ImageButton) findViewById(R.id.fab1);
        fab_2 = (ImageButton) findViewById(R.id.fab2);
        fab_3 = (ImageButton) findViewById(R.id.fab3);
        fab_4 = (ImageButton) findViewById(R.id.fab4);
        fab_5 = (ImageButton) findViewById(R.id.fab5);
        fab_6 = (ImageButton) findViewById(R.id.fab6);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Choose your action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                animateFAB();
            }
        });


        cardViewOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ScannerActivity.class);
                startActivity(i);
            }
        });

        cardViewFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ReportToAuthoritiesActivity.class);
                startActivity(i);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void animateFAB() {

        if (isFabOpen) {

            fab.startAnimation(rotate_backward);
            fab_1.startAnimation(fab_close);
            fab_2.startAnimation(fab_close);
            fab_3.startAnimation(fab_close);
            fab_4.startAnimation(fab_close);
            fab_5.startAnimation(fab_close);
            fab_6.startAnimation(fab_close);
            fab_1.setClickable(false);
            fab_2.setClickable(false);
            fab_3.setClickable(false);
            fab_4.setClickable(false);
            fab_5.setClickable(false);
            fab_6.setClickable(false);
            isFabOpen = false;

        } else {

            fab.startAnimation(rotate_forward);
            fab_1.startAnimation(fab_open);
            fab_2.startAnimation(fab_open);
            fab_3.startAnimation(fab_open);
            fab_4.startAnimation(fab_open);
            fab_5.startAnimation(fab_open);
            fab_6.startAnimation(fab_open);
            fab_1.setClickable(true);
            fab_2.setClickable(true);
            fab_3.setClickable(true);
            fab_4.setClickable(true);
            fab_5.setClickable(true);
            fab_6.setClickable(true);
            isFabOpen = true;

        }

    }


}
