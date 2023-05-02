package com.orders;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.chatfunction.ChatMainActivity;
import com.example.products.MainActivity;
import com.example.products.ProductDetailActivity;
import com.example.products.ProfilePageActivity;
import com.example.products.R;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;
import java.util.List;

public class OrderMAIN extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD_PRODUCT = 1;

    private ListView mProductsListView;
    private List<Orders> mProducts;
    private FeedbackAdapter mFeedbackAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        mProductsListView = findViewById(R.id.orderListView);
        mProducts = new ArrayList<>();
        mFeedbackAdapter = new FeedbackAdapter(this, R.layout.orders_recycler_view, mProducts);
        mProductsListView.setAdapter(mFeedbackAdapter);

        // Create some dummy products
        Orders product1 = new Orders("DBTK OG", "Lex Ogaya");

        // Add the products to the list
        mProducts.add(product1);

        // Notify the adapter that the data set has changed
        mFeedbackAdapter.notifyDataSetChanged();


        // Set an item click listener on the ListView to handle item selection
        mProductsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Set the item as checked in the ListView
                mProductsListView.setItemChecked(position, true);
            }
        });

        // Find the transaction button in the layout
        Button moreButton = findViewById(R.id.moreButton);

        // Set a click listener on the transaction button
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the selected item position in the ListView
                int position = mProductsListView.getCheckedItemPosition();
                showAlert("Please click one of your History");
                if (position != ListView.INVALID_POSITION) {
                    // Get the selected product from the List
                    Orders selectedProduct = mProducts.get(position);
                    // Start the TransactionDetails activity with the selected product
                    Intent intent = new Intent(OrderMAIN.this, ProductDetailActivity.class);
                    intent.putExtra("clothes_id", "1");
                    startActivity(intent);
                }
            }
        });

        // Find the rate button in the layout
        Button rateButton = findViewById(R.id.rateButton);

        // Set a click listener on the rate button
        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = mProductsListView.getCheckedItemPosition();
                showAlert("Please click one of your History");
                if (position != ListView.INVALID_POSITION) {
                    // Get the selected product from the List
                    Orders selectedProduct = mProducts.get(position);
                    // Start the TransactionDetails activity with the selected product
                    Intent intent = new Intent(OrderMAIN.this, OrderCRUD.class);
                    intent.putExtra("clothes_id", "1");
                    startActivity(intent);
                }
            }
        });

        Button home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(OrderMAIN.this, MainActivity.class);
                startActivity(newIntent);
                finish();
            }
        });

        Button profile = findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderMAIN.this, ProfilePageActivity.class);
                startActivity(intent);
            }
        });

        Button chat = findViewById(R.id.chat);
        chat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderMAIN.this, ChatMainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button orders = findViewById(R.id.orders);
        orders.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showAlert("You're already at your Order History");}
        });

        ImageButton notifButton = findViewById(R.id.notif);
        notifButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                Alerter alerter = Alerter.create(OrderMAIN.this)
                        .setText("No New Notifications")
                        .setBackgroundColorRes(R.color.black)
                        .setIcon(R.drawable.notif_yellowlogo)
                        .setIconColorFilter(0)
                        .setDuration(5000)
                        .setEnterAnimation(com.tapadoo.alerter.R.anim.alerter_slide_in_from_left)
                        .setExitAnimation(com.tapadoo.alerter.R.anim.alerter_slide_out_to_right)
                        .enableSwipeToDismiss()
                        .setTextAppearance(R.style.CustomAlerterTextAppearance)
                        .setTitleAppearance(R.style.CustomAlerterTextAppearance)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        });
                View alerterView = alerter.show();
            }});
    }

    private void showAlert(String text) {
        Alerter alerter = Alerter.create(this)
                .setTextAppearance(R.style.CustomAlerterTextAppearance)
                .setTitleAppearance(R.style.CustomAlerterTextAppearance)
                .setText(text)
                .setBackgroundColorRes(R.color.black)
                .setIcon(R.drawable.notif_yellowlogo)
                .setIconColorFilter(0)
                .setDuration(5000)
                .setEnterAnimation(com.tapadoo.alerter.R.anim.alerter_slide_in_from_left)
                .setExitAnimation(com.tapadoo.alerter.R.anim.alerter_slide_out_to_right)
                .enableSwipeToDismiss()
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
        alerter.show();
    }

}