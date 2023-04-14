package com.example.products;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class ProfilePage2 extends AppCompatActivity {

    ImageButton imageButton;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page2);

        imageButton = (ImageButton) findViewById(R.id.seller_mode);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewSellerMode();
            }
        });

        button = (Button) findViewById(R.id.home3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GotoMain();
            }
        });

        imageButton = (ImageButton) findViewById(R.id.seller_mode);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewSellerMode();
            }
        });


        imageButton = (ImageButton) findViewById(R.id.edit_info);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditInformation();
            }
        });

    }

    public void EditInformation(){
        Intent intent   = new Intent(this, EditInformation2.class);
        startActivity(intent);
    }

    public void NewSellerMode(){
        Intent newIntent = new Intent(this, SellerMode2.class);
        startActivity(newIntent);
    }

    public void GotoMain(){
        Intent newIntent = new Intent(this, MainActivity.class);
        startActivity(newIntent);
    }
}