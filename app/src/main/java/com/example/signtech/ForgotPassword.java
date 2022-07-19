package com.example.signtech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    private EditText etEmailAddress;
    private Button btnResetPass;
    private ProgressBar progressBarReset;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etEmailAddress = (EditText) findViewById(R.id.etEmailAddress);
        btnResetPass = (Button) findViewById(R.id.btnResetPass);
        progressBarReset = (ProgressBar) findViewById(R.id.progressBarReset);

        mAuth = FirebaseAuth.getInstance();


        btnResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPass();

            }
        });

    }

    private void resetPass() {
        String email = etEmailAddress.getText().toString().trim();


        if (email.isEmpty()){
            etEmailAddress.setError("Email is required");
            etEmailAddress.requestFocus();
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmailAddress.setError("Enter Valid Email");
            etEmailAddress.requestFocus();
        }

        progressBarReset.setVisibility(View.VISIBLE);
        btnResetPass.setVisibility(View.INVISIBLE);
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(ForgotPassword.this,"The reset password will be send to your email address",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ForgotPassword.this,Login.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(ForgotPassword.this,"There is something wrong",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}