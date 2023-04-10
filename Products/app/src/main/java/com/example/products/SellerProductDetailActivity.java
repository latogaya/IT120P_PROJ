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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.databases.ProductDBHelper;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public class SellerProductDetailActivity extends AppCompatActivity {
    private ProductDBHelper dbHelper;

    String selection = "clothes_id = ?";
    String[] selectionArgs = { "7" };

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

        dbHelper = new ProductDBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                "clothes_name",
                "clothes_brand",
                "clothes_price",
                "clothes_size",
                "clothes_type",
                "clothes_condition",
                "description_content",
                "clothes_image"
        };

        String sortOrder = "clothes_name DESC";
        Cursor cursor = db.query(
                "product_details",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        if (cursor.moveToFirst()) {
            String clothesName = cursor.getString(cursor.getColumnIndexOrThrow("clothes_name"));
            String clothesBrand = cursor.getString(cursor.getColumnIndexOrThrow("clothes_brand"));
            double clothesPrice = cursor.getInt(cursor.getColumnIndexOrThrow("clothes_price"));
            String clothesSize = cursor.getString(cursor.getColumnIndexOrThrow("clothes_size"));
            String clothesType = cursor.getString(cursor.getColumnIndexOrThrow("clothes_type"));
            String clothesCondition = cursor.getString(cursor.getColumnIndexOrThrow("clothes_condition"));
            String descriptionContent = cursor.getString(cursor.getColumnIndexOrThrow("description_content"));
            byte[] clothesImage = cursor.getBlob(cursor.getColumnIndexOrThrow("clothes_image"));
            String formattedPrice = "$" + String.format("%.2f", clothesPrice);

            ImageView ivClothesImage = findViewById(R.id.clothes_image);
            Bitmap bitmap = BitmapFactory.decodeByteArray(clothesImage, 0, clothesImage.length);
            ivClothesImage.setImageBitmap(bitmap);

            TextView tvClothesName = findViewById(R.id.clothes_name);
            tvClothesName.setText(clothesName);

            TextView tvClothesBrand = findViewById(R.id.clothes_brand);
            tvClothesBrand.setText(clothesBrand);

            TextView tvClothesPrice = findViewById(R.id.clothes_price);
            tvClothesPrice.setText(formattedPrice);

            TextView tvClothesSize = findViewById(R.id.clothes_size);
            tvClothesSize.setText(clothesSize);

            TextView tvClothesType = findViewById(R.id.clothes_type);
            tvClothesType.setText(clothesType);

            TextView tvClothesCondition = findViewById(R.id.clothes_condition);
            tvClothesCondition.setText(clothesCondition);

            TextView tvDescriptionContent = findViewById(R.id.description_content);
            tvDescriptionContent.setText(descriptionContent);

        }

        cursor.close();

        ImageButton deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SellerProductDetailActivity.this);
                builder.setTitle("Confirm Deletion")
                        .setMessage("Are you sure you want to delete this item?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SQLiteDatabase db = dbHelper.getWritableDatabase();
                                int deletedRows = db.delete("product_details", selection, selectionArgs);
                                if (deletedRows > 0) {
                                    Toast.makeText(SellerProductDetailActivity.this, "Row deleted successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(SellerProductDetailActivity.this, "Failed to delete row", Toast.LENGTH_SHORT).show();
                                }
                                db.close();
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

        final TextView tvClothesName = findViewById(R.id.clothes_name);
        final TextView tvClothesBrand = findViewById(R.id.clothes_brand);
        final TextView tvClothesPrice = findViewById(R.id.clothes_price);
        final TextView tvClothesSize = findViewById(R.id.clothes_size);
        final TextView tvClothesCondition = findViewById(R.id.clothes_condition);
        final TextView tvClothesType = findViewById(R.id.clothes_type);

        tvClothesType.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (tvClothesType.getLineCount() > 1) {
                    tvClothesType.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                } else {
                    tvClothesType.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                }
                tvClothesType.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        tvClothesName.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (tvClothesName.getLineCount() > 1) {
                    tvClothesName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                } else {
                    tvClothesName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                }
                tvClothesName.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        tvClothesBrand.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (tvClothesBrand.getLineCount() > 1) {
                    tvClothesBrand.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                } else {
                    tvClothesBrand.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                }
                tvClothesBrand.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        tvClothesPrice.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (tvClothesPrice.getLineCount() > 1) {
                    tvClothesPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                } else {
                    tvClothesPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                }
                tvClothesPrice.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        tvClothesSize.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (tvClothesSize.getLineCount() > 1) {
                    tvClothesSize.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                } else {
                    tvClothesSize.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                }
                tvClothesSize.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        tvClothesCondition.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (tvClothesCondition.getLineCount() > 1) {
                    tvClothesCondition.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                } else {
                    tvClothesCondition.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                }
                tvClothesCondition.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        ImageButton updateButton = findViewById(R.id.updateButton);
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
        Button submitButton = findViewById(R.id.submit);
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


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = clothesNameEditText.getText().toString();
                String brand = clothesBrand.getText().toString();
                String priceString = clothesPrice.getText().toString().replace("$", "");
                double price = Double.parseDouble(priceString);
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

                Cursor cursor = db.query("product_details", new String[]{"clothes_name", "clothes_brand", "clothes_price", "clothes_size", "clothes_condition", "clothes_type", "description_content", "clothes_image"}, selection, selectionArgs, null, null, null);
                String currentName = "";
                String currentBrand = "";
                double currentPrice = 0.0;
                String currentSize = "";
                String currentCondition = "";
                String currentType = "";
                String currentDescription = "";
                byte[] currentImage = null;
                if (cursor != null && cursor.moveToFirst()) {
                    currentName = cursor.getString(cursor.getColumnIndexOrThrow("clothes_name"));
                    currentBrand = cursor.getString(cursor.getColumnIndexOrThrow("clothes_brand"));
                    currentPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("clothes_price"));
                    currentSize = cursor.getString(cursor.getColumnIndexOrThrow("clothes_size"));
                    currentCondition = cursor.getString(cursor.getColumnIndexOrThrow("clothes_condition"));
                    currentType = cursor.getString(cursor.getColumnIndexOrThrow("clothes_type"));
                    currentDescription = cursor.getString(cursor.getColumnIndexOrThrow("description_content"));
                    currentImage = cursor.getBlob(cursor.getColumnIndexOrThrow("clothes_image"));
                    cursor.close();
                }

                if (!name.equals(currentName) || !brand.equals(currentBrand) || price != currentPrice ||
                        !size.equals(currentSize) || !condition.equals(currentCondition) ||
                        !type.equals(currentType) || !description.equals(currentDescription) || !Arrays.equals(byteArray, currentImage))  {
                    ContentValues values = new ContentValues();
                    values.put("clothes_name", name);
                    values.put("clothes_brand", brand);
                    values.put("clothes_price", price);
                    values.put("clothes_size", size);
                    values.put("clothes_condition", condition);
                    values.put("clothes_type", type);
                    values.put("description_content", description);
                    if (byteArray != null) {
                        values.put("clothes_image", byteArray);
                    }

                    int rowsAffected = db.update("product_details", values, selection, selectionArgs);

                    if (rowsAffected > 0) {
                        Toast.makeText(getApplicationContext(), "Row updated successfully!", Toast.LENGTH_SHORT).show();
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
                        if (byteArray != null) {
                            clothesImage.setImageBitmap(bitmap);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to update row.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No changes made.", Toast.LENGTH_SHORT).show();
                }
            }
        });
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