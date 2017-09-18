package com.example.dominikbauer.edeka;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_einkaufszettel:
                    mTextMessage.setText(R.string.title_einkaufszettel);
                    return true;
                case R.id.navigation_produktsuche:
                    mTextMessage.setText(R.string.title_produktsuche);
                    return true;
                case R.id.navigation_angebote:
                    mTextMessage.setText(R.string.title_angebote);
                    return true;
                case R.id.navigation_ueber_uns:
                    mTextMessage.setText(R.string.title_ueber_uns);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
