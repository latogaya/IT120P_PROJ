package com.example.admin;
import static android.content.ContentValues.TAG;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.MyAsyncTask;
import com.example.products.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDB {
    private int id;
    private String clothes_name;
    private String clothes_brand;
    private String clothes_price;
    private String clothes_size;
    private String clothes_condition;
    private String clothes_type;
    private String content_description;
    private String clothes_image;

    public ProductDB(){}

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getName() {return clothes_name;}
    public void setName(String clothes_name) {this.clothes_name = clothes_name;}
    public String getBrand() {return clothes_brand;}
    public void setBrand(String clothes_brand) {this.clothes_brand = clothes_brand;}
    public String getPrice() {return clothes_price;}
    public void setPrice(String clothes_price){this.clothes_price = clothes_price;}
    public String getSize() {return clothes_size;}
    public void setSize(String clothes_size) {this.clothes_size = clothes_size;}
    public String getCondition() {return clothes_condition;}
    public void setCondition(String clothes_condition) {this.clothes_condition = clothes_condition;}
    public String getType() {return clothes_type;}
    public void setType(String clothes_type) {this.clothes_type = clothes_type;}
    public String getDescription() {return content_description;}
    public void setDescription(String content_description) {this.content_description = content_description;}
    public String getImage() {return clothes_image;}
    public void setImage(String clothes_image) {this.clothes_image = clothes_image;}

    public static class ProductActivity extends AppCompatActivity {
        private ListView productsListView;
        private EditText nameEditText;
        private EditText brandEditText;
        private EditText priceEditText;
        private EditText sizeEditText;
        private EditText conditionEditText;
        private EditText typeEditText;
        private EditText descriptionEditText;
        private ImageView imageView;
        private ImageView imageViewBigger;
        private Button addButton;
        private Button editButton;
        private Button clearButton;
        private Button deleteButton;
        private Button backButton;

        private ProductListAdapter productListAdapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_products);
            MyAsyncTask task = new MyAsyncTask();
            task.execute();

            Button userButton = findViewById(R.id.users);
            userButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProductDB.ProductActivity.this, UserDB.UserActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            // Initialize views
            productsListView = findViewById(R.id.productsListView);
            nameEditText = findViewById(R.id.nameEditText);
            brandEditText = findViewById(R.id.brandEditText);
            priceEditText = findViewById(R.id.priceEditText);
            sizeEditText = findViewById(R.id.sizeEditText);
            conditionEditText = findViewById(R.id.conditionEditText);
            typeEditText = findViewById(R.id.typeEditText);
            descriptionEditText = findViewById(R.id.descriptionEditText);
            imageView = findViewById(R.id.imageView);
            imageViewBigger = findViewById(R.id.imageViewBigger);
            addButton = findViewById(R.id.addButton);
            editButton = findViewById(R.id.editButton);
            clearButton = findViewById(R.id.clearButton);
            deleteButton = findViewById(R.id.deleteButton);
            backButton = findViewById(R.id.backButton);
            ProductDB.ProductListAdapter adapter = new ProductDB.ProductListAdapter(ProductDB.ProductActivity.this, new ArrayList<ProductDB>());
            productListAdapter = adapter;
            productsListView.setAdapter(adapter);
            adapter.getProductsFromFirebase();
            productsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Get the selected product from the list view
                    ProductDB productdb = productListAdapter.getItem(position);

                    // Populate the EditText fields with the old product details
                    nameEditText.setText(productdb.getName());
                    brandEditText.setText(productdb.getBrand());
                    priceEditText.setText(productdb.getPrice());
                    sizeEditText.setText(productdb.getSize());
                    conditionEditText.setText(productdb.getCondition());
                    typeEditText.setText(productdb.getType());
                    descriptionEditText.setText(productdb.getDescription());
                    String imageUrl = productdb.getImage();
                    Glide.with(ProductDB.ProductActivity.this)
                            .load(imageUrl)
                            .apply(new RequestOptions()
                                    .placeholder(R.drawable.placeholder_image))
                            .into(imageView);

                }
            });

            // Set listeners for buttons
            imageView.setOnTouchListener(new View.OnTouchListener() {
                private boolean isLongClick = false;
                private Runnable longClickRunnable = new Runnable() {
                    @Override
                    public void run() {
                        isLongClick = true;
                        imageViewBigger.setVisibility(View.VISIBLE);
                    }
                };

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            // The user has pressed down on the imageView, start a long click timer
                            isLongClick = false;
                            v.postDelayed(longClickRunnable, 500); // Set the time to detect a long click (in milliseconds)
                            return true;
                        case MotionEvent.ACTION_UP:
                            // The user has released the imageView, cancel the long click timer
                            v.removeCallbacks(longClickRunnable);
                            if (!isLongClick) {
                                image();
                            } else {
                                // Handle the long click release here
                                imageViewBigger.setVisibility(View.INVISIBLE);
                            }
                            return true;
                    }
                    return false;
                }
            });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {image();}
            });

            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ProductDB.ProductActivity.this, Admin.AdminActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {addProduct();}
            });

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {editProduct();}
            });

            clearButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {clearProduct();}
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {removeProduct();}
            });
        }

        private void image() {
            AlertDialog.Builder builder = new AlertDialog.Builder(ProductDB.ProductActivity.this);
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

        private void addProduct() {
            // Get the product details from the input fields
            String clothes_name = nameEditText.getText().toString();
            String clothes_brand = brandEditText.getText().toString();
            String clothes_price = priceEditText.getText().toString();
            String clothes_size = sizeEditText.getText().toString();
            String clothes_condition = conditionEditText.getText().toString();
            String clothes_type = typeEditText.getText().toString();
            String description_content = descriptionEditText.getText().toString();
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
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
                Toast.makeText(this, "Please fill up all the necessary details", Toast.LENGTH_SHORT).show();
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
                                productsRef.child(clothes_name).setValue(product);

                                // Show a toast message to indicate success
                                Toast.makeText(ProductDB.ProductActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();

                                // Clear the input fields
                                nameEditText.setText("");
                                brandEditText.setText("");
                                priceEditText.setText("");
                                sizeEditText.setText("");
                                conditionEditText.setText("");
                                typeEditText.setText("");
                                descriptionEditText.setText("");
                                imageView.setImageBitmap(null);



                            }
                        });
                    }
                });
            }
        }


        private void editProduct() {
            // Get the selected product from the list view
            int position = productsListView.getCheckedItemPosition();
            if (position == ListView.INVALID_POSITION) {
                Toast.makeText(this, "Please select a product to edit", Toast.LENGTH_SHORT).show();
                return;
            }
            ProductDB productDB = productListAdapter.getItem(position);

            // Get the new product details from the input fields
            //int id = productDBToEdit.getId();
            String clothes_name = nameEditText.getText().toString();
            String clothes_brand = brandEditText.getText().toString();
            String clothes_price = priceEditText.getText().toString();
            String clothes_size = sizeEditText.getText().toString();
            String clothes_condition = conditionEditText.getText().toString();
            String clothes_type = typeEditText.getText().toString();
            String description_content = descriptionEditText.getText().toString();
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            byte[] byteArray = null;
            if (bitmap != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();
            }

            Map<String, Object> updates = new HashMap<>();
            updates.put("clothes_name", clothes_name);
            updates.put("clothes_brand", clothes_brand);
            updates.put("clothes_price", clothes_price);
            updates.put("clothes_size", clothes_size);
            updates.put("clothes_condition", clothes_condition);
            updates.put("clothes_type", clothes_type);
            updates.put("description_content", description_content);

            if(byteArray != null) {
                updates.put("clothes_image", Base64.encodeToString(byteArray, Base64.DEFAULT));
            }

            // Update the products details in Firebase
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("product_details").child(clothes_name);
            userRef.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        // Show a toast message to indicate success
                        Toast.makeText(ProductDB.ProductActivity.this, "Product updated successfully", Toast.LENGTH_SHORT).show();
                        // Clear the input fields
                        nameEditText.setText("");
                        brandEditText.setText("");
                        priceEditText.setText("");
                        sizeEditText.setText("");
                        conditionEditText.setText("");
                        priceEditText.setText("");
                        typeEditText.setText("");
                        brandEditText.setText("");
                        descriptionEditText.setText("");
                        imageView.setImageBitmap(null);
                        // Clear the checked item in the list view
                        productsListView.clearChoices();
                    } else {
                        // Show a toast message to indicate failure
                        Toast.makeText(ProductDB.ProductActivity.this, "Failed to update user", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        private void clearProduct() {
            String name = nameEditText.getText().toString();
            String brand = brandEditText.getText().toString();
            String price = priceEditText.getText().toString();
            String size = sizeEditText.getText().toString();
            String condition = conditionEditText.getText().toString();
            String type = typeEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            if (drawable != null) {
                Bitmap bitmap = drawable.getBitmap();
            }
            if (!name.isEmpty() || !brand.isEmpty() || !price.isEmpty() || !size.isEmpty() || !condition.isEmpty() || !type.isEmpty() || !description.isEmpty() || drawable != null) {
                // Clear the input fields
                nameEditText.setText("");
                brandEditText.setText("");
                priceEditText.setText("");
                sizeEditText.setText("");
                conditionEditText.setText("");
                typeEditText.setText("");
                descriptionEditText.setText("");
                imageView.setImageBitmap(null);
                Toast.makeText(this, "Text Cleared", Toast.LENGTH_SHORT).show();
            } else {
                // Display a toast message indicating that there is nothing to clear
                Toast.makeText(this, "The text fields are already cleared", Toast.LENGTH_SHORT).show();
            }

            // Clear the input fields
            nameEditText.setText("");
            brandEditText.setText("");
            priceEditText.setText("");
            sizeEditText.setText("");
            conditionEditText.setText("");
            typeEditText.setText("");
            descriptionEditText.setText("");
            imageView.setImageBitmap(null);
            Toast.makeText(this, "Text Cleared", Toast.LENGTH_SHORT).show();
        }

        private void removeProduct() {
            // Get the selected product from the list view
            int position = productsListView.getCheckedItemPosition();
            if (position == ListView.INVALID_POSITION) {
                Toast.makeText(this, "Please select a product to remove", Toast.LENGTH_SHORT).show();
                return;
            }
            ProductDB productDBToRemove = productListAdapter.getItem(position);

            String removeProduct = productDBToRemove.getName();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("product_details").child(removeProduct);
            userRef.removeValue();

            // Update the list view
            productListAdapter.notifyDataSetChanged();

            // Show a toast message to indicate success
            Toast.makeText(this, "Product removed successfully", Toast.LENGTH_SHORT).show();

            // Clear the input fields
            nameEditText.setText("");
            brandEditText.setText("");
            priceEditText.setText("");
            sizeEditText.setText("");
            conditionEditText.setText("");
            typeEditText.setText("");
            descriptionEditText.setText("");
            imageView.setImageBitmap(null);
            // Clear the checked item in the list view
            productsListView.clearChoices();
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
                    ImageView ivClothesImage = findViewById(R.id.imageView);
                    ivClothesImage.setImageBitmap(bitmap);
                }
            }
        }

    }

    public static class ProductListAdapter extends ArrayAdapter<ProductDB> {

        private final Context context;
        private final List<ProductDB> productDBS;

        public ProductListAdapter(Context context, List<ProductDB> productDBS) {
            super(context, R.layout.product_list_item, productDBS);
            this.context = context;
            this.productDBS = productDBS;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.product_list_item, parent, false);
            }

            TextView nameTextView = convertView.findViewById(R.id.nameTextView);
            TextView brandTextView = convertView.findViewById(R.id.brandTextView);
            TextView priceTextView = convertView.findViewById(R.id.priceTextView);
            TextView sizeTextView = convertView.findViewById(R.id.sizeTextView);
            TextView conditionTextView = convertView.findViewById(R.id.conditionTextView);
            TextView typeTextView = convertView.findViewById(R.id.typeTextView);
            TextView descriptionTextView = convertView.findViewById(R.id.descriptionTextView);

            ProductDB productDB = getItem(position);

            nameTextView.setText(productDB.getName());
            brandTextView.setText(productDB.getBrand());
            priceTextView.setText(productDB.getPrice());
            sizeTextView.setText(productDB.getSize());
            conditionTextView.setText(productDB.getCondition());
            typeTextView.setText(productDB.getType());
            descriptionTextView.setText(productDB.getDescription());

            return convertView;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            getProductsFromFirebase();
        }

        private void getProductsFromFirebase() {
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("product_details");
            usersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    productDBS.clear();
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        //String name = userSnapshot.getKey();
                        String clothes_brand = userSnapshot.child("clothes_brand").getValue(String.class);
                        String clothes_condition = userSnapshot.child("clothes_condition").getValue(String.class);
                        String clothes_image = userSnapshot.child("clothes_image").getValue(String.class);
                        String clothes_name = userSnapshot.child("clothes_name").getValue(String.class);
                        String clothes_price = userSnapshot.child("clothes_price").getValue(String.class);
                        String clothes_size = userSnapshot.child("clothes_size").getValue(String.class);
                        String clothes_type = userSnapshot.child("clothes_type").getValue(String.class);
                        String description_content = userSnapshot.child("description_content").getValue(String.class);
                        ProductDB productDB = new ProductDB();
                        productDB.setBrand(clothes_brand);
                        productDB.setCondition(clothes_condition);
                        productDB.setImage(clothes_image);
                        productDB.setName(clothes_name);
                        productDB.setPrice(clothes_price);
                        productDB.setSize(clothes_size);
                        productDB.setType(clothes_type);
                        productDB.setDescription(description_content);
                        productDBS.add(productDB);
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "Failed to read value.", error.toException());
                }
            });
        }
    }
}

