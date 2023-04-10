package com.example.products;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.databases.ProductDBHelper;
import com.models.Product;


public class ProductDetailActivity extends AppCompatActivity {
    private ProductDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);

        ImageView profilePic = findViewById(R.id.profile_pic);
        CircleTransform.applyCircularTransform(profilePic);

        Button backButton = findViewById(R.id.back_home);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dbHelper = new ProductDBHelper(this);
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

        String selection = "clothes_id = ?";
        String[] selectionArgs = { "7" };
        String sortOrder = "clothes_name DESC";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
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

        final TextView tvClothesName = findViewById(R.id.clothes_name);
        final TextView tvClothesBrand = findViewById(R.id.clothes_brand);
        final TextView tvClothesPrice = findViewById(R.id.clothes_price);
        final TextView tvClothesSize = findViewById(R.id.clothes_size);
        final TextView tvClothesCondition = findViewById(R.id.clothes_condition);
        final TextView tvClothesType = findViewById(R.id.clothes_type);

// Add a ViewTreeObserver to the TextView
        tvClothesType.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Check if the text has become 2 lines
                if (tvClothesType.getLineCount() > 1) {
                    // Set a smaller text size
                    tvClothesType.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                } else {
                    // Set the original text size
                    tvClothesType.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                }
                // Remove the ViewTreeObserver after it has been triggered
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



    }
}