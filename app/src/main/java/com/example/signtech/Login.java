package com.example.signtech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText etLoginEmail;
    EditText etLoginPass;
    Button btnLogin;
    TextView tvRegisterHere;
    TextView tvForgotPassword;
    ProgressBar progressBarLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etLoginEmail = (EditText) findViewById(R.id.etLoginEmail);
        etLoginPass = (EditText) findViewById(R.id.etLoginPass);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        tvRegisterHere = (TextView) findViewById(R.id.tvRegisterHere);
        tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        progressBarLogin = (ProgressBar) findViewById(R.id.progressBarLogin);

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserLogin();
            }
        });
        tvRegisterHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,Register.class));
                finish();
            }
        });
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, ForgotPassword.class);
                startActivity(intent);

            }
        });

    }

    private void UserLogin() {
        progressBarLogin.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.INVISIBLE);
        String email = etLoginEmail.getText().toString().trim();
        String pass = etLoginPass.getText().toString().trim();

        if (email.isEmpty()) {
            progressBarLogin.setVisibility(View.INVISIBLE);
            btnLogin.setVisibility(View.VISIBLE);
            etLoginEmail.setError("Email is required");
            etLoginEmail.requestFocus();
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            progressBarLogin.setVisibility(View.INVISIBLE);
            btnLogin.setVisibility(View.VISIBLE);
            etLoginEmail.setError("Provide a valid email");
            etLoginEmail.requestFocus();
        }

        if (pass.isEmpty()) {
            progressBarLogin.setVisibility(View.INVISIBLE);
            btnLogin.setVisibility(View.VISIBLE);
            etLoginPass.setError("password is required");
            etLoginPass.requestFocus();
        } else {
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(Login.this, MainHome.class);
                        startActivity(intent);
                        finish();

                        Toast.makeText(Login.this, "Login successfully", Toast.LENGTH_LONG).show();
                    } else {
                        progressBarLogin.setVisibility(View.INVISIBLE);
                        btnLogin.setVisibility(View.VISIBLE);
                        Toast.makeText(Login.this, "Unable to login ", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        if (pass.length() < 8) {
            etLoginPass.setError("Min password length is 8 characters");
            etLoginPass.requestFocus();
        }

    }
}