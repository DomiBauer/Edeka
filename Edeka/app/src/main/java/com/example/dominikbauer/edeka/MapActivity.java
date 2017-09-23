package com.example.dominikbauer.edeka;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MapActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        int location;
        Intent intent = getIntent();
        location = intent.getIntExtra("Location", 0);
        System.out.println(location);

        setMapRessource(location);
    }

    public void setMapRessource (int location) {
        ImageView mapImage = (ImageView) findViewById(R.id.map_image);
        Context context = mapImage.getContext();

        String imageURL = "a" + Integer.toString(location);
        int id = context.getResources().getIdentifier(imageURL, "mipmap", context.getPackageName());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mapImage.setImageDrawable(getResources().getDrawable(id, getApplicationContext().getTheme()));
        } else {
            mapImage.setImageDrawable(getResources().getDrawable(id));
        }
    }
}
