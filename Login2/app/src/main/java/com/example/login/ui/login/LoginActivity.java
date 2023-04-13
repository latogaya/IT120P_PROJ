package com.example.login.ui.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.login.CreateNewAccount;
import com.example.login.Nextpage;
import com.example.login.R;
import com.example.login.forgot_password;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    EditText email1, password1;
    Button loginbutton1, registerbutton, forgotpassword;
    DBHelper DB;

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

        loginbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user=email1.getText().toString();
                String pass=password1.getText().toString();

                if(TextUtils.isEmpty(user) || TextUtils.isEmpty(pass))
                    Toast.makeText(LoginActivity.this, "All field Required",Toast.LENGTH_SHORT).show();
                else{
                    Boolean checkEmailAndPassword= DB.checkEmailAndPassword(user,pass);
                    if(checkEmailAndPassword==true) {
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Nextpage.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(LoginActivity.this,"Login Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateNewAccount.class);
                startActivity(intent);
            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), forgot_password.class);
                startActivity(intent);
            }
        });
    }

}
