package com.example.admin;
import static android.content.ContentValues.TAG;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
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
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDB {

    private String name;
    private String email;
    private String password;
    private String profile_pic;

    public UserDB() {}

    public String getName() {
        return name;
    }
    public void setName(String name){ this.name = name;}
    public String getEmail() {
        return email;
    }
    public void setEmail(String email){ this.email = email;}
    public String getPassword() { return password;}
    public void setPassword(String password){ this.password = password;}
    public String getProfile_pic(){return profile_pic;}
    public void setProfile_pic(String profile_pic){this.profile_pic = profile_pic;}

    public static class UserActivity extends AppCompatActivity {
        private ListView userListView;
        private EditText nameEditText;
        private EditText passwordEditText;
        private EditText emailEditText;
        private Button addButton;
        private Button editButton;
        private Button clearButton;
        private Button deleteButton;
        private Button backButton;
        private ImageView imageView;
        private UserListAdapter userListAdapter;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_users);

            Button productsButton = findViewById(R.id.product);
            productsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UserDB.UserActivity.this, ProductDB.ProductActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            // Initialize views
            userListView = findViewById(R.id.userListView);
            nameEditText = findViewById(R.id.nameEditText);
            emailEditText = findViewById(R.id.emailEditText);
            passwordEditText = findViewById(R.id.passwordEditText);
            imageView = findViewById(R.id.imageView);
            addButton = findViewById(R.id.addButton);
            editButton = findViewById(R.id.editButton);
            clearButton = findViewById(R.id.clearButton);
            deleteButton = findViewById(R.id.deleteButton);
            backButton = findViewById(R.id.backButton);
            // Initialize UserListAdapter
            UserListAdapter adapter = new UserListAdapter(UserDB.UserActivity.this, new ArrayList<UserDB>());
            userListAdapter = adapter;
            userListView.setAdapter(adapter);
            adapter.getUsersFromFirebase();
            userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Get the selected user from the list view
                    UserDB userdb = userListAdapter.getItem(position);
                    // Populate the EditText fields with the old user details
                    nameEditText.setText(userdb.getName());
                    emailEditText.setText(userdb.getEmail());
                    passwordEditText.setText(userdb.getPassword());
                    String imageUrl = userdb.getProfile_pic();
                    Glide.with(UserDB.UserActivity.this)
                            .load(imageUrl)
                            .apply(new RequestOptions()
                            .placeholder(R.drawable.placeholder_image))
                            .into(imageView);
                }
            });

            // Set listeners for buttons
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    image();
                }
            });

            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(UserDB.UserActivity.this, Admin.AdminActivity.class);
                    startActivity(intent);
                    finish();
                }
            });


            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addUser();
                }
            });

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editUser();
                }
            });

            clearButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clearUser();
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeUser();
                }
            });
        }

        private void image() {
            AlertDialog.Builder builder = new AlertDialog.Builder(UserDB.UserActivity.this);
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

        private void addUser() {
            // Get the user details from the input fields
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

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || byteArray == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserDB.UserActivity.this);
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
                // Upload the image to Firebase Storage
                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                StorageReference imageRef = storageRef.child("Users").child("name").child("profile_pic.png");
                UploadTask uploadTask = imageRef.putBytes(byteArray);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get the download URL of the image and add it to the user object
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                User user = new User(name, email, password, uri.toString());

                                // Get a reference to the Firebase database and add the new user
                                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
                                usersRef.child(name).setValue(user);

                                // Show a toast message to indicate success
                                Toast.makeText(UserDB.UserActivity.this, "User added successfully", Toast.LENGTH_SHORT).show();

                                // Clear the input fields
                                nameEditText.setText("");
                                emailEditText.setText("");
                                passwordEditText.setText("");
                                imageView.setImageBitmap(null);
                            }
                        });
                    }
                });
            }
        }

        private void editUser() {
            // Get the selected user from the list view
            int position = userListView.getCheckedItemPosition();
            if (position == ListView.INVALID_POSITION) {
                Toast.makeText(this, "Please select a User to edit", Toast.LENGTH_SHORT).show();
                return;
            }
            UserDB userDB = userListAdapter.getItem(position);

            // Get the new user details from the input fields
            String email = userDB.getEmail(); // get the email of the selected user
            String name = nameEditText.getText().toString();
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
            updates.put("name", name);
            updates.put("password", password);
            if (byteArray != null) {
                updates.put("profile_pic", Base64.encodeToString(byteArray, Base64.DEFAULT));
            }

            // Update the user details in Firebase
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(name);
            userRef.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        // Show a toast message to indicate success
                        Toast.makeText(UserActivity.this, "User updated successfully", Toast.LENGTH_SHORT).show();
                        // Clear the input fields
                        nameEditText.setText("");
                        emailEditText.setText("");
                        passwordEditText.setText("");
                        imageView.setImageBitmap(null);
                        // Clear the checked item in the list view
                        userListView.clearChoices();
                    } else {
                        // Show a toast message to indicate failure
                        Toast.makeText(UserActivity.this, "Failed to update user", Toast.LENGTH_SHORT).show();
                    }
                }
            });
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
                Toast.makeText(this, "Text Cleared", Toast.LENGTH_SHORT).show();
            } else {
                // Display a toast message indicating that there is nothing to clear
                Toast.makeText(this, "The text fields are already cleared", Toast.LENGTH_SHORT).show();
            }
        }


        private void removeUser() {
            // Get the selected user from the list view
            int position = userListView.getCheckedItemPosition();
            if (position == ListView.INVALID_POSITION) {
                Toast.makeText(this, "Please select a User to remove", Toast.LENGTH_SHORT).show();
                return;
            }
            UserDB userDBtoRemove = userListAdapter.getItem(position);

            // Remove the user from Firebase
            String removeUser = userDBtoRemove.getName();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(removeUser);
            userRef.removeValue();

            // Show a toast message to indicate success
            Toast.makeText(this, "User removed successfully", Toast.LENGTH_SHORT).show();

            // Clear the input fields
            nameEditText.setText("");
            emailEditText.setText("");
            passwordEditText.setText("");
            imageView.setImageBitmap(null);

            // Clear the checked item in the list view
            userListView.clearChoices();
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

    public static class UserListAdapter extends ArrayAdapter<UserDB> {

        private final Context context;
        private final List<UserDB> userDBS;

        public UserListAdapter(Context context, List<UserDB> userDBS) {
            super(context, R.layout.list_user, userDBS);
            this.context = context;
            this.userDBS = userDBS;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.list_user, parent, false);
            }

            TextView nameTextView = convertView.findViewById(R.id.nameTextView);
            TextView emailTextView = convertView.findViewById(R.id.emailTextView);
            TextView passwordTextView = convertView.findViewById(R.id.passwordTextView);
            UserDB userDB = getItem(position);

            nameTextView.setText(userDB.getName());
            emailTextView.setText(userDB.getEmail());
            passwordTextView.setText(userDB.getPassword());

            return convertView;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            getUsersFromFirebase();
        }

        private void getUsersFromFirebase() {
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
            usersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userDBS.clear();
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String name = userSnapshot.getKey();
                        String email = userSnapshot.child("email").getValue(String.class);
                        String password = userSnapshot.child("password").getValue(String.class);
                        String profile_pic = userSnapshot.child("profile_pic").getValue(String.class);
                        UserDB userDB = new UserDB();
                        userDB.setName(name);
                        userDB.setEmail(email);
                        userDB.setPassword(password);
                        userDB.setProfile_pic(profile_pic);
                        userDBS.add(userDB);
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
