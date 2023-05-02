package com.example.login;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.login.ui.login.DBHelper;
import com.example.login.ui.login.LoginActivity;
import com.example.products.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgot_password extends AppCompatActivity {

    TextView enterPass;
    EditText email3, passwordx2;
    Button sendbutton, backButton;
private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        email3 = (EditText) findViewById(R.id.email3);
        sendbutton= (Button) findViewById(R.id.sendbutton);
        backButton = (Button) findViewById(R.id.backButton);
        mAuth = FirebaseAuth.getInstance();

        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = email3.getText().toString().trim();

                if (TextUtils.isEmpty(userEmail)) {
                    Toast.makeText(forgot_password.this, "Please enter your email address", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.sendPasswordResetEmail(userEmail)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(forgot_password.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(forgot_password.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(forgot_password.this, "email not found", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
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
        finish();
    }
}
