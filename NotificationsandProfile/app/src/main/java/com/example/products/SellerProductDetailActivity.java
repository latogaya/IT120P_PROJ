package com.example.products;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class SellerProductDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_product_detail);
        ImageView profilePic1 = findViewById(R.id.profile_pic1);
        CircleTransform.applyCircularTransform(profilePic1);
        ImageView profilePic2 = findViewById(R.id.profile_pic2);
        CircleTransform.applyCircularTransform(profilePic2);
        Button doneButton = findViewById(R.id.done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerProductDetailActivity.this, SellerHomeActivity.class);
                startActivity(intent);
            }
        });
    }
}
