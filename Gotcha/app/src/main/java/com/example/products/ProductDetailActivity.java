package com.example.products;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.chatfunction.ChatMainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.models.Product;
import com.orders.OrderMAIN;
import com.tapadoo.alerter.Alerter;

public class ProductDetailActivity extends AppCompatActivity {
private TextView user_name;
private ImageView profile_pic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);

        ImageView profilePic = findViewById(R.id.profile_pic);
        CircleTransform.applyCircularTransform(profilePic);
        user_name = findViewById(R.id.user_name);
        profile_pic = findViewById(R.id.profile_pic);
        Button backButton = findViewById(R.id.back_home);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailActivity.this, OrderMAIN.class);
                startActivity(intent);
                finish();
            }
        });

        Button orders = findViewById(R.id.orders);
        orders.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDetailActivity.this, OrderMAIN.class);
                startActivity(intent);
            }
        });

        Button home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDetailActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button profile = findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDetailActivity.this, ProfilePageActivity.class);
                startActivity(intent);
            }
        });

        Button chat = findViewById(R.id.chat);
        chat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDetailActivity.this, ChatMainActivity.class);
                startActivity(intent);
                finish();
            }
        });




// Get a reference to your Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String profile_pic = intent.getStringExtra("profilePic");
        String clothesId = getIntent().getStringExtra("clothes_id");
        DatabaseReference productrefs = FirebaseDatabase.getInstance().getReference().child("product_details").child(clothesId);
// Add a ValueEventListener to the reference
        productrefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get the data from the snapshot
                String clothesName = dataSnapshot.child("clothes_name").getValue(String.class);
                String clothesBrand = dataSnapshot.child("clothes_brand").getValue(String.class);
                String clothesPrice = dataSnapshot.child("clothes_price").getValue(String.class);
                String clothesSize = dataSnapshot.child("clothes_size").getValue(String.class);
                String clothesType = dataSnapshot.child("clothes_type").getValue(String.class);
                String clothesCondition = dataSnapshot.child("clothes_condition").getValue(String.class);
                String descriptionContent = dataSnapshot.child("description_content").getValue(String.class);
                String clothesImage = dataSnapshot.child("clothes_image").getValue(String.class);
                //String formattedPrice = "$" + clothesPrice + ".00";
                // Update the UI with the data
                ImageView ivClothesImage = findViewById(R.id.clothes_image);
                Glide.with(ProductDetailActivity.this)
                        .load(clothesImage)
                        .into(ivClothesImage);

                TextView tvClothesName = findViewById(R.id.clothes_name);
                tvClothesName.setText(clothesName);

                TextView tvClothesBrand = findViewById(R.id.clothes_brand);
                tvClothesBrand.setText(clothesBrand);

                TextView tvClothesPrice = findViewById(R.id.clothes_price);
                tvClothesPrice.setText(clothesPrice);

                TextView tvClothesSize = findViewById(R.id.clothes_size);
                tvClothesSize.setText(clothesSize);

                TextView tvClothesType = findViewById(R.id.clothes_type);
                tvClothesType.setText(clothesType);

                TextView tvClothesCondition = findViewById(R.id.clothes_condition);
                tvClothesCondition.setText(clothesCondition);

                TextView tvDescriptionContent = findViewById(R.id.description_content);
                tvDescriptionContent.setText(descriptionContent);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                showAlert("ERROR");
            }
        });

        ImageButton notifbutton = findViewById(R.id.notif);
        notifbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                Alerter alerter = Alerter.create(ProductDetailActivity.this)
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


        final TextView tvClothesName = findViewById(R.id.clothes_name);
        final TextView tvClothesBrand = findViewById(R.id.clothes_brand);
        final TextView tvClothesPrice = findViewById(R.id.clothes_price);
        final TextView tvClothesSize = findViewById(R.id.clothes_size);
        final TextView tvClothesCondition = findViewById(R.id.clothes_condition);
        final TextView tvClothesType = findViewById(R.id.clothes_type);

// Add a ViewTreeObserver to the TextView
        tvClothesType.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Check if the text has become 2 lines
                if (tvClothesType.getLineCount() > 1) {
                    // Set a smaller text size
                    tvClothesType.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                } else {
                    // Set the original text size
                    tvClothesType.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                }
                // Remove the ViewTreeObserver after it has been triggered
                tvClothesType.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        tvClothesName.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (tvClothesName.getLineCount() > 1) {
                    tvClothesName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                } else {
                    tvClothesName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                }
                tvClothesName.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        tvClothesBrand.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (tvClothesBrand.getLineCount() > 1) {
                    tvClothesBrand.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                } else {
                    tvClothesBrand.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                }
                tvClothesBrand.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        tvClothesPrice.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (tvClothesPrice.getLineCount() > 1) {
                    tvClothesPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                } else {
                    tvClothesPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                }
                tvClothesPrice.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        tvClothesSize.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (tvClothesSize.getLineCount() > 1) {
                    tvClothesSize.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                } else {
                    tvClothesSize.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                }
                tvClothesSize.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        tvClothesCondition.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (tvClothesCondition.getLineCount() > 1) {
                    tvClothesCondition.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                } else {
                    tvClothesCondition.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                }
                tvClothesCondition.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
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