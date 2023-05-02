package com.orders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import com.chatfunction.ChatMainActivity;
import com.example.products.MainActivity;
import com.example.products.ProfilePageActivity;
import com.example.products.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tapadoo.alerter.Alerter;

import java.util.HashMap;

public class OrderCRUD extends AppCompatActivity {

    private EditText sellerFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_crud);

        sellerFeedback = findViewById(R.id.sellerFeedbackEdit);
        DatabaseReference ratingsRef = FirebaseDatabase.getInstance().getReference("ratings");
        ImageButton notifbutton = findViewById(R.id.notif);
        notifbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                Alerter alerter = Alerter.create(OrderCRUD.this)
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

        Button orders = findViewById(R.id.orders);
        orders.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showAlert("You're already at Order History");}
        });

        Button home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(OrderCRUD.this, MainActivity.class);
                startActivity(newIntent);
                finish();
            }
        });

        Button profile = findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderCRUD.this, ProfilePageActivity.class);
                startActivity(intent);
            }
        });

        Button chat = findViewById(R.id.chat);
        chat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderCRUD.this, ChatMainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button rateButton = findViewById(R.id.rateButton);
        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sellerfeedback = sellerFeedback.getText().toString();
                if(sellerfeedback.isEmpty()){
                    showAlert("Please Input a Rating first");
                } else {
                    // Get the current maximum id
                    ratingsRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long maxId = 0;
                            for (DataSnapshot child : snapshot.getChildren()) {
                                long id = Long.parseLong(child.getKey());
                                if (id > maxId) {
                                    maxId = id;
                                }
                            }
                            long newId = maxId + 1;
                            String newIdString = String.valueOf(newId);
                            // Create a HashMap representing the new rating entry
                            HashMap<String, Object> newRatingEntry = new HashMap<>();
                            newRatingEntry.put("for_user", "Lex Ogaya");
                            newRatingEntry.put("liked", sellerfeedback);

                            // Add the new rating entry to the ratings node
                            ratingsRef.child(newIdString).setValue(newRatingEntry)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            showAlert("Seller has been Successfully Rated");
                                            sellerFeedback.setText("");
                                            sellerFeedback.setHint("Thank You for Rating this Seller");

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            showAlert("Failed to rate seller. Please try again later.");
                                        }
                                    });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            showAlert("Failed to retrieve maximum id. Please try again later.");
                        }
                    });
                }
            }
        });


        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderCRUD.this, OrderMAIN.class);
                startActivity(intent);
                finish();
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