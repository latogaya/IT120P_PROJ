package com.example.products;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chatfunction.ChatMainActivity;
import com.example.login.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orders.OrderMAIN;
import com.tapadoo.alerter.Alerter;

public class ProfilePageActivity extends AppCompatActivity {
    private Switch sellerswitch;

    private TextView username;
    private TextView likedby;
    private ImageView profile_pic;

private DonutProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        ImageView profilePic = findViewById(R.id.profile_pic);
        CircleTransform.applyCircularTransform(profilePic);
        sellerswitch = findViewById(R.id.sellerswitch);
        username = findViewById(R.id.user_name);
        profile_pic = findViewById(R.id.profile_pic);
        likedby = findViewById(R.id.likedby1);
        progressBar = findViewById(R.id.progress_bar);
        loadSwitchState();
        loadSwitchText();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference("Users");
        sellerswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sellerswitch.setText("Seller Mode");
                    progressBar.setVisibility(View.VISIBLE);
                    likedby.setVisibility(View.VISIBLE);
                    progressBar.setMax(100);
                    progressBar.setProgress(80);
                } else {
                    sellerswitch.setText("Buyer Mode");
                    progressBar.setVisibility(View.INVISIBLE);
                    likedby.setVisibility(View.INVISIBLE);
                }
                saveSwitchState(isChecked);
                saveSwitchText(sellerswitch.getText().toString());
            }
        });

        Button orders = findViewById(R.id.orders);
        orders.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfilePageActivity.this, OrderMAIN.class);
                startActivity(intent);
            }
        });


        Button home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sellerswitch.isChecked()) {
                    GotoSellerHome();
                } else {
                    GotoHome();
                }
            }
        });

        Button chat = findViewById(R.id.chat);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePageActivity.this, ChatMainActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        Button profile = findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {showAlert("You're Already at your Profile");}
        });

        Button logoutButton = findViewById(R.id.logoutbutton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ProfilePageActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ImageButton edit_info = findViewById(R.id.edit_info);
        edit_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditInformation();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", "");

        myRef.orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userName = userSnapshot.getKey();
                    String imageStr = userSnapshot.child("profile_pic").getValue(String.class);

                    username.setText(userName);
                    if (imageStr != null) {
                        Glide.with(ProfilePageActivity.this).load(imageStr).into(profile_pic);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
               showAlert("ERROR");
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
                .enableSwipeToDismiss();
        alerter.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Switch sellerswitch = findViewById(R.id.sellerswitch);

        // Reset switch state and text to default
        sellerswitch.setChecked(false);
        sellerswitch.setText("Buyer Mode");

        SharedPreferences prefs = getSharedPreferences("my_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("switch_text", sellerswitch.getText().toString());
        editor.putBoolean("switch_state", false);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSwitchState();
        loadSwitchText();
    }

    private void loadSwitchState() {
        SharedPreferences prefs = getSharedPreferences("my_prefs", MODE_PRIVATE);
        boolean switchState = prefs.getBoolean("switch_state", false);
        sellerswitch.setChecked(switchState);
    }
    public boolean getSellerSwitchState() {
        SharedPreferences prefs = getSharedPreferences("my_prefs", MODE_PRIVATE);
        return prefs.getBoolean("switch_state", false);
    }

    public String getSellerSwitchText() {
        SharedPreferences prefs = getSharedPreferences("my_prefs", MODE_PRIVATE);
        return prefs.getString("switch_text", "Buyer Mode");
    }

    private void loadSwitchText() {
        SharedPreferences prefs = getSharedPreferences("my_prefs", MODE_PRIVATE);
        String switchText = prefs.getString("switch_text", "Buyer Mode");
        sellerswitch.setText(switchText);
    }

    private void saveSwitchText(String text) {
        SharedPreferences prefs = getSharedPreferences("my_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("switch_text", text);
        editor.apply();
    }

    private void saveSwitchState(boolean state) {
        SharedPreferences prefs = getSharedPreferences("my_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("switch_state", state);
        editor.apply();
    }

    public void EditInformation(){
        Intent intent   = new Intent(this, EditInformation.class);
        startActivity(intent);
        //finish();
    }

    public void GotoHome(){
        Intent newIntent = new Intent(this, MainActivity.class);
        startActivity(newIntent);
        //finish();
    }

    public void GotoSellerHome(){
        Intent newIntent = new Intent(this, SellerHomeActivity.class);
        startActivity(newIntent);
        //finish();
    }
}
