package com.example.products;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.chatfunction.ChatMainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.inventory.InventoryMain;
import com.models.Product;
import com.tapadoo.alerter.Alerter;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SellerProductDetailActivity extends AppCompatActivity {

    Button submitButton;
    ImageButton deleteButton;
    ImageButton updateButton;
    TextView update;
    TextView delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_product_detail);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ImageView profilePic1 = findViewById(R.id.profile_pic1);
        CircleTransform.applyCircularTransform(profilePic1);
        ImageView profilePic2 = findViewById(R.id.profile_pic2);
        CircleTransform.applyCircularTransform(profilePic2);
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerProductDetailActivity.this, SellerHomeActivity.class);
                startActivity(intent);
            }
        });

        updateButton = findViewById(R.id.updateButton);
        deleteButton = findViewById(R.id.deleteButton);
        EditText clothesNameEditText = findViewById(R.id.clothes_name);
        EditText clothesBrand = findViewById(R.id.clothes_brand);
        EditText clothesPrice = findViewById(R.id.clothes_price);
        EditText clothesSize = findViewById(R.id.clothes_size);
        EditText clothesCondition = findViewById(R.id.clothes_condition);
        EditText clothesType = findViewById(R.id.clothes_type);
        EditText descriptionContent = findViewById(R.id.description_content);
        ImageView clothesImage = findViewById(R.id.clothes_image);
        TextView delete = findViewById(R.id.delete);
        TextView update = findViewById(R.id.update);
        submitButton = findViewById(R.id.submit);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clothesNameEditText.setEnabled(true);
                clothesBrand.setEnabled(true);
                clothesPrice.setEnabled(true);
                clothesSize.setEnabled(true);
                clothesCondition.setEnabled(true);
                clothesType.setEnabled(true);
                descriptionContent.setEnabled(true);
                submitButton.setVisibility(View.VISIBLE);
                updateButton.setVisibility(View.INVISIBLE);
                deleteButton.setVisibility(View.INVISIBLE);
                delete.setVisibility(View.INVISIBLE);
                update.setVisibility(View.INVISIBLE);
                clothesImage.setImageResource(R.drawable.update_image_placeholder);
            }
        });

        clothesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateButton.getVisibility() == View.INVISIBLE) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SellerProductDetailActivity.this);
                    builder.setTitle("Upload Image")
                            .setMessage("Do you want to upload an image?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(intent, 1001);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        ImageButton notifbutton = findViewById(R.id.notif);
        notifbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                Alerter alerter = Alerter.create(SellerProductDetailActivity.this)
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

        Button chat = findViewById(R.id.chat);
        chat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerProductDetailActivity.this, ChatMainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button inventory = findViewById(R.id.inventory);
        inventory.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerProductDetailActivity.this, InventoryMain.class);
                startActivity(intent);
                finish();
            }
        });

        DatabaseReference productrefs = FirebaseDatabase.getInstance().getReference().child("product_details");
        productrefs.child("1").addValueEventListener(new ValueEventListener() {
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
                String formattedPrice = "$" + clothesPrice + ".00";

                // Update the UI with the data
                ImageView ivClothesImage = findViewById(R.id.clothes_image);
                Glide.with(SellerProductDetailActivity.this)
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

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("product_details");
        submitButton = findViewById(R.id.submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = clothesNameEditText.getText().toString();
                String brand = clothesBrand.getText().toString();
                String priceString = clothesPrice.getText().toString();
                //String formattedPrice = "$" + String.format("%.2f", priceString);
                String size = clothesSize.getText().toString();
                String condition = clothesCondition.getText().toString();
                String type = clothesType.getText().toString();
                String description = descriptionContent.getText().toString();
                BitmapDrawable drawable = (BitmapDrawable) clothesImage.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                byte[] byteArray = null;
                if (bitmap != null) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byteArray = stream.toByteArray();
                }
                if (byteArray != null) {
                    // upload image to Firebase Storage

                    StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("clothes_image");
                    UploadTask uploadTask = storageRef.putBytes(byteArray);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // save the image URL in Firebase Realtime Database

                                    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("product_details").child("1");
                                    Map<String, Object> product = new HashMap<>();
                                    product.put("clothes_name", name);
                                    product.put("clothes_brand", brand);
                                    product.put("clothes_price", priceString);
                                    product.put("clothes_size", size);
                                    product.put("clothes_condition", condition);
                                    product.put("clothes_type", type);
                                    product.put("description_content", description);
                                    product.put("status", "grab");
                                    product.put("clothes_image", uri.toString());
                                    databaseRef.setValue(product);
                                    clothesNameEditText.setEnabled(false);
                                    clothesBrand.setEnabled(false);
                                    clothesPrice.setEnabled(false);
                                    clothesSize.setEnabled(false);
                                    clothesCondition.setEnabled(false);
                                    clothesType.setEnabled(false);
                                    descriptionContent.setEnabled(false);
                                    submitButton.setVisibility(View.INVISIBLE);
                                    updateButton.setVisibility(View.VISIBLE);
                                    deleteButton.setVisibility(View.VISIBLE);
                                    delete.setVisibility(View.VISIBLE);
                                    update.setVisibility(View.VISIBLE);
                                    //clothesImage.setImageResource();
                                    showAlert("Product Successfully Updated");
                                }
                            });
                        }
                    });
                }
            }
        });

        ImageButton deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SellerProductDetailActivity.this);
                builder.setTitle("Confirm Deletion")
                        .setMessage("Are you sure you want to delete this item?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("product_details");
                                dbRef.child("1").removeValue(new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                        if (error == null) {
                                            showAlert("Product Deleted Successfully");
                                            finish();
                                        } else {
                                            showAlert("Failed to Delete Product");
                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the deletion, do nothing
                            }
                        });
                builder.create().show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001 && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (bitmap != null) {
                ImageView ivClothesImage = findViewById(R.id.clothes_image);
                ivClothesImage.setImageBitmap(bitmap);
            }
        }
    }
}