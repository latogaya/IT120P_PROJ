package com.example.products;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.databases.ProductDBHelper;
import com.models.Product;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class add_product extends AppCompatActivity {

    private EditText clothesNameEditText;
    private EditText clothesBrandEditText;
    private EditText clothesPriceEditText;
    private EditText clothesSizeEditText;
    private EditText clothesConditionEditText;
    private EditText clothesTypeEditText;
    private EditText descriptionContentEditText;
    private ImageView clothesImageImageView;

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
            }
        });

        clothesNameEditText = findViewById(R.id.clothes_name);
        clothesBrandEditText = findViewById(R.id.clothes_brand);
        clothesPriceEditText = findViewById(R.id.clothes_price);
        clothesSizeEditText = findViewById(R.id.clothes_size);
        clothesConditionEditText = findViewById(R.id.clothes_condition);
        clothesTypeEditText = findViewById(R.id.clothes_type);
        descriptionContentEditText = findViewById(R.id.description_content);
        clothesImageImageView = findViewById(R.id.clothes_image);

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

        Button submitButton = findViewById(R.id.submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String clothesName = clothesNameEditText.getText().toString();
                String clothesBrand = clothesBrandEditText.getText().toString();
                String clothesPrice = clothesPriceEditText.getText().toString();
                String clothesSize = clothesSizeEditText.getText().toString();
                String clothesCondition = clothesConditionEditText.getText().toString();
                String clothesType = clothesTypeEditText.getText().toString();
                String descriptionContent = descriptionContentEditText.getText().toString();
                BitmapDrawable drawable = (BitmapDrawable) clothesImageImageView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                byte[] byteArray = null;
                if (bitmap != null) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byteArray = stream.toByteArray();
                }

                if (clothesName.isEmpty() || clothesBrand.isEmpty() || clothesPrice.isEmpty()
                        || clothesSize.isEmpty() || clothesCondition.isEmpty() || clothesType.isEmpty()
                        || descriptionContent.isEmpty() || byteArray == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(add_product.this);
                    builder.setTitle("Error")
                            .setMessage("Please fill in all the fields.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    ProductDBHelper dbHelper = new ProductDBHelper(add_product.this);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("clothes_name", clothesName);
                    values.put("clothes_brand", clothesBrand);
                    values.put("clothes_price", Double.parseDouble(clothesPrice));
                    values.put("clothes_size", clothesSize);
                    values.put("clothes_condition", clothesCondition);
                    values.put("clothes_type", clothesType);
                    values.put("description_content", descriptionContent);
                    values.put("clothes_image", byteArray);
                    long newRowId = db.insert("product_details", null, values);

                    clothesNameEditText.setText("");
                    clothesBrandEditText.setText("");
                    clothesPriceEditText.setText("");
                    clothesSizeEditText.setText("");
                    clothesConditionEditText.setText("");
                    clothesTypeEditText.setText("");
                    descriptionContentEditText.setText("");
                    clothesImageImageView.setImageResource(R.drawable.image_placeholder);

                    AlertDialog.Builder builder = new AlertDialog.Builder(add_product.this);
                    builder.setTitle("Success")
                            .setMessage("Product added successfully!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
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