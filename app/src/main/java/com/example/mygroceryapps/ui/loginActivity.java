package com.example.mygroceryapps.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mygroceryapps.R;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginActivity extends AppCompatActivity {
    TextInputEditText mPassword;
    EditText mEmail;
    Button mlogin;
    TextView mforgerpassword, tvLogin, textView;
    ImageButton mCreateBtn;
    ImageView fb,google;
    FirebaseAuth fauth;
    ProgressBar mprogresspar;
    AdView adView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail =  findViewById(R.id.EmailLogin);
        mPassword = findViewById(R.id.PasswordLogin);
        fauth = FirebaseAuth.getInstance();
        tvLogin = findViewById(R.id.tvLogin);
        mlogin =  findViewById(R.id.Login);
        mCreateBtn = findViewById(R.id.SignUPtext);
        mprogresspar =  findViewById(R.id.progressBar1);
        mforgerpassword =  findViewById(R.id.ForgetPassword);
        fb = findViewById(R.id.fbBtn);
        google = findViewById(R.id.googleBtn);


        // Checking if the user is logging in or log out ! ;
        if (fauth.getCurrentUser() != null) {
            if (fauth.getCurrentUser().getEmail().equals("admin@gmail.com")) {
                startActivity(new Intent(loginActivity.this, AdminActivity.class));
                finish();
            } else {
                startActivity(new Intent(loginActivity.this, MainActivity.class));
                finish();
            }

        }

        // fb sign in  on firebase
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginActivity.this,FacebookAuthActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);


            }
        });

        //  google sign in on firebase
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),GoogleSignInActivity.class));
            }
        });

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String Email = mEmail.getText().toString().trim();
                final String Password = mPassword.getText().toString();
                if (TextUtils.isEmpty(Email)) {
                    mEmail.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(Password)) {
                    mPassword.setError("Password is required");
                    return;
                }
                if (Password.length() < 6) {
                    mPassword.setError("Password must be bigger than or equal 6 characters");
                    return;
                }
                // progress in background and i make it here visible.
                mprogresspar.setVisibility(View.VISIBLE);

                // Authenticate
                fauth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (Email.equals("admin@gmail.com") && Password.equals("password")) {
                                Toast.makeText(loginActivity.this, "Welcome My Creator", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(loginActivity.this, AdminActivity.class));
                                finish();
                            } else {
                                Toast.makeText(loginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(loginActivity.this, MainActivity.class));
                                finish();
                            }
                        } else {
                            Toast.makeText(loginActivity.this, "Wrong User name Or Password", Toast.LENGTH_SHORT).show();
                            mprogresspar.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });
        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(loginActivity.this, RegisterActivity.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(tvLogin, "tvLogin");
                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(loginActivity.this, pairs);
                startActivity(intent, activityOptions.toBundle());
            }
        });

        mforgerpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Here we will send a verification message
                startActivity(new Intent(loginActivity.this, ForgetPassword.class));
            }
        });

    }


}