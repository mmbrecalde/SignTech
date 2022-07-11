package com.example.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Number_verification extends AppCompatActivity {

    EditText etPhone;
    Button btnSend;
    ProgressBar progressBarSend;

    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_verification);
        etPhone = (EditText) findViewById(R.id.etPhone);
        btnSend = (Button) findViewById(R.id.btnSend);
        progressBarSend = (ProgressBar) findViewById(R.id.progressBarSend);

        mAuth = FirebaseAuth.getInstance();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                optSend();
            }
        });
    }

    public void optSend() {
        progressBarSend.setVisibility(View.VISIBLE);
        btnSend.setVisibility(View.INVISIBLE);
        String phone = etPhone.getText().toString().trim();

        if (phone.isEmpty()) {
            etPhone.setError("Phone number is required");
            etPhone.requestFocus();
        }

        if (phone.length() != 10) {
            etPhone.setError("phone number must be 10 digits");
            etPhone.requestFocus();
        }

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(Number_verification.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                Toast.makeText(Number_verification.this,"OTP is successfully send", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Number_verification.this, OTP_Verification.class);
                intent.putExtra("phone", etPhone.getText().toString().trim());
                intent.putExtra("verificationId",verificationId);
                startActivity(intent);
                finish();
            }
        };
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+63" + phone)
                        .setTimeout(60l, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

}