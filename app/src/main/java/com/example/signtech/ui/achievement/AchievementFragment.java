package com.example.signtech.ui.achievement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.signtech.R;
import com.example.signtech.databinding.FragmentAchievementBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class AchievementFragment extends Fragment {

    private FragmentAchievementBinding binding;

    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private String userID;
    private int i;

    int[] awards = new int[]{
            R.id.award1, R.id.award2, R.id.award3,
    };

    String[] awardsName = new String[]{
            "Got 10 Score in Beginner!", "Got 10 Score in Intermediate!", "Got 10 Score in Advanced!",
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAchievementBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        mDatabase.child(userID).child("Achievements").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    String awardName = postSnapshot.getKey();
                    Boolean isAcquired = (Boolean) postSnapshot.getValue();
                    if (isAcquired) {
                        for (i = 0; i < awards.length; i++) {
                            String key = awardsName[i];
                            if (awardName.equalsIgnoreCase(key)) {
                                TextView awardTV = root.findViewById(awards[i]);
                                awardTV.setVisibility(View.VISIBLE);
                                awardTV.setText(awardName.toString());
                            }
                        }
                    } else {
                        for (i = 0; i < awards.length; i++) {
                            String key = awardsName[i];
                            if (awardName.equalsIgnoreCase(key)) {
                                TextView awardTV = root.findViewById(awards[i]);
                                awardTV.setVisibility(View.GONE);
                                awardTV.setText("");
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(root.getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}