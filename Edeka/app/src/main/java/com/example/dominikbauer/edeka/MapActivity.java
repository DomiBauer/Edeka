package com.example.dominikbauer.edeka;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MapActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        int location;
        Intent intent = getIntent();
        location = intent.getIntExtra("Location", 0);
        System.out.println(location);
    }
}
