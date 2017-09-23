package com.example.dominikbauer.edeka;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class ShoppingActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        Bundle bundle = getIntent().getExtras();
        ArrayList<Product> productList =  bundle.getParcelableArrayList("ShoppingList");
    }
}
