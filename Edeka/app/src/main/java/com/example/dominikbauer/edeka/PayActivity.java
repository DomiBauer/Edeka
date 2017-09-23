package com.example.dominikbauer.edeka;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PayActivity extends FragmentActivity {

    Product [] productArray;
    double shoppingCartValue;
    int numberOfProductsInShoppingCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        Bundle bundle = getIntent().getExtras();
        ArrayList<Product> productList =  bundle.getParcelableArrayList("ShoppingList");
        shoppingCartValue = bundle.getDouble("Price");
        numberOfProductsInShoppingCart = bundle.getInt("Amount");

        productArray = new Product[productList.size()];
        productList.toArray(productArray);

        addProductsToView();
        setCurrentShoppingCartValue();
    }

    private void setView () {
        ViewGroup inclusionViewGroup = (ViewGroup)findViewById(R.id.content);
        View shoppingListContent = LayoutInflater.from(this).inflate(R.layout.content_discount, null);
        inclusionViewGroup.addView(shoppingListContent);
        addProductsToView();
    }

    private void addProductsToView () {
        for (int i = 0; i < productArray.length; i++) {
            final View myView;
            final int index = i;
            LinearLayout myListView = (LinearLayout) findViewById(R.id.to_pay_products);
            myView = LayoutInflater.from(this).inflate(R.layout.pay_element, null);

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

            TextView productName = (TextView) myView.findViewById(R.id.product_name);
            productName.setText(productArray[i].productName);

            TextView productPrice = (TextView) myView.findViewById(R.id.product_price);

            if (productArray[i].discountPrice == 0.00) {
                productPrice.setText(String.valueOf(productArray[index].originalPrice) + "€");
            } else {
                productPrice.setText(String.valueOf(productArray[index].discountPrice) + "€");
            }

            Button goToPayButton = (Button) findViewById(R.id.pay_button);
            goToPayButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(PayActivity.this, QRActivity.class);
                    startActivity(intent);
                }
            });

            myListView.addView(myView);
        }
    }

    private void setCurrentShoppingCartValue(){
        TextView TextShoppingCartValue = (TextView) findViewById(R.id.current_shopping_cart_value);
        DecimalFormat df = new DecimalFormat("####0.00");
        if (shoppingCartValue == 0.00) {
            TextShoppingCartValue.setText("Summe: (" + numberOfProductsInShoppingCart + " Artikel: 0.00 €)");
        } else {
            TextShoppingCartValue.setText("Summe: (" + numberOfProductsInShoppingCart + " Artikel: " + df.format(shoppingCartValue) + "€)");
        }
    }
}
