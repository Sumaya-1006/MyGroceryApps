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

import com.example.mygroceryapps.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    EditText name,email,pass;
    Button button;
    TextView text1, text2;
    ProgressBar progressBar;
    FirebaseAuth auth;
   FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        name = findViewById(R.id.nameId);
        email = findViewById(R.id.emailId);
        pass = findViewById(R.id.passId);
        button = findViewById(R.id.buttonId);
        text1 = findViewById(R.id.text1Id);
        text2 = findViewById(R.id.text2Id);
        auth = FirebaseAuth.getInstance();
       database = FirebaseDatabase.getInstance();
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }

        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
                progressBar.setVisibility(View.VISIBLE);

            }
        });
    }

    private void createUser() {
        String userName = name.getText().toString();
        String userEmail = email.getText().toString();
        String userPassword = pass.getText().toString();

        if(TextUtils.isEmpty(userName)){
            Toast.makeText(this, "Name is empty", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(userEmail)){
            Toast.makeText(this, "Email is empty", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(userPassword)){
            Toast.makeText(this, "Password is empty", Toast.LENGTH_SHORT).show();
        }
        if(userPassword.length()<6 ){
            Toast.makeText(this, "Password must be greater than 6", Toast.LENGTH_SHORT).show();
        }

        auth.createUserWithEmailAndPassword(userEmail,userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressBar.setVisibility(View.GONE);

                            UserModel userModel = new UserModel(userName,userEmail,userPassword);
                            String id = task.getResult().getUser().getUid();
                            database.getReference().child("Users").child(id).setValue(userModel);
                            Toast.makeText(RegistrationActivity.this, "Registration is successful", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(RegistrationActivity.this, "Registration unsuccessful", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}