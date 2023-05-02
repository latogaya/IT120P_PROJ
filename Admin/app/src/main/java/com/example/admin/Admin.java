package com.example.admin;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import androidx.appcompat.app.AppCompatActivity;
import com.example.login.ui.login.LoginActivity;
import com.example.products.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Admin {

    public static class AdminActivity extends AppCompatActivity {

        private ImageView profile_pic;
        private TextView admin_name;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_admin);
            ImageView profilePic = findViewById(R.id.profile_pic);
            CircleTransform.applyCircularTransform(profilePic);
            profile_pic = findViewById(R.id.profile_pic);
            admin_name = findViewById(R.id.admin_name);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Users");


            SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
            String UserEmail = sharedPreferences.getString("email", "");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            String userEmail = userSnapshot.child("email").getValue(String.class);
                            if (userEmail.equals(UserEmail)) {
                                String userName = userSnapshot.getKey();
                                String imageUrl = userSnapshot.child("profile_pic").getValue(String.class);
                                admin_name.setText(userName);
                                if (imageUrl != null) {
                                    Glide.with(AdminActivity.this)
                                            .load(imageUrl)
                                            .into(profile_pic);
                                }
                                break;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(Admin.AdminActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
                    });

            ImageButton productsButton = findViewById(R.id.product_button);
            productsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Admin.AdminActivity.this, ProductDB.ProductActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            ImageButton userButton = findViewById(R.id.user_button);
            userButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Admin.AdminActivity.this, UserDB.UserActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            Button logoutButton = findViewById(R.id.logoutbutton);
            logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(Admin.AdminActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

        }
    }

}
