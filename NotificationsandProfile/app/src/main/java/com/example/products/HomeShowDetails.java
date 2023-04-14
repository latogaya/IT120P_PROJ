package com.example.products;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeShowDetails extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_front);

        ImageButton more_details = findViewById(R.id.more_details);

        more_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(HomeShowDetails.this, ProductDetailActivity.class);
                    startActivity(intent);

            }
        });

        /*
        //ImageView myImageView = findViewById(R.id.prod_img);
        //CardView cview = findViewById(R.id.back_card);
        //LinearLayout img_card = findViewById(R.id.card_img_layout);
        LinearLayout whole_card = findViewById(R.id.top_seller_deets);
        LinearLayout myLinearLayout = findViewById(R.id.product_detail);
        //CardView backCard = findViewById(R.id.front_card); //testing

        whole_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                whole_card.setVisibility(View.INVISIBLE);
                if (myLinearLayout.getVisibility() == View.VISIBLE)
                {
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
        }); */

    }

}
