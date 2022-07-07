package com.example.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etLoginEmail = (EditText) findViewById(R.id.etLoginEmail);
        etLoginPass = (EditText) findViewById(R.id.etLoginPass);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        tvRegisterHere = (TextView) findViewById(R.id.tvRegisterHere);

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


    }

    private void UserLogin() {
        String email = etLoginEmail.getText().toString().trim();
        String pass = etLoginPass.getText().toString().trim();

        if (email.isEmpty()) {
            etLoginEmail.setError("Email is required");
            etLoginEmail.requestFocus();
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etLoginEmail.setError("Provide a valid email");
            etLoginEmail.requestFocus();
        }

        if (pass.isEmpty()) {
            etLoginPass.setError("password is required");
            etLoginPass.requestFocus();
        }

        if (pass.length() < 8) {
            etLoginPass.setError("Min password length is 8 characters");
            etLoginPass.requestFocus();
        }

        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(Login.this,Number_verification.class);
                    startActivity(intent);
                    finish();

                    Toast.makeText(Login.this,"Login successfully", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(Login.this,"Unable to login ",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}