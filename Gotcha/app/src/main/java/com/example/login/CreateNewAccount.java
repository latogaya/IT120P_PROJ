package com.example.login;

import androidx.annotation.NonNull;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.ui.login.DBHelper;
import com.example.login.ui.login.LoginActivity;
import com.example.products.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.models.User;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

public class CreateNewAccount extends AppCompatActivity {

    EditText emailx, username, passwordx ;
    Button signupbutton;
    ImageView profile_picx;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_account);
        auth = FirebaseAuth.getInstance();
        username = findViewById(R.id.username);
        emailx = findViewById(R.id.emailx);
        passwordx = findViewById(R.id.passwordx);
        signupbutton = findViewById(R.id.signupbutton);
        profile_picx = findViewById(R.id.profile_picx);

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
                //register(name, email, password);
                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                StorageReference imageRef = storageRef.child("Users").child(name).child("profile_pic.png");
                UploadTask uploadTask = imageRef.putBytes(byteArray);

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get the download URL of the image and add it to the user object
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                User user = new User(name, email, password, uri.toString());

                                // Get a reference to the Firebase database and check if the email already exists
                                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
                                usersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            // If the email already exists, show an error message
                                            Toast.makeText(CreateNewAccount.this, "User with this email already exists", Toast.LENGTH_SHORT).show();
                                        } else {
                                            // If the email doesn't exist, add the new user
                                            usersRef.child(name).setValue(user);

                                            // Show a toast message to indicate success
                                            Toast.makeText(CreateNewAccount.this, "Registered successfully", Toast.LENGTH_SHORT).show();

                                            // Clear the input fields
                                            emailx.setText("");
                                            passwordx.setText("");
                                            username.setText("");
                                            profile_picx.setImageBitmap(null);
                                            register(name, email, password);
                                            Intent intent = new Intent(CreateNewAccount.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(CreateNewAccount.this, "Failed to Register", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });

        TextView alreadyRegText = findViewById(R.id.alreadyRegText);
        alreadyRegText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateNewAccount.this, LoginActivity.class);
                startActivity(intent);
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

    private void register(String name, String email, String password){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userid = firebaseUser.getUid();

                        }
                    }
                });
    }
}
