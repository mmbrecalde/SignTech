package com.example.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    EditText etRegisterName;
    EditText etRegisterEmail;
    EditText etRegisterPhone;
    EditText etRegisterPass;
    EditText etRegisterConfirmPass;
    Button btnRegister;
    TextView tvLoginHere;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etRegisterName = (EditText) findViewById(R.id.etRegisterName);
        etRegisterEmail = (EditText) findViewById(R.id.etRegisterEmail);
        etRegisterPhone = (EditText) findViewById(R.id.etRegisterPhone);
        etRegisterPass = (EditText) findViewById(R.id.etRegisterPass);
        etRegisterConfirmPass = (EditText) findViewById(R.id.etRegisterConfirmPass);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        tvLoginHere = (TextView) findViewById(R.id.tvLoginHere);


        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        tvLoginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this,Login.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void registerUser() {
        String name = etRegisterName.getText().toString().trim();
        String email = etRegisterEmail.getText().toString().trim();
        String phone = etRegisterPhone.getText().toString().trim();
        String pass = etRegisterPass.getText().toString().trim();
        String confirmpass = etRegisterConfirmPass.getText().toString().trim();

        if(name.isEmpty()) {
            etRegisterName.setError("name is required");
            etRegisterName.requestFocus();
            return;

        }
        if(email.isEmpty()) {
            etRegisterEmail.setError("email is required");
            etRegisterEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etRegisterEmail.setError("Please provide a valid email");
            etRegisterEmail.requestFocus();

        }
        if (phone.isEmpty()) {
            etRegisterPhone.setError("phone Number is required");
            etRegisterPhone.requestFocus();
        }
        if (pass.isEmpty()) {
            etRegisterPass.setError("password is required");
            etRegisterPass.requestFocus();

        }
        if (pass.length() < 8) {
            etRegisterPass.setError("Min password length should be 8 characters");
            etRegisterPass.requestFocus();
        }

        if (confirmpass.isEmpty()) {
            etRegisterConfirmPass.setError("confirm password is required");
            etRegisterConfirmPass.requestFocus();
        }

        if (pass.equals(confirmpass)) {
            mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        User user = new User(name,email,phone);

                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Register.this,"User has been registered successfully", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(Register.this,Login.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Toast.makeText(Register.this,"Failed to register try again", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }else {
                        Toast.makeText(Register.this,"Failed to register try again", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(Register.this,"Password Unmatched", Toast.LENGTH_LONG).show();
        }

    }
}