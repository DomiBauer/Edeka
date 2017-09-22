package com.example.dominikbauer.edeka;


import android.content.Context;
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

import com.example.dominikbauer.edeka.Product;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;


    public Product [] productArray = new Product[5];
    public Product product0;
    public Product product1;
    public Product product2;
    public Product product3;
    public Product product4;
    public Product product5;
    public Product product6;
    public Product product7;
    public Product product8;
    public Product product9;
    public Product product10;
    public Product product11;
    public Product product12;
    public Product product13;
    public Product product14;
    public Product product15;
    public Product product16;
    public Product product17;
    public Product product18;
    public Product product19;


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
        createProducts();

        //getSupportActionBar().hide();
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

        addProductToDiscountView ();
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

    public void createProducts () {
        product0 = new Product(0, "Hochland Käse-Ecken", "Inhalt: 200 g", 2.29, 0.00, "hochland_kaese_ecken", false, false);
        productArray [0] = product0;
        product1 = new Product(1, "EDEKA Bio Roggen Vollkornbrot", "Inhalt: 500 g", 1.39, 0.00, "edeka_bio_roggen_vollkornbrot", false, false);
        productArray [1] = product1;
        product2 = new Product(2, "Nutella Nuss-Nougat-Creme groß", "Inhalt: 750 g", 3.89, 0.00, "nutella_nuss_nougat_creme_gross", false, false);
        productArray [2] = product2;
        product3 = new Product(3, "Knorr Fix für Spaghetti Bolognese", "Inhalt: 42 g", 0.89, 0.00, "knorr_fix_fuer_spaghetti_bolognese", false, false);
        productArray [3] = product3;
        product4 = new Product(4, "Knorr Knoblauch Grillsauce", "Inhalt: 250 ml", 1.29, 0.00, "knorr_knoblauch_grillsauce", false, false);
        productArray [4] = product4;
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

    private void addProductToDiscountView () {

        for (int i = 0; i < productArray.length; i++) {
            LinearLayout myListView = (LinearLayout) findViewById(R.id.discount_product_list);
            View myView = LayoutInflater.from(this).inflate(R.layout.discount_element, null);

            String imageURL = productArray[i].imgURL;
            ImageView productImage = (ImageView) myView.findViewById(R.id.product_image);
            Context context = productImage.getContext();
            int id = context.getResources().getIdentifier(imageURL, "drawable", context.getPackageName());


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                productImage.setImageDrawable(getResources().getDrawable(id, getApplicationContext().getTheme()));
            } else {
                productImage.setImageDrawable(getResources().getDrawable(id));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                productImage.setImageDrawable(getResources().getDrawable(id, getApplicationContext().getTheme()));
            } else {
                productImage.setImageDrawable(getResources().getDrawable(id));
            }

            TextView productHeadline = (TextView) myView.findViewById(R.id.product_headline);
            productHeadline.setText(productArray[i].productName);

            TextView productDescription = (TextView) myView.findViewById(R.id.product_description);
            productDescription.setText(productArray[i].productDescription);

            TextView priceProduct = (TextView) myView.findViewById(R.id.product_old_price);
            priceProduct.setText(String.valueOf(productArray[i].originalPrice + "€"));
            priceProduct.setPaintFlags(priceProduct.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            TextView priceDiscountProduct = (TextView) myView.findViewById(R.id.product_price);
            priceDiscountProduct.setText(String.valueOf(productArray[i].discountPrice) + "€");

            final Button addToShoppingList = (Button) myView.findViewById(R.id.discount_add_to_shopping_list);
            addToShoppingList.setTag(productArray[i].id);

            myListView.addView(myView);
        }

    }

    private void populateProductList () {
        /*for (int i = 0; i < 5; i++) {
            LinearLayout myListView = (LinearLayout) findViewById(R.id.discount_product_list);
            View myView = LayoutInflater.from(this).inflate(R.layout.discount_element, null);

            ImageView productImage = (ImageView) myView.findViewById(R.id.product_image);
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

            /*TextView productHeadline = (TextView) myView.findViewById(R.id.product_headline);
            productHeadline.setText("Product " + i);*/

            /*TextView productDescription = (TextView) myView.findViewById(R.id.product_description);
            productDescription.setText("Productdescription " + i);

            TextView oldPriceProduct = (TextView) myView.findViewById(R.id.product_old_price);
            String oldPriceProductString = Double.toString(3.99);
            oldPriceProduct.setText(oldPriceProductString + "€");
            oldPriceProduct.setPaintFlags(oldPriceProduct.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            /*TextView priceProduct = (TextView) myView.findViewById(R.id.product_price);
            String priceProductString = Double.toString(2.99);
            priceProduct.setText(priceProductString  + "€");

            final Button addToShoppingList = (Button) myView.findViewById(R.id.discount_add_to_shopping_list);
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

            myListView.addView(myView);*/
        }
}
