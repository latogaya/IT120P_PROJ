package com.example.login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.login.ui.login.DBHelper;
import com.example.login.ui.login.LoginActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

public class CreateNewAccount extends AppCompatActivity {

    EditText emailx, username, passwordx ;
    Button signupbutton;
    ImageView profile_picx;

    DBHelper DB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_account);

        username = findViewById(R.id.username);
        emailx = findViewById(R.id.emailx);
        passwordx = findViewById(R.id.passwordx);
        signupbutton = findViewById(R.id.signupbutton);
        profile_picx = findViewById(R.id.profile_picx);

        DB = new DBHelper(this);
        profile_picx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateNewAccount.this);
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
        });

        // Set up the click listener for the sign up button
        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the values from the input fields
                String email = emailx.getText().toString();
                String password = passwordx.getText().toString();
                String name = username.getText().toString();
                BitmapDrawable drawable = (BitmapDrawable) profile_picx.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                byte[] byteArray= null;
                if (bitmap != null) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byteArray = stream.toByteArray();
                }

                // Check if any of the input fields are empty
                if (TextUtils.isEmpty(name) || byteArray == null || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(CreateNewAccount.this, "All fields required", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if a user with the given email address already exists in the database
                boolean checkUser = DB.checkEmailExists(email);
                if (checkUser) {
                    Toast.makeText(CreateNewAccount.this, "User already exists", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Insert the new user into the database
                boolean insert = DB.insertData(name, email, password, byteArray);
                if (insert) {
                    Toast.makeText(CreateNewAccount.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                    // If the registration was successful, navigate to the next page
                    Intent intent = new Intent(CreateNewAccount.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(CreateNewAccount.this, "Registration failed", Toast.LENGTH_SHORT).show();
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
                ImageView ivClothesImage = findViewById(R.id.profile_picx);
                ivClothesImage.setImageBitmap(bitmap);
            }
        }
    }
}
