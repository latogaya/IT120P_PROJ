package com.example.products;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.chatfunction.ChatMainActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.inventory.InventoryMain;
import com.models.Product;
import com.tapadoo.alerter.Alerter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class add_product extends AppCompatActivity {

    private EditText clothesNameEditText;
    private EditText clothesBrandEditText;
    private EditText clothesPriceEditText;
    private EditText clothesSizeEditText;
    private EditText clothesConditionEditText;
    private EditText clothesTypeEditText;
    private EditText descriptionContentEditText;
    private ImageView clothesImageImageView;
    private Button submitButton;
    private Button homeButton;
    private Button chatButton;
    private Button inventoryButton;
    private Button profileButton;
    private ImageButton notifbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(add_product.this, SellerHomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        notifbutton = findViewById(R.id.notif);
        homeButton = findViewById(R.id.home);
        chatButton = findViewById(R.id.chat);
        inventoryButton = findViewById(R.id.inventory);
        profileButton = findViewById(R.id.profile);
        submitButton = findViewById(R.id.submit);
        clothesNameEditText = findViewById(R.id.clothes_name);
        clothesBrandEditText = findViewById(R.id.clothes_brand);
        clothesPriceEditText = findViewById(R.id.clothes_price);
        clothesSizeEditText = findViewById(R.id.clothes_size);
        clothesConditionEditText = findViewById(R.id.clothes_condition);
        clothesTypeEditText = findViewById(R.id.clothes_type);
        descriptionContentEditText = findViewById(R.id.description_content);
        clothesImageImageView = findViewById(R.id.clothes_image);

        homeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(add_product.this, SellerHomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(add_product.this, ProfilePageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        chatButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(add_product.this, ChatMainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        inventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(add_product.this, InventoryMain.class);
                startActivity(intent);
                finish();
                                               }
        });


        notifbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                if (alertsList.isEmpty()) {
                    showAlert("No new notifications");
                } else {
                    showAlert(alertsList.get(alertsList.size() - 1));
                }
            }
        });

        clothesImageImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(add_product.this);
                builder.setTitle("Upload Image")
                        .setMessage("Do you want to upload an image?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, 1);
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
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String clothes_name = clothesNameEditText.getText().toString();
                String clothes_brand = clothesBrandEditText.getText().toString();
                String clothes_price = clothesPriceEditText.getText().toString();
                String clothes_size = clothesSizeEditText.getText().toString();
                String clothes_condition = clothesConditionEditText.getText().toString();
                String clothes_type = clothesTypeEditText.getText().toString();
                String description_content = descriptionContentEditText.getText().toString();
                BitmapDrawable drawable = (BitmapDrawable) clothesImageImageView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                byte[] byteArray = null;
                if (bitmap != null) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byteArray = stream.toByteArray();
                }

                if (clothes_name.isEmpty() || clothes_brand.isEmpty() || clothes_price.isEmpty()
                        || clothes_size.isEmpty() || clothes_condition.isEmpty() || clothes_type.isEmpty()
                        || description_content.isEmpty() || byteArray == null) {
                    Toast.makeText(add_product.this, "Please fill up all the necessary details", Toast.LENGTH_SHORT).show();
                } else {
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                    StorageReference imageRef = storageRef.child("product_details").child(clothes_name).child("clothes_image.png");
                    UploadTask uploadTask = imageRef.putBytes(byteArray);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            // Get the download URL of the image and add it to the product object
                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    Product product = new Product(clothes_name, clothes_brand, clothes_price, clothes_size, clothes_condition, clothes_type, description_content, uri.toString());

                                    // Get a reference to the Firebase database and add the new product
                                    DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("product_details");
                                    productsRef.child("11").setValue(product);


                                    // Show a toast message to indicate success
                                    showAlert("Product Added Successfully!");

                                    // Clear the input fields
                                    clothesNameEditText.setText("");
                                    clothesBrandEditText.setText("");
                                    clothesPriceEditText.setText("");
                                    clothesSizeEditText.setText("");
                                    clothesConditionEditText.setText("");
                                    clothesTypeEditText.setText("");
                                    descriptionContentEditText.setText("");
                                    clothesImageImageView.setImageBitmap(null);

                        }
                    });
                }
            });
        }

        }
    });
}
    ArrayList<String> alertsList = new ArrayList<>();
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
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                clothesImageImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}