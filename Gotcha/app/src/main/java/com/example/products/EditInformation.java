package com.example.products;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.chatfunction.ChatMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.orders.OrderMAIN;
import com.tapadoo.alerter.Alerter;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class EditInformation extends AppCompatActivity {
    private EditText nameEditText;
    private EditText passwordEditText;
    private EditText emailEditText;
    private ImageView imageView;

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
                imageView = findViewById(R.id.profile_picx);
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_information);
        nameEditText = findViewById(R.id.fullnameinput);
        emailEditText = findViewById(R.id.emailinput);
        passwordEditText = findViewById(R.id.passwordinput);
        imageView = findViewById(R.id.profile_picx);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {image();}
        });

        Button home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity();
            }
        });

        Button back = findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfilePage();
            }
        });

        Button profile = findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert("You're Already at your Profile, Please use the Back Button");
            }
        });

        Button discard = findViewById(R.id.discardbutton);
        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearUser();
            }
        });

        Button update = findViewById(R.id.updatebutton);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {editUser();}
        });

        Button chat = findViewById(R.id.chat);
        chat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditInformation.this, ChatMainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button orders = findViewById(R.id.orders);
        orders.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditInformation.this, OrderMAIN.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void image() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditInformation.this);
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

    public void MainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void ProfilePage(){
        Intent intent = new Intent(this, ProfilePageActivity.class);
        startActivity(intent);
        finish();
    }
    private void editUser() {
        // Get the new user details from the input fields
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        byte[] byteArray = null;
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();
        }

        // Create a map to update the user details in Firebase
        Map<String, Object> updates = new HashMap<>();
        //updates.put("new_name", name);
        updates.put("email", email);
        updates.put("password", password);
        if (byteArray != null) {
            // Upload the image to Firebase Storage
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/" + name);
            UploadTask uploadTask = storageRef.putBytes(byteArray);
            uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Get the download URL of the uploaded image
                return storageRef.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    updates.put("profile_pic", downloadUri.toString());

                    // Update the user details in Firebase
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(name);
                    userRef.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Show a toast message to indicate success
                                showAlert("Information Successfully Updated");
                                // Clear the input fields
                                nameEditText.setText("");
                                emailEditText.setText("");
                                passwordEditText.setText("");
                                imageView.setImageBitmap(null);
                            } else {
                                // Show a toast message to indicate failure
                                showAlert("Failed to Edit Info");
                            }
                        }
                    });
                } else {
                    // Show a toast message to indicate failure
                    showAlert("Failed to upload image");
                }
            });
        } else {
            // Update the user details in Firebase
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(name);
            userRef.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        // Show a toast message to indicate success
                        showAlert("Information Successfully Updated");
                        // Clear the input fields
                        nameEditText.setText("");
                        emailEditText.setText("");
                        passwordEditText.setText("");
                        imageView.setImageBitmap(null);
                    } else {
                        // Show a toast message to indicate failure
                        showAlert("Failed to Edit Info");
                    }
                }
            });
        }
    }

    private void clearUser() {
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        if (drawable != null) {
            Bitmap bitmap = drawable.getBitmap();
        }
        // Check if any of the input fields are non-empty
        if (!name.isEmpty() || !email.isEmpty() || !password.isEmpty() || drawable != null) {
            // Clear the input fields
            nameEditText.setText("");
            emailEditText.setText("");
            passwordEditText.setText("");
            imageView.setImageBitmap(null);
            showAlert("Text Cleared");
        } else {
            // Display a toast message indicating that there is nothing to clear
            showAlert("The text fields are already cleared");
        }
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
                .enableSwipeToDismiss();
        alerter.show();
    }

}