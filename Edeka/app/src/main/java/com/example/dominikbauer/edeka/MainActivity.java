package com.example.dominikbauer.edeka;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

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
        ViewGroup inclusionViewGroup = (ViewGroup)findViewById(R.id.content);
        View shoppingListContent = LayoutInflater.from(this).inflate(R.layout.content_shopping_list, null);
        inclusionViewGroup.addView(shoppingListContent);
    }

    public void productSearch () {
        ViewGroup inclusionViewGroup = (ViewGroup)findViewById(R.id.content);
        View shoppingListContent = LayoutInflater.from(this).inflate(R.layout.content_product_search, null);
        inclusionViewGroup.addView(shoppingListContent);
    }

    public void openDiscount () {
        ViewGroup inclusionViewGroup = (ViewGroup)findViewById(R.id.content);
        View shoppingListContent = LayoutInflater.from(this).inflate(R.layout.content_discount, null);
        inclusionViewGroup.addView(shoppingListContent);
    }

    public void openAbout () {
        ViewGroup inclusionViewGroup = (ViewGroup)findViewById(R.id.content);
        View aboutContent = LayoutInflater.from(this).inflate(R.layout.content_about, null);
        inclusionViewGroup.addView(aboutContent);

    }

}
