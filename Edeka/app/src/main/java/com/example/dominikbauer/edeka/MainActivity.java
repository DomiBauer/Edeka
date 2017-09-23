package com.example.dominikbauer.edeka;


import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;


    public Product [] productArray = new Product[15];
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

        createProducts();
        openShoppingList();
        //getSupportActionBar().hide();
    }

    public void openShoppingList () {
        clearContent();
        ViewGroup inclusionViewGroup = (ViewGroup) findViewById(R.id.content);

        View shoppingListContent = LayoutInflater.from(this).inflate(R.layout.content_shopping_list, null);
        inclusionViewGroup.addView(shoppingListContent);

        if (checkIfShoppingListIsEmpty ()) {
            for (int i = 0; i < productArray.length; i++) {
                if (productArray[i].onShoppingList == true) {
                    drawShoppingList (i);
                }
            }
        } else {
            LinearLayout myListView = (LinearLayout) findViewById(R.id.shopping_list);
            View myView = LayoutInflater.from(this).inflate(R.layout.empty_shopping_list, null);
            Button startShoppingButton = (Button) findViewById(R.id.start_shopping_button);
            startShoppingButton.setVisibility(myView.GONE);
            myListView.addView(myView);
        }

        Button startShoppingButton = (Button) findViewById(R.id.start_shopping_button);
        startShoppingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ArrayList<Product> productList = new ArrayList<Product>();

                for (int i = 0; i < productArray.length; i++) {
                    if (productArray[i].onShoppingList == true) {
                        productList.add(productArray[i]);
                    }
                }

                Intent intent = new Intent(MainActivity.this, ShoppingActivity.class);
                intent.putExtra ("ShoppingList", productList);
                startActivity(intent);
            }
        });

    }

    public boolean checkIfShoppingListIsEmpty () {
        for (int i = 0; i < productArray.length; i++) {
            if (productArray[i].onShoppingList == true) {
                return true;
            }
        }
        return false;
    }

    public void drawShoppingList (int i) {
        final View myView;
        final int index = i;

        LinearLayout myListView = (LinearLayout) findViewById(R.id.shopping_list);
        myView = LayoutInflater.from(this).inflate(R.layout.product_element, null);

        String imageURL = productArray[index].imgURL;
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
        productDescription.setText(productArray[index].productDescription);

        if (productArray[i].discountPrice == 0.00) {
            TextView productPrice = (TextView) myView.findViewById(R.id.product_price);
            productPrice.setText(String.valueOf(productArray[index].originalPrice) + "€");

            TextView oldProductPrice = (TextView) myView.findViewById(R.id.product_price);
            myListView.removeView(oldProductPrice);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) productPrice.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);

            productPrice.setLayoutParams(params);

        } else {
            TextView oldProductPrice = (TextView) myView.findViewById(R.id.product_old_price);
            oldProductPrice.setText(String.valueOf(productArray[index].originalPrice + "€"));
            oldProductPrice.setPaintFlags(oldProductPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            TextView productPrice = (TextView) myView.findViewById(R.id.product_price);
            productPrice.setText(String.valueOf(productArray[index].discountPrice) + "€");
        }

        final Button addToShoppingList = (Button) myView.findViewById(R.id.add_to_shopping_list);
        addToShoppingList.setTag(productArray[index].id);
        setButtonColor(addToShoppingList, index);
        addToShoppingList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Button addButton = myView.findViewWithTag(v.getTag());
                changeButtonColor(addButton, index);
                //openShoppingList();
            }
        });

        myListView.addView(myView);
    }

    public void productSearch () {
        clearContent();
        shuffleArray ();
        setUpSearchViewGrid();
        drawProductSearch(productArray);

        EditText searchBar = (EditText) findViewById(R.id.search_bar);
        searchBar.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchBarInput = s.toString();
                LinearLayout ll = findViewById(R.id.product_search_list);
                ll.removeAllViews();

                List<Product> filteredList = new ArrayList<Product>();

                for (int i = 0; i < productArray.length; i++) {
                    if (productArray[i].productName.toLowerCase().contains(searchBarInput.toLowerCase())){
                        filteredList.add(productArray[i]);
                    }
                }
                Product [] newProductArray = new Product[filteredList.size()];
                filteredList.toArray(newProductArray);
                drawProductSearch(newProductArray);
            }
        });
    }

    public void setUpSearchViewGrid () {
        ViewGroup inclusionViewGroup = (ViewGroup) findViewById(R.id.content);
        View shoppingListContent = LayoutInflater.from(this).inflate(R.layout.content_product_search, null);
        inclusionViewGroup.addView(shoppingListContent);
    }

    public void drawProductSearch (Product[] Products) {
        for (int j = 0; j < Products.length; j++) {
            final int index = j;
            final View myView;
            LinearLayout myListView = (LinearLayout) findViewById(R.id.product_search_list);
            myView = LayoutInflater.from(this).inflate(R.layout.product_element, null);

            String imageURL = Products[index].imgURL;
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
            productHeadline.setText(Products[index].productName);

            TextView productDescription = (TextView) myView.findViewById(R.id.product_description);
            productDescription.setText(Products[index].productDescription);

            if (Products[index].discountPrice == 0.00) {
                TextView productPrice = (TextView) myView.findViewById(R.id.product_price);
                productPrice.setText(String.valueOf(Products[index].originalPrice) + "€");

                TextView oldProductPrice = (TextView) myView.findViewById(R.id.product_price);
                myListView.removeView(oldProductPrice);

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) productPrice.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);

                productPrice.setLayoutParams(params);

            } else {
                TextView oldProductPrice = (TextView) myView.findViewById(R.id.product_old_price);
                oldProductPrice.setText(String.valueOf(Products[index].originalPrice + "€"));
                oldProductPrice.setPaintFlags(oldProductPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                TextView productPrice = (TextView) myView.findViewById(R.id.product_price);
                productPrice.setText(String.valueOf(Products[index].discountPrice) + "€");
            }

            final Button addToShoppingList = (Button) myView.findViewById(R.id.add_to_shopping_list);
            addToShoppingList.setTag(getProductId(Products[index].productName));
            setButtonColor (addToShoppingList, getProductId(productHeadline.getText().toString()));
            addToShoppingList.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Button addButton = myView.findViewWithTag(v.getTag());
                    TextView productHeadline = (TextView) myView.findViewById(R.id.product_headline);
                    String productName = productHeadline.getText().toString();
                    changeButtonColor(addButton, getProductId(productName));
                }
            });
            myListView.addView(myView);
        }
    }

    public int getProductId (String ProductName) {
        for (int i = 0; i < productArray.length; i++) {
            if (productArray[i].productName.equals(ProductName)) {
                return i;
            }
        }
        return 1000;
    }

    public void shuffleArray (){
        List<Product> list = Arrays.asList(productArray);
        Collections.shuffle(list);
        list.toArray(productArray);
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
        //Discount Prodcuts
        product0 = new Product(0, "EDEKA Bananen", "1 kg", 2.29, 1.49, "edeka_bananen", true, false, 1, false);
        productArray [0] = product0;
        product1 = new Product(1, "Frische Feigen", "aus der Türkei, Kl. I, Stück", 0.69, 0.39, "frische_feigen", true, false, 1, false);
        productArray [1] = product1;
        product2 = new Product(2, "Berchtesgadener Land Joghurt-Drink", "je 400g Packung", 0.99, 0.77, "berchtesgadener_land_joghurt_drink", true, false, 1, false);
        productArray [2] = product2;
        product3 = new Product(3, "Tabaluga Bio-Ketchup", "78% Tomatenmark, 500ml Flasche", 1.89, 1.49, "tabaluga_bio_ketchup", true, false, 1, false);
        productArray [3] = product3;
        product4 = new Product(4, "Kühne Ungarische Gurken", "Abtropfgewicht 360g, je 670g Glas", 1.59, 0.99, "kuehne_ungarische_gurken", true, false, 1, false);
        productArray [4] = product4;
        product5 = new Product(5, "Rockstar Energy Drink", "mit Taurin und Koffein, +0.25 Pfand, je 0,5l Dose, (1l=1.98)", 1.19, 0.99, "rockstar_energy_drink", true, false, 1, false);
        productArray [5] = product5;
        product6 = new Product(6, "Löwenbräu Oktoberfestbier", "inkl. 3.10 Pfand, Träger 20x0,5l Flasche", 14.49, 13.99, "loewenbraeu_oktoberfestbier", true, false, 1, false);
        productArray [6] = product6;
        product7 = new Product(7, "Lorenz Crunchips - Paprika", "je 150-225g Beutel", 1.49, 0.99, "lorenz_crunchips_paprika", true, false, 1, false);
        productArray [7] = product7;
        product8 = new Product(8, "Teekanne Sanfte Kamille", "je 18/20er Packung", 1.59, 1.29, "teekanne_sanfte_kamille", true, false, 1, false);
        productArray [8] = product8;
        product9 = new Product(9, "Odol-med3 Zahncreme", "75ml Tube", 1.59, 0.89, "odol_med3_zahncreme", true, false, 1, false);
        productArray [9] = product9;


        //Usual Products
        product10 = new Product(10, "Hochland Käse-Ecken", "Inhalt: 200 g", 2.29, 0.00, "hochland_kaese_ecken", false, false, 1, false);
        productArray [10] = product10;
        product11 = new Product(11, "EDEKA Bio Roggen Vollkornbrot", "Inhalt: 500 g", 1.39, 0.00, "edeka_bio_roggen_vollkornbrot", false, false, 1, false);
        productArray [11] = product11;
        product12 = new Product(12, "Nutella Nuss-Nougat-Creme", "Inhalt: 750 g", 3.89, 0.00, "nutella_nuss_nougat_creme_gross", false, false, 1, false);
        productArray [12] = product12;
        product13 = new Product(13, "Knorr Fix für Spaghetti Bolognese", "Inhalt: 42 g", 0.89, 0.00, "knorr_fix_fuer_spaghetti_bolognese", false, false, 1, false);
        productArray [13] = product13;
        product14 = new Product(14, "Knorr Knoblauch Grillsauce", "Inhalt: 250 ml", 1.29, 0.00, "knorr_knoblauch_grillsauce", false, false, 1, false);
        productArray [14] = product14;
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
            if (productArray[i].discountPrice != 0.00) {
                final View myView;
                final int index = i;
                LinearLayout myListView = (LinearLayout) findViewById(R.id.discount_product_list);
                myView = LayoutInflater.from(this).inflate(R.layout.product_element, null);

                String imageURL = productArray[index].imgURL;
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
                productHeadline.setText(productArray[index].productName);

                TextView productDescription = (TextView) myView.findViewById(R.id.product_description);
                productDescription.setText(productArray[index].productDescription);

                TextView oldProductPrice = (TextView) myView.findViewById(R.id.product_old_price);
                oldProductPrice.setText(String.valueOf(productArray[index].originalPrice + "€"));
                oldProductPrice.setPaintFlags(oldProductPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                TextView productPrice = (TextView) myView.findViewById(R.id.product_price);
                productPrice.setText(String.valueOf(productArray[index].discountPrice) + "€");

                final Button addToShoppingList = (Button) myView.findViewById(R.id.add_to_shopping_list);
                addToShoppingList.setTag(productArray[index].id);
                setButtonColor (addToShoppingList, index);
                addToShoppingList.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Button addButton = myView.findViewWithTag(v.getTag());
                        changeButtonColor(addButton, index);
                    }
                });
                myListView.addView(myView);
            }
        }
    }

    private void setButtonColor (Button addToShoppingList, int index) {
        if (productArray[index].onShoppingList == true) {
            addToShoppingList.setBackgroundColor(getResources().getColor(R.color.remove_button_color));
            addToShoppingList.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_delete, 0, 0, 0);
            addToShoppingList.setText("Entfernen");
        } else {
            addToShoppingList.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            addToShoppingList.setCompoundDrawablesWithIntrinsicBounds(R.drawable.add_to_shopping_list_button, 0, 0, 0);
            addToShoppingList.setText("Auf Einkaufsliste");
            productArray[index].onShoppingList = false;
        }
    }

    private void changeButtonColor (Button addButton, int index) {
        if (productArray[index].onShoppingList == false) {
            addButton.setBackgroundColor(getResources().getColor(R.color.remove_button_color));
            addButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_delete, 0, 0, 0);
            addButton.setText("Entfernen");
            productArray[index].onShoppingList = true;
        } else {
            addButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            addButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.add_to_shopping_list_button, 0, 0, 0);
            addButton.setText("Auf Einkaufsliste");
            productArray[index].onShoppingList = false;
        }
    }
}
