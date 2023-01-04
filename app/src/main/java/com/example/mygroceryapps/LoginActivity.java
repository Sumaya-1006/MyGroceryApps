package com.example.mygroceryapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    TextView log_text1,log_text2;
    Button log_button;
    EditText log_email, log_pass;
    ProgressBar progressBar;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        log_text1 = findViewById(R.id.log_text1Id);
        log_text2 = findViewById(R.id.log_text2Id);
        log_button = findViewById(R.id.log_buttonId);
        log_email = findViewById(R.id.log_emailId);
        log_pass = findViewById(R.id.log_passId);
        auth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        log_text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            }
        });

        log_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginUser();
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void loginUser() {

        String userEmail = log_email.getText().toString();
        String userPassword = log_pass.getText().toString();

        if(TextUtils.isEmpty(userEmail)){
            Toast.makeText(this, "Email is empty", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(userPassword)){
            Toast.makeText(this, "Password is empty", Toast.LENGTH_SHORT).show();
        }
        if(userPassword.length()<6 ){
            Toast.makeText(this, "Password must be greater than 6", Toast.LENGTH_SHORT).show();
        }

        auth.signInWithEmailAndPassword(userEmail,userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Login unsuccessful", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}