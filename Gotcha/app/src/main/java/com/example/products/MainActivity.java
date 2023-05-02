package com.example.products;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.Glide;
import com.chatfunction.ChatMainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orders.OrderMAIN;
import com.tapadoo.alerter.Alerter;
import com.yalantis.library.Koloda;
import com.yalantis.library.KolodaListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements KolodaListener {

    private Koloda koloda;
    private SwipeAdapter adapter;
    private List<Integer> list;
    private int currentposition = 1;

    private TextView clothes_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showAlert("You're already at Home");
            }
        });

        Button profile = findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfilePageActivity.class);
                startActivity(intent);
            }
        });

        Button chat = findViewById(R.id.chat);
        chat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ChatMainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button orders = findViewById(R.id.orders);
        orders.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, OrderMAIN.class);
                startActivity(intent);
                finish();
            }
        });

        ImageButton notifbutton = findViewById(R.id.notif);
        notifbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                Alerter alerter = Alerter.create(com.example.products.MainActivity.this)
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

        koloda = findViewById(R.id.koloda);
        list = new ArrayList<>();
        adapter = new SwipeAdapter(this, list);
        koloda.setAdapter(adapter);
        koloda.setKolodaListener(this);

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        usersRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Retrieve user data and store in a HashMap
                Map<String, Map<String, String>> userMap = new HashMap<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String username = userSnapshot.getKey();
                    String profilePic = userSnapshot.child("profile_pic").getValue(String.class);
                    String ownsProduct = userSnapshot.child("owns_product").getValue(String.class);
                    Map<String, String> userData = new HashMap<>();
                    userData.put("username", username);
                    userData.put("profilePic", profilePic);
                    //userData.put("ownsProduct", ownsProduct);
                    userMap.put(ownsProduct, userData);
                    currentposition = 0;
                }
                // Retrieve product data and display in the card views
                DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("product_details");
                productRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Iterate through the snapshot to get each product detail
                        for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                            String clothesName = productSnapshot.child("clothes_name").getValue(String.class);
                            String clothesImage = productSnapshot.child("clothes_image").getValue(String.class);
                            String status = productSnapshot.child("status").getValue(String.class);
                            String ownsProduct = productSnapshot.getKey();
                            // Look up user data from the HashMap
                            Map<String, String> userData = userMap.get(ownsProduct);

                            for (Map<String, String> user : userMap.values()) {
                                if (ownsProduct.equals(user.get("ownsProduct"))) {
                                    userData = user;
                                    break;
                                }
                            }

                            if (userData != null) {
                                String profilePic = userData.get("profilePic");
                                String displayName = userData.get("username");
                                // Check if there is a card view available, otherwise inflate a new one
                                View cardView = koloda.getChildAt(currentposition);
                                if (cardView == null) {
                                    cardView = LayoutInflater.from(MainActivity.this).inflate(R.layout.card_front, koloda, false);

                                }

                                ImageView imageView = cardView.findViewById(R.id.profile_pic);
                                TextView textView = cardView.findViewById(R.id.user_name);
                                textView.setText(displayName);
                                ImageView clothesImageView = cardView.findViewById(R.id.clothes_image);
                                clothes_name = cardView.findViewById(R.id.clothes_name);
                                clothes_name.setText(clothesName);
                                Glide.with(MainActivity.this).load(profilePic).into(imageView);
                                Glide.with(MainActivity.this).load(clothesImage).into(clothesImageView);
                                System.out.println(currentposition);
                                // Set the values to the views of the card_front layout
                                if (status.equals("steal")) {
                                    cardView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.steal)));
                                } else if (status.equals("mine")) {
                                    cardView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.mine)));
                                } else if (status.equals("grab")) {
                                    cardView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.grab)));
                                }
                               currentposition++;

                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        showAlert("Error: " + databaseError.getMessage());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showAlert("Error: " + databaseError.getMessage());
            }
        });

        // Set up gesture detection on Koloda view
        koloda.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    float deltaX = e2.getX() - e1.getX();
                    float deltaY = e2.getY() - e1.getY();
                    if (Math.abs(deltaX) > Math.abs(deltaY)) {
                        if (Math.abs(deltaX) > 100 && Math.abs(velocityX) > 100) {
                            if (deltaX > 0) {
                            } else {
                            }
                            return true;
                        }
                    }
                    return false;
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    public void onCardSwipedLeft(int position) {showAlert("Successfully STOLEN");}

    public void onCardSwipedRight(int position) {showAlert("Successfully MINE'd");}

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
    @Override
    public void onCardDoubleTap(int position) {
        int clothes_id = position + 1;
        System.out.println(clothes_id);
        String positionAsString = String.valueOf(position);

        Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
        intent.putExtra("clothes_id", positionAsString);
        startActivity(intent);
        finish();
    }

    @Override
    public void onCardDrag(int i, @NonNull View view, float v) {
    }

    @Override
    public void onCardLongPress(int position) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String userId = "Lex Ogaya";
                int productId = 1;
                String key = FirebaseDatabase.getInstance().getReference().child("transactions").getKey();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("for_user", userId);
                childUpdates.put("for_product", productId);

                FirebaseDatabase.getInstance().getReference(key).child("1").updateChildren(childUpdates)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                showAlert("Successfully GRABBED. Please check your Chat");
                                int clothes_id = position;
                                System.out.println(clothes_id);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle the error
                            }
                        });
            }
        }, 750);
    }


    @Override
    public void onCardSingleTap(int i) {

    }

    @Override
    public void onClickLeft(int i) {

    }

    @Override
    public void onClickRight(int i) {

    }

    @Override
    public void onEmptyDeck() {

    }

    @Override
    public void onNewTopCard(int i) {

    }

}
