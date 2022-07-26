package com.example.signtech.ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.signtech.Login;
import com.example.signtech.R;
import com.example.signtech.User;
import com.example.signtech.databinding.ActivityProfileBinding;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private ActivityProfileBinding binding;

    TextView tvVerified;
    TextView tvWelcome;
    TextView tvPhone;
    TextView tvEmail;
    Button btnLogout;
    AlertDialog.Builder builder;


    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = ActivityProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        tvWelcome = (TextView) root.findViewById(R.id.tvWelcome);
        tvPhone = (TextView) root.findViewById(R.id.tvPhone);
        tvEmail = (TextView) root.findViewById(R.id.tvEmail);
        tvVerified = (TextView) root.findViewById(R.id.tvVerified);
        btnLogout = (Button) root.findViewById(R.id.btnLogout);
        builder = new AlertDialog.Builder(root.getContext());

        EmailVerified();
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        reference.child(userID).child("User Information").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userWelcome = snapshot.getValue(User.class);
                user = FirebaseAuth.getInstance().getCurrentUser();
                if (userWelcome != null) {
                    String name = userWelcome.name;
                    String email = userWelcome.email;
                    String phone = userWelcome.phone;

                    tvWelcome.setText(name);
                    tvEmail.setText(email);
                    tvPhone.setText("+63"+phone);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(root.getContext(),"Something went wrong", Toast.LENGTH_LONG).show();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                builder.setTitle("Logout!")
                        .setMessage("Do you want to Logout?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mAuth.signOut();
                                Intent intent = new Intent(root.getContext(), Login.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .show();
            }
        });

        return root;
    }

    private void EmailVerified() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            if (user.isEmailVerified()) {
                tvVerified.setText("Email verified");
            } else {
                tvVerified.setText("Email not verified (Click to verify)");
                tvVerified.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(binding.getRoot().getContext() , "Email verification sent", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
            }
        }

    }

}