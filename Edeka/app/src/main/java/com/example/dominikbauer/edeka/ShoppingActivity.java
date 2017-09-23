package com.example.dominikbauer.edeka;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ShoppingActivity extends FragmentActivity {

    Product [] productArray;
    double shoppingCartValue;
    int numberOfProductsInShoppingCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        Bundle bundle = getIntent().getExtras();
        ArrayList<Product> productList =  bundle.getParcelableArrayList("ShoppingList");

        productArray = new Product[productList.size()];
        productList.toArray(productArray);

        for (int i = 0; i < productArray.length; i++) {
            drawShoppingListItems (i);
        }

        shoppingCartValue = 0.00;
        numberOfProductsInShoppingCart = 0;
    }

    public void drawShoppingListItems (int i) {
        final View myView;
        final int index = i;

        LinearLayout myListView = (LinearLayout) findViewById(R.id.started_shopping_list);
        myView = LayoutInflater.from(this).inflate(R.layout.shopping_element, null);

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

        final Button addToShoppingCart = (Button) myView.findViewById(R.id.add_to_shoppingCart);
        addToShoppingCart.setTag(productArray[index].id);
        setButtonColor(addToShoppingCart, index);
        addToShoppingCart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Button addButton = myView.findViewWithTag(v.getTag());
                calculateShoppingCartValue (index);
                setCurrentShoppingCartValue();
                changeButtonColor(addButton, index);
                //openShoppingList();
            }
        });

        Button goToPayButton = (Button) findViewById(R.id.go_to_pay_button);
        goToPayButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ShoppingActivity.this, PayActivity.class);
                startActivity(intent);
            }
        });

        Button showProductPosition = (Button) myView.findViewById(R.id.show_position_of_product);
        showProductPosition.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ShoppingActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });


        //setUpMapCall(index, myView);

        myListView.addView(myView);
        setCurrentShoppingCartValue();
    }

    private void setUpMapCall (int i, View currentView) {
        final int index = i;
        final View myView = currentView;
        final Button showPositionOfProductButton = (Button) findViewById(R.id.show_position_of_product);
        showPositionOfProductButton.setTag(productArray[index].id);

        showPositionOfProductButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Button addButton = myView.findViewWithTag(v.getTag());
            }
        });
    }

    private void calculateShoppingCartValue (int index) {
        if (productArray[index].inShoppingCart == false) {
            if (productArray[index].discountPrice == 0.00) {
                shoppingCartValue += productArray[index].originalPrice;
            } else {
                shoppingCartValue += productArray[index].discountPrice;
            }
            numberOfProductsInShoppingCart ++;
        } else {
            if (productArray[index].discountPrice == 0.00) {
                shoppingCartValue -= productArray[index].originalPrice;
            } else {
                shoppingCartValue -= productArray[index].discountPrice;
            }
            numberOfProductsInShoppingCart --;
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

    private void setButtonColor (Button addToShoppingCart, int index) {
        if (productArray[index].inShoppingCart == true) {
            addToShoppingCart.setTextColor(getResources().getColor(R.color.remove_button_color));
            addToShoppingCart.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_remove_from_shopping_cart, 0, 0, 0);
            addToShoppingCart.setText("Entfernen");
        } else {
            addToShoppingCart.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            addToShoppingCart.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_to_shopping_cart, 0, 0, 0);
            addToShoppingCart.setText("In den Warenkorb");
            //productArray[index].inShoppingCart = false;
        }
    }

    private void changeButtonColor (Button addButton, int index) {
        if (productArray[index].inShoppingCart == false) {
            addButton.setTextColor(getResources().getColor(R.color.remove_button_color));
            addButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_remove_from_shopping_cart, 0, 0, 0);
            addButton.setText("Entfernen");
            productArray[index].inShoppingCart = true;
        } else {
            addButton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            addButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_to_shopping_cart, 0, 0, 0);
            addButton.setText("In den Warenkorb");
            productArray[index].inShoppingCart = false;
        }
    }


}
