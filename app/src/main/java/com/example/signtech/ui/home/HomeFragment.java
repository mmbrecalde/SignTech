package com.example.signtech.ui.home;

import static com.example.signtech.MainHome.activityVisible;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.signtech.R;
import com.example.signtech.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class HomeFragment extends Fragment {

    //Database Variables
    private DatabaseReference mDatabase;
    double totalCorrect;
    double totalWrong;
    private FirebaseUser user;
    private String userID;

    String[] begQuestions = {"a", "b", "c", "d", "e",
            "f", "g", "h", "i", "j",
            "k", "l", "m", "n", "o",
            "p", "q", "r", "s", "t",
            "u", "v", "w", "x", "y",
            "z", "1", "2", "3", "4",
            "5", "6", "7", "8", "9",
            "10", "and"};

    String[] InterQuestions = {
            "baby", "bathroom", "deaf", "father", "friend",
            "hello", "ily", "love", "mother", "no",
            "please", "school", "thanks", "yes",
    };

    int[] progressBars = new int[]{
            R.id.progress1, R.id.progress2, R.id.progress3, R.id.progress4, R.id.progress5,
            R.id.progress6, R.id.progress7, R.id.progress8, R.id.progress9, R.id.progress10,
            R.id.progress11, R.id.progress12, R.id.progress13, R.id.progress14, R.id.progress15,
            R.id.progress16, R.id.progress17, R.id.progress18, R.id.progress19, R.id.progress20,
            R.id.progress21, R.id.progress22, R.id.progress23, R.id.progress24, R.id.progress25,
            R.id.progress26, R.id.progress27, R.id.progress28, R.id.progress29, R.id.progress30,
            R.id.progress31, R.id.progress32, R.id.progress33, R.id.progress34, R.id.progress35,
            R.id.progress36, R.id.progress37
    };

    int[] textLetters = new int[]{
            R.id.textLetter1, R.id.textLetter2, R.id.textLetter3, R.id.textLetter4, R.id.textLetter5,
            R.id.textLetter6, R.id.textLetter7, R.id.textLetter8, R.id.textLetter9, R.id.textLetter10,
            R.id.textLetter11, R.id.textLetter12, R.id.textLetter13, R.id.textLetter14, R.id.textLetter15,
            R.id.textLetter16, R.id.textLetter17, R.id.textLetter18, R.id.textLetter19, R.id.textLetter20,
            R.id.textLetter21, R.id.textLetter22, R.id.textLetter23, R.id.textLetter24, R.id.textLetter25,
            R.id.textLetter26, R.id.textLetter27, R.id.textLetter28, R.id.textLetter29, R.id.textLetter30,
            R.id.textLetter31, R.id.textLetter32, R.id.textLetter33, R.id.textLetter34, R.id.textLetter35,
            R.id.textLetter36, R.id.textLetter37
    };

    final Handler handler = new Handler();

    int i;
    double sum, prog;
    //Binding
    private FragmentHomeBinding binding;

    //Progress
    ProgressBarAnimation anim;

    //Switch
    Switch modeSwitch;
    String getMode = "Beginner";

    //Learning Progress
    ProgressBar learnProgress;
    TextView learnProgTV;
    int masteredLetters = 0;
    double averageMastered;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //Database Variables
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        //Switch
        modeSwitch = root.findViewById(R.id.modeSwitch);

        //Learning Progress
        learnProgress = root.findViewById(R.id.learningProg);
        learnProgTV = root.findViewById(R.id.learnProgress);


        //Run Database on startup
        mDatabase.child(userID).child("Questions").child(getMode).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    if (activityVisible) {

                        masteredLetters = 0;
                        for (DataSnapshot postSnapshot : task.getResult().getChildren()) {
                            if (getMode.equalsIgnoreCase("beginner")) {
                                for (i = 0; i < begQuestions.length; i++) {
                                    String key = postSnapshot.getKey();
                                    String key2 = begQuestions[i];

                                    if (key.equalsIgnoreCase(key2)) {
                                        //TextView
                                        TextView textQ = root.findViewById(textLetters[i]);
                                        textQ.setText(begQuestions[i].toUpperCase(Locale.ROOT));

                                        //Progress
                                        for (DataSnapshot childSnapshot : postSnapshot.getChildren()) {
                                            String childKey = childSnapshot.getKey();
                                            if (childKey.equalsIgnoreCase("correct")) {
                                                String convertString = String.valueOf(childSnapshot.getValue());
                                                totalCorrect = Integer.parseInt(convertString);
                                            }
                                            if (childKey.equalsIgnoreCase("wrong")) {
                                                String convertString = String.valueOf(childSnapshot.getValue());
                                                totalWrong = Integer.parseInt(convertString);
                                            }
                                        }

                                        sum = totalCorrect + totalWrong;
                                        prog = totalCorrect / sum * 100.0;
                                        ProgressBar progressQ = root.findViewById(progressBars[i]);
                                        anim = new ProgressBarAnimation(progressQ, 0, (int) prog);
                                        anim.setDuration(2000);
                                        progressQ.startAnimation(anim);
                                        if ((int) prog >= 70) {
                                            masteredLetters++;
                                        }
                                    }
                                }
                            } else {
                                for (i = 0; i < InterQuestions.length; i++) {
                                    String key = postSnapshot.getKey();
                                    String key2 = InterQuestions[i];


                                    if (key.equalsIgnoreCase(key2)) {
                                        //TextView
                                        TextView textQ = root.findViewById(textLetters[i]);
                                        textQ.setText(InterQuestions[i].toUpperCase(Locale.ROOT));

                                        //Progress
                                        for (DataSnapshot childSnapshot : postSnapshot.getChildren()) {
                                            String childKey = childSnapshot.getKey();
                                            if (childKey.equalsIgnoreCase("correct")) {
                                                String convertString = String.valueOf(childSnapshot.getValue());
                                                totalCorrect = Integer.parseInt(convertString);
                                            }
                                            if (childKey.equalsIgnoreCase("wrong")) {
                                                String convertString = String.valueOf(childSnapshot.getValue());
                                                totalWrong = Integer.parseInt(convertString);
                                            }
                                        }

                                        sum = totalCorrect + totalWrong;
                                        prog = totalCorrect / sum * 100.0;
                                        ProgressBar progressQ = root.findViewById(progressBars[i]);
                                        anim = new ProgressBarAnimation(progressQ, 0, (int) prog);
                                        anim.setDuration(2000);
                                        progressQ.startAnimation(anim);
                                        if ((int) prog >= 70) {
                                            masteredLetters++;
                                        }
                                    }
                                }
                            }
                        }

                        if (getMode.equalsIgnoreCase("beginner")) {
                            for (i = 0; i < begQuestions.length; i++) {
                                TextView textQ = root.findViewById(textLetters[i]);
                                String text = String.valueOf(textQ.getText());
                                if (text.equalsIgnoreCase("")) {
                                    ((View) textQ.getParent()).setVisibility(View.GONE);
                                } else {
                                    ((View) textQ.getParent()).setVisibility(View.VISIBLE);
                                }
                            }
                            averageMastered = masteredLetters / (double) begQuestions.length * 100.0;
                            learnProgTV.setText(String.valueOf(masteredLetters) + "/" + String.valueOf(begQuestions.length));
                        } else {
                            for (i = 0; i < InterQuestions.length; i++) {
                                TextView textQ = root.findViewById(textLetters[i]);
                                String text = String.valueOf(textQ.getText());
                                if (text.equalsIgnoreCase("")) {
                                    ((View) textQ.getParent()).setVisibility(View.GONE);
                                } else {
                                    ((View) textQ.getParent()).setVisibility(View.VISIBLE);
                                }
                            }
                            averageMastered = masteredLetters / (double) InterQuestions.length * 100.0;
                            learnProgTV.setText(String.valueOf(masteredLetters) + "/" + String.valueOf(InterQuestions.length));
                        }

                        anim = new ProgressBarAnimation(learnProgress, 0, (int) averageMastered);
                        anim.setDuration(1000);
                        learnProgress.startAnimation(anim);
                    }
                }
            }
        });
        for (i = 0; i <= 36; i++) {
            TextView textQ = root.findViewById(textLetters[i]);
            textQ.setText("");
            ((View) textQ.getParent()).setVisibility(View.GONE);
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Show the Learn Stats
                modeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override

                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        for (i = 0; i <= 36; i++) {
                            TextView textQ = root.findViewById(textLetters[i]);
                            textQ.setText("");
                            ((View) textQ.getParent()).setVisibility(View.GONE);
                        }

                        if (b) {
                            getMode = "Intermediate";
                            modeSwitch.setText("Words");
                        } else {
                            getMode = "Beginner";
                            modeSwitch.setText("Letters");
                        }
                        mDatabase.child(userID).child("Questions").child(getMode).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (activityVisible) {

                                    masteredLetters = 0;
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        if (getMode.equalsIgnoreCase("beginner")) {
                                            for (i = 0; i < begQuestions.length; i++) {
                                                String key = postSnapshot.getKey();
                                                String key2 = begQuestions[i];

                                                if (key.equalsIgnoreCase(key2)) {
                                                    //TextView
                                                    TextView textQ = root.findViewById(textLetters[i]);
                                                    textQ.setText(begQuestions[i].toUpperCase(Locale.ROOT));

                                                    //Progress
                                                    for (DataSnapshot childSnapshot : postSnapshot.getChildren()) {
                                                        String childKey = childSnapshot.getKey();
                                                        if (childKey.equalsIgnoreCase("correct")) {
                                                            String convertString = String.valueOf(childSnapshot.getValue());
                                                            totalCorrect = Integer.parseInt(convertString);
                                                        }
                                                        if (childKey.equalsIgnoreCase("wrong")) {
                                                            String convertString = String.valueOf(childSnapshot.getValue());
                                                            totalWrong = Integer.parseInt(convertString);
                                                        }
                                                    }

                                                    sum = totalCorrect + totalWrong;
                                                    prog = totalCorrect / sum * 100.0;
                                                    ProgressBar progressQ = root.findViewById(progressBars[i]);
                                                    anim = new ProgressBarAnimation(progressQ, 0, (int) prog);
                                                    anim.setDuration(2000);
                                                    progressQ.startAnimation(anim);
                                                    if ((int) prog >= 70) {
                                                        masteredLetters++;
                                                    }
                                                }
                                            }
                                        } else {
                                            for (i = 0; i < InterQuestions.length; i++) {
                                                String key = postSnapshot.getKey();
                                                String key2 = InterQuestions[i];


                                                if (key.equalsIgnoreCase(key2)) {
                                                    //TextView
                                                    TextView textQ = root.findViewById(textLetters[i]);
                                                    textQ.setText(InterQuestions[i].toUpperCase(Locale.ROOT));

                                                    //Progress
                                                    for (DataSnapshot childSnapshot : postSnapshot.getChildren()) {
                                                        String childKey = childSnapshot.getKey();
                                                        if (childKey.equalsIgnoreCase("correct")) {
                                                            String convertString = String.valueOf(childSnapshot.getValue());
                                                            totalCorrect = Integer.parseInt(convertString);
                                                        }
                                                        if (childKey.equalsIgnoreCase("wrong")) {
                                                            String convertString = String.valueOf(childSnapshot.getValue());
                                                            totalWrong = Integer.parseInt(convertString);
                                                        }
                                                    }

                                                    sum = totalCorrect + totalWrong;
                                                    prog = totalCorrect / sum * 100.0;
                                                    ProgressBar progressQ = root.findViewById(progressBars[i]);
                                                    anim = new ProgressBarAnimation(progressQ, 0, (int) prog);
                                                    anim.setDuration(2000);
                                                    progressQ.startAnimation(anim);
                                                    if ((int) prog >= 70) {
                                                        masteredLetters++;
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    if (getMode.equalsIgnoreCase("beginner")) {
                                        for (i = 0; i < begQuestions.length; i++) {
                                            TextView textQ = root.findViewById(textLetters[i]);
                                            String text = String.valueOf(textQ.getText());
                                            if (text.equalsIgnoreCase("")) {
                                                ((View) textQ.getParent()).setVisibility(View.GONE);
                                            } else {
                                                ((View) textQ.getParent()).setVisibility(View.VISIBLE);
                                            }
                                        }
                                        averageMastered = masteredLetters / (double) begQuestions.length * 100.0;
                                        learnProgTV.setText(String.valueOf(masteredLetters) + "/" + String.valueOf(begQuestions.length));
                                    } else {
                                        for (i = 0; i < InterQuestions.length; i++) {
                                            TextView textQ = root.findViewById(textLetters[i]);
                                            String text = String.valueOf(textQ.getText());
                                            if (text.equalsIgnoreCase("")) {
                                                ((View) textQ.getParent()).setVisibility(View.GONE);
                                            } else {
                                                ((View) textQ.getParent()).setVisibility(View.VISIBLE);
                                            }
                                        }
                                        averageMastered = masteredLetters / (double) InterQuestions.length * 100.0;
                                        learnProgTV.setText(String.valueOf(masteredLetters) + "/" + String.valueOf(InterQuestions.length));
                                    }

                                    anim = new ProgressBarAnimation(learnProgress, 0, (int) averageMastered);
                                    anim.setDuration(1000);
                                    learnProgress.startAnimation(anim);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(root.getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }, 500);

        return root;
    }

    public class ProgressBarAnimation extends Animation {
        private ProgressBar progressBar;
        private float from;
        private float to;

        public ProgressBarAnimation(ProgressBar progressBar, float from, float to) {
            super();
            this.progressBar = progressBar;
            this.from = from;
            this.to = to;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            float value = from + (to - from) * interpolatedTime;
            progressBar.setProgress((int) value);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}