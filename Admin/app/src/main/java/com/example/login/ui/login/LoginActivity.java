package com.example.login.ui.login;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.login.CreateNewAccount;
import com.example.login.forgot_password;
import com.example.admin.Admin;
import com.example.products.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    EditText email1, password1;
    Button loginbutton1, registerbutton, forgotpassword;
    DBHelper DB;
    FirebaseAuth mAuth;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email1=findViewById(R.id.email1);
        password1=findViewById(R.id.password1);
        loginbutton1=findViewById(R.id.loginbutton1);
        registerbutton=findViewById(R.id.regisbutton);
        forgotpassword=findViewById(R.id.forgotpasswordbutton);
        DB= new DBHelper(this);
        mAuth = FirebaseAuth.getInstance();
        loginbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user = email1.getText().toString();
                String pass = password1.getText().toString();

                if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pass))
                    Toast.makeText(LoginActivity.this, "All field Required", Toast.LENGTH_SHORT).show();
                else{
                    mAuth.signInWithEmailAndPassword(user, pass)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("email", user);
                                        editor.apply();
                                        Intent intent = new Intent(getApplicationContext(), Admin.AdminActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

                    registerbutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), CreateNewAccount.class);
                            startActivity(intent);
                            finish();
                        }
                    });

                    forgotpassword.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), forgot_password.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }

            }


