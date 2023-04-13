package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.login.ui.login.DBHelper;
import com.example.login.ui.login.LoginActivity;

public class forgot_password extends AppCompatActivity {

    EditText email3;
    Button sendbutton, backButton;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email3=(EditText) findViewById(R.id.email3);
        sendbutton= (Button) findViewById(R.id.sendbutton);
        backButton = (Button) findViewById(R.id.backButton); // initialize inside onCreate
        DB = new DBHelper(this);

        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user = email3.getText().toString();

                Boolean checkuser = DB.checkEmailExists(user);
                if(checkuser==true)
                {
                    Intent intent = new Intent(getApplicationContext(),forgot_password.class);
                    intent.putExtra("email3", user);
                    startActivity(intent);
                }else{
                    Toast.makeText(forgot_password.this,"Email does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLoginActivity();
            }
        });
    }


    public void openLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
