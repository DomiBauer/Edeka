package com.example.dominikbauer.edeka;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_einkaufszettel:
                    openShoppingList();
                    return true;
                case R.id.navigation_produktsuche:
                    productSearch();
                    return true;
                case R.id.navigation_angebote:
                    openDiscount();
                    return true;
                case R.id.navigation_ueber_uns:
                    openAbout();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        openShoppingList();

        getSupportActionBar().hide();
    }

    public void openShoppingList () {
        clearContent ();
        ViewGroup inclusionViewGroup = (ViewGroup)findViewById(R.id.content);
        View shoppingListContent = LayoutInflater.from(this).inflate(R.layout.content_shopping_list, null);
        inclusionViewGroup.addView(shoppingListContent);
    }

    public void productSearch () {
        clearContent ();
        ViewGroup inclusionViewGroup = (ViewGroup)findViewById(R.id.content);
        View shoppingListContent = LayoutInflater.from(this).inflate(R.layout.content_product_search, null);
        inclusionViewGroup.addView(shoppingListContent);
    }

    public void openDiscount () {
        clearContent ();
        ViewGroup inclusionViewGroup = (ViewGroup)findViewById(R.id.content);
        View shoppingListContent = LayoutInflater.from(this).inflate(R.layout.content_discount, null);
        inclusionViewGroup.addView(shoppingListContent);
    }

    public void openAbout () {
        clearContent ();
        ViewGroup inclusionViewGroup = (ViewGroup)findViewById(R.id.content);
        View aboutContent = LayoutInflater.from(this).inflate(R.layout.content_about, null);
        inclusionViewGroup.addView(aboutContent);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void clearContent () {
        FrameLayout content = (FrameLayout) findViewById(R.id.content);
        content.removeAllViews();
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng storeLocation = new LatLng(48.393171, 12.605773);
        mMap.addMarker(new MarkerOptions().position(storeLocation).title("EDEKA Steinberger"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(storeLocation, 15));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        setUpMapScrolling();
    }

    private void setUpMapScrolling () {
        final ScrollView scroll = (ScrollView) findViewById(R.id.scrollView);
        ImageView transparent = (ImageView)findViewById(R.id.imagetrans);

        transparent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        scroll.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        scroll.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        scroll.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });
    }
}
