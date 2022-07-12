package com.example.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTP_Verification extends AppCompatActivity {

    EditText etC1;
    EditText etC2;
    EditText etC3;
    EditText etC4;
    EditText etC5;
    EditText etC6;
    TextView tvMobile;
    TextView tvResendBtn;
    Button btnVerify;
    ProgressBar progressBarVerify;

    private String verificationId;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        etC1 = (EditText) findViewById(R.id.etC1);
        etC2 = (EditText) findViewById(R.id.etC2);
        etC3 = (EditText) findViewById(R.id.etC3);
        etC4 = (EditText) findViewById(R.id.etC4);
        etC5 = (EditText) findViewById(R.id.etC5);
        etC6 = (EditText) findViewById(R.id.etC6);
        tvMobile = (TextView) findViewById(R.id.tvMobile);
        tvResendBtn = (TextView) findViewById(R.id.tvResendBtn);
        btnVerify = (Button) findViewById(R.id.btnVerify);
        progressBarVerify = (ProgressBar) findViewById(R.id.progressBarVerify);

        verificationId = getIntent().getStringExtra("verificationId");
        mAuth = FirebaseAuth.getInstance();

        tvMobile.setText(String.format("+63-%s", getIntent().getStringExtra("phone")));

        editTextInput();
        tvResendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+63" + getIntent().getStringExtra("phone"),
                        60l,
                        TimeUnit.SECONDS,
                        OTP_Verification.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {

                            }
                            @Override
                            public void onCodeSent(@NonNull String newverificationId,
                                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                    verificationId = newverificationId;
                                    Toast.makeText(OTP_Verification.this,"OTP send", Toast.LENGTH_LONG).show();

                            }
                        }
                );

            }
        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBarVerify.setVisibility(View.VISIBLE);
                btnVerify.setVisibility(View.INVISIBLE);
                verify();
            }
        });

    }




    public void verify() {
        String Code1 = etC1.getText().toString().trim();
        String Code2 = etC2.getText().toString().trim();
        String Code3 = etC3.getText().toString().trim();
        String Code4 = etC4.getText().toString().trim();
        String Code5 = etC5.getText().toString().trim();
        String Code6 = etC6.getText().toString().trim();

        if (Code1.isEmpty() || Code2.isEmpty() || Code3.isEmpty() || Code4.isEmpty() || Code5.isEmpty() || Code6.isEmpty()) {
            Toast.makeText(OTP_Verification.this,"Otp is not valid",Toast.LENGTH_LONG).show();

        }
        else {
            if (verificationId != null) {
                String VerifyCode = Code1 + Code2 + Code3 + Code4 + Code5 + Code6;

                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,VerifyCode);
                mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBarVerify.setVisibility(View.VISIBLE);
                            btnVerify.setVisibility(View.INVISIBLE);
                            Toast.makeText(OTP_Verification.this,"phone number verified",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(OTP_Verification.this,Home.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(OTP_Verification.this,"Invalid OTP", Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        }
    }

    private void editTextInput() {
        etC1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etC1.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etC2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etC2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etC3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etC3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etC4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etC4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        etC5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etC5.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etC6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etC6.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

}