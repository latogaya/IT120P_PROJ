package com.example.products;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.databases.ProductDBHelper;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView profilePic = findViewById(R.id.profile_pic);
        CircleTransform.applyCircularTransform(profilePic);

        ImageButton detailsButton = findViewById(R.id.details1);
        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                startActivity(intent);
            }
        });

        Button profileButton = findViewById(R.id.profile);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SellerHomeActivity.class);
                startActivity(intent);
            }
        });

        ImageView myImageView = findViewById(R.id.card_img1);
        LinearLayout myLinearLayout = findViewById(R.id.product_detail);

        myImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myLinearLayout.getVisibility() == View.VISIBLE) {
                    myLinearLayout.setVisibility(View.INVISIBLE);
                } else {
                    myLinearLayout.setVisibility(View.VISIBLE);
                    TextView clothesName = findViewById(R.id.clothes_name1);
                    TextView clothesBrand = findViewById(R.id.clothes_brand);
                    TextView clothesPrice = findViewById(R.id.clothes_price);
                    TextView clothesSize = findViewById(R.id.clothes_size);
                    TextView clothesCondition = findViewById(R.id.clothes_condition);

                    clothesName.setVisibility(View.VISIBLE);
                    clothesBrand.setVisibility(View.VISIBLE);
                    clothesPrice.setVisibility(View.VISIBLE);
                    clothesSize.setVisibility(View.VISIBLE);
                    clothesCondition.setVisibility(View.VISIBLE);
                }
            }
        });

    }
}