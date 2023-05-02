package com.orders;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.products.R;

public class ProductDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);

        Button doneButton = findViewById(R.id.back_home);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent to return to the InventoryMain activity
                Intent intent = new Intent(ProductDetails.this, OrderMAIN.class);
                startActivity(intent);

                // Finish the current activity and remove it from the back stack
                finish();
            }
        });
    }
}
