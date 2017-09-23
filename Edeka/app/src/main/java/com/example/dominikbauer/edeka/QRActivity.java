package com.example.dominikbauer.edeka;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class QRActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        Button showProductPosition = (Button) findViewById(R.id.finish_button);
        showProductPosition.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(QRActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }
}
