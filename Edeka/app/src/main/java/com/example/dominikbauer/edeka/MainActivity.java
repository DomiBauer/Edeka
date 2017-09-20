package com.example.dominikbauer.edeka;


import android.content.Intent;
import android.graphics.Paint;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final String TAG = MainActivity.class.getSimpleName();
    private DatabaseReference mDatabase;


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

        //getSupportActionBar().hide();


        mDatabase = FirebaseDatabase.getInstance().getReference();
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

        getDataFromDatabase();
        populateProductList();
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
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.map);
        if(fragment != null)
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
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

    private void getDataFromDatabase() {
                mDatabase.child("Products").child("1").child("Productname").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String value = dataSnapshot.getValue(String.class);
                        Toast.makeText(getApplicationContext(),
                                value,
                                Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    private void populateProductList () {
        for (int i = 0; i < 20; i++) {
            LinearLayout myListView = (LinearLayout) findViewById(R.id.discount_product_list);
            View test = LayoutInflater.from(this).inflate(R.layout.discount_element, null);

            ImageView productImage = (ImageView) test.findViewById(R.id.product_image);
            if(i == 1) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    productImage.setImageDrawable(getResources().getDrawable(R.drawable.produkt_tafeltrauben_sultana_image, getApplicationContext().getTheme()));
                } else {
                    productImage.setImageDrawable(getResources().getDrawable(R.drawable.produkt_tafeltrauben_sultana_image));
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    productImage.setImageDrawable(getResources().getDrawable(R.drawable.test, getApplicationContext().getTheme()));
                } else {
                    productImage.setImageDrawable(getResources().getDrawable(R.drawable.test));
                }
            }

            TextView productHeadline = (TextView) test.findViewById(R.id.product_headline);
            productHeadline.setText("Product " + i);

            TextView productDescription = (TextView) test.findViewById(R.id.product_description);
            productDescription.setText("Productdescription " + i);

            TextView oldPriceProduct = (TextView) test.findViewById(R.id.product_old_price);
            String oldPriceProductString = Double.toString(3.99);
            oldPriceProduct.setText(oldPriceProductString + "€");
            oldPriceProduct.setPaintFlags(oldPriceProduct.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            TextView priceProduct = (TextView) test.findViewById(R.id.product_price);
            String priceProductString = Double.toString(2.99);
            priceProduct.setText(priceProductString  + "€");

            /*TextView priceSecondDigit = (TextView) test.findViewById(R.id.price_second_digit);
            String strI2 = Integer.toString(i+3);
            productHeadline.setText("." + strI2);*/

            final Button addToShoppingList = (Button) test.findViewById(R.id.discount_add_to_shopping_list);
            addToShoppingList.setTag("Button"+i);

            final int index = i;

            addToShoppingList.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Log.i("TAG", "index :" + index);

                    Toast.makeText(getApplicationContext(),
                            "Clicked Button Index :" + addToShoppingList.getTag(),
                            Toast.LENGTH_LONG).show();

                }
            });

            myListView.addView(test);
        }
    }
}
