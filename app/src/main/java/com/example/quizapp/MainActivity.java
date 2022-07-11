package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.assist.AssistStructure;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //Questions Variables
    int[] images = new int[]{R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d,
            R.drawable.e, R.drawable.f, R.drawable.g, R.drawable.h, R.drawable.i,
            R.drawable.j, R.drawable.k, R.drawable.l, R.drawable.m, R.drawable.n,
            R.drawable.o, R.drawable.p, R.drawable.q, R.drawable.r, R.drawable.s,
            R.drawable.t, R.drawable.u, R.drawable.v, R.drawable.w, R.drawable.x,
            R.drawable.y, R.drawable.z, R.drawable.one, R.drawable.two, R.drawable.three,
            R.drawable.four, R.drawable.five, R.drawable.six, R.drawable.seven, R.drawable.eight,
            R.drawable.nine, R.drawable.ten, R.drawable.and, };
    String[] questions = {"a", "b", "c", "d", "e",
            "f", "g", "h", "i", "j",
            "k", "l", "m", "n", "o",
            "p", "q", "r", "s", "t",
            "u", "v", "w", "x", "y",
            "z", "1", "2", "3", "4",
            "5", "6", "7", "8", "9",
            "10", "and"};

    static String answer;
    int arrayNo;
    int currentLives = 10;
    int currentScore = 0;
    Button next, endQ;
    EditText userAnswer;
    TextView userScore, userLives;
    ImageView question, settings;
    ScrollView settingsContainer;
    //Anti crash (Keyboard thing)
    boolean openedK = false;
    //Settings
    boolean settingsCon = false;
    static CheckBox muteBgMusic, muteSoundEf;

    //StopWatch Variables
    final Handler handler = new Handler();
    TextView start, timer;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0;
    int Seconds, Minutes, MilliSeconds, Hours;

    //Music Variables
    static MediaPlayer backgroundMusic;

    //Database Variables
    private DatabaseReference mDatabase;
    static String letterToAdd;
    static int convertLetter;
    boolean isAvailable;

    //Animations
    RotateAnimation rotateAnimSettings;
    RotateAnimation rotateAnimBtns;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        //Database Variables
        mDatabase = FirebaseDatabase.getInstance().getReference();
        isAvailable = false;

        //Questions Variables
        next = findViewById(R.id.nextBtn);
        endQ = findViewById(R.id.endBtn);
        userAnswer = findViewById(R.id.userAnswer);
        userScore =  findViewById(R.id.currentScore);
        userLives =  findViewById(R.id.currentLives);
        question = findViewById(R.id.question);

        //Settings
        settings = findViewById(R.id.settingsBtn);
        settingsContainer = findViewById(R.id.settingsContainer);
        muteBgMusic =  findViewById(R.id.muteMusic);
        muteSoundEf = findViewById(R.id.muteSound);

        //Animations
        next.animate().alpha(0f).setDuration(0);
        endQ.animate().alpha(0f).setDuration(0);

        rotateAnimSettings = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimSettings.setDuration(3500);
        rotateAnimSettings.setRepeatCount(Animation.INFINITE);

        settings.startAnimation(rotateAnimSettings);

        rotateAnimBtns = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimBtns.setDuration(1500);

        rotateAnimBtns.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                next.setClickable(false);
                endQ.setClickable(false);
                start.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                next.setClickable(true);
                endQ.setClickable(true);
                start.setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        AlphaAnimation fadeIn = new AlphaAnimation(0f, 1f);
        AlphaAnimation fadeOut = new AlphaAnimation(1f, 0f);

        fadeIn.setDuration(500);
        fadeOut.setDuration(500);

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                settings.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                settings.setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                settings.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                settings.setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //Music Variables
        final MediaPlayer correctSound = MediaPlayer.create(this, R.raw.correctsound);
        final MediaPlayer wrongSound = MediaPlayer.create(this, R.raw.wrongsound);
        backgroundMusic = MediaPlayer.create(this, R.raw.backgroundmusic);
        playMusic();

        findViewById(R.id.backgroundLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                settingsCon = settingsContainer.isShown();
                if (settingsCon) {
                    settingsContainer.setVisibility(View.INVISIBLE);
                }
                openedK = userAnswer.hasFocus();
                hideKeyboard();
                return true;
            }
        });

        //Settings
        muteBgMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (muteBgMusic.isChecked()) {
                    backgroundMusic.pause();
                } else {
                    backgroundMusic.start();
                    backgroundMusic.seekTo(0L, MediaPlayer.SEEK_NEXT_SYNC);
                }
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsCon = settingsContainer.isShown();
                if (settingsCon) {
                    settingsContainer.startAnimation(fadeOut);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            settingsContainer.setVisibility(View.INVISIBLE);
                        }
                    }, 500);

                } else {
                    settingsContainer.startAnimation(fadeIn);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            settingsContainer.setVisibility(View.VISIBLE);
                        }
                    }, 500);
                }
            }
        });

        //Questions
        userAnswer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                settingsCon = settingsContainer.isShown();
                if (settingsCon) {
                    settingsContainer.setVisibility(View.INVISIBLE);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAvailable = false;
                openedK = userAnswer.hasFocus();

                hideKeyboard();
                if (!"".equals(String.valueOf(userAnswer.getText()))) {
                    if (answer.equalsIgnoreCase(String.valueOf(userAnswer.getText()))) {
                        //Check if the answer is exist in the database
                        mDatabase.child("Questions").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (!task.isSuccessful()) {
                                    Log.e("firebase", "Error getting data", task.getException());
                                } else {
                                    for (DataSnapshot snapshot : task.getResult().getChildren()) {
                                        String key = snapshot.getKey();
                                        if (key.equalsIgnoreCase(String.valueOf(userAnswer.getText()))) {
                                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                                String childKey = childSnapshot.getKey();
                                                if (childKey.equalsIgnoreCase("correct")) {
                                                    letterToAdd = String.valueOf(childSnapshot.getValue());
                                                    convertLetter = Integer.parseInt(letterToAdd) + 1;
                                                    mDatabase.child("Questions").child(String.valueOf(userAnswer.getText())).child("correct").setValue(String.valueOf(convertLetter));

                                                    isAvailable = true;
                                                }
                                            }
                                        }
                                    }
                                }
                                if (!isAvailable) {
                                    String key = String.valueOf(userAnswer.getText());
                                    Map<String, Object> values = new HashMap<>();
                                    values.put("correct", "wrong");
                                    mDatabase.child("Questions").child(key).push();
                                    mDatabase.child("Questions").child(key).setValue(values);
                                    mDatabase.child("Questions").child(key).child("correct").setValue("1");
                                    mDatabase.child("Questions").child(key).child("wrong").setValue("0");
                                }
                            }
                        });

                        if (!muteSoundEf.isChecked()) {
                            if (correctSound.isPlaying()) {
                                correctSound.seekTo(0L, MediaPlayer.SEEK_NEXT_SYNC);
                            } else {
                                correctSound.start();
                            }
                        }
                        delayFunction("correct");
                    } else {
                        mDatabase.child("Questions").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (!task.isSuccessful()) {
                                    Log.e("firebase", "Error getting data", task.getException());
                                } else {
                                    for (DataSnapshot snapshot : task.getResult().getChildren()) {
                                        String key = snapshot.getKey();
                                        if (key.equalsIgnoreCase(String.valueOf(answer))) {
                                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                                String childKey = childSnapshot.getKey();
                                                if (childKey.equalsIgnoreCase("wrong")) {
                                                    letterToAdd = String.valueOf(childSnapshot.getValue());
                                                    convertLetter = Integer.parseInt(letterToAdd) + 1;
                                                    mDatabase.child("Questions").child(answer).child("wrong").setValue(String.valueOf(convertLetter));
                                                    isAvailable = true;
                                                }
                                            }
                                        }
                                    }
                                }
                                if (!isAvailable) {
                                    String key = String.valueOf(answer);
                                    Map<String, Object> values = new HashMap<>();
                                    values.put("correct", "wrong");
                                    mDatabase.child("Questions").child(key).push();
                                    mDatabase.child("Questions").child(key).setValue(values);
                                    mDatabase.child("Questions").child(key).child("correct").setValue("0");
                                    mDatabase.child("Questions").child(key).child("wrong").setValue("1");
                                }
                            }
                        });

                        if (!muteSoundEf.isChecked()) {
                            if (wrongSound.isPlaying()) {
                                wrongSound.seekTo(0L, MediaPlayer.SEEK_NEXT_SYNC);
                            } else {
                                wrongSound.start();
                            }
                        }
                        delayFunction("wrong");
                    }
                }
            }

        });

        //Stopwatch Function
        timer = findViewById(R.id.stopWatch);
        start = findViewById(R.id.startBtn);

        //Buttons function
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                start.animate().alpha(0f).setDuration(1500);
                endQ.animate().alpha(1f).setDuration(1500);
                next.animate().alpha(1f).setDuration(1500);

                start.startAnimation(rotateAnimBtns);
                next.startAnimation(rotateAnimBtns);
                endQ.startAnimation(rotateAnimBtns);

                openedK = userAnswer.hasFocus();
                changeQuestion();
                start.setEnabled(false);
                next.setEnabled(true);
                endQ.setEnabled(true);
                StartTime = SystemClock.uptimeMillis();
                handler.postDelayed(runnable, 0);

            }
        });

        endQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getScoreAndTime();

                start.animate().alpha(1f).setDuration(1500);
                endQ.animate().alpha(0f).setDuration(1500);
                next.animate().alpha(0f).setDuration(1500);

                start.startAnimation(rotateAnimBtns);
                next.startAnimation(rotateAnimBtns);
                endQ.startAnimation(rotateAnimBtns);

                openedK = userAnswer.hasFocus();
                hideKeyboard();
                ResetEverything();

            }
        });
    }

    //Detect if Screen is Active/Inactive
    private static boolean activityVisible;

    public static void activityResumed() {
        if (!muteBgMusic.isChecked()) {
            backgroundMusic.start();
            activityVisible = true;
        }
    }

    public static void activityPaused() {
        backgroundMusic.pause();
        activityVisible = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityPaused();
    }

    //Other Functions
    public void delayFunction(String checkAnswer) {
        next.setEnabled(false);
        endQ.setEnabled(false);
        userAnswer.setEnabled(false);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                userAnswer.setText("");
                userAnswer.setEnabled(true);
                next.setEnabled(true);
                endQ.setEnabled(true);
                if (checkAnswer.equalsIgnoreCase("correct")) {
                    currentScore = Integer.parseInt(userScore.getText().toString()) + 1;
                    userScore.setText(String.valueOf(currentScore));
                } else {
                    currentLives = Integer.parseInt(userLives.getText().toString()) - 1;
                    userLives.setText(String.valueOf(currentLives));
                }

                changeQuestion();
            }
        }, 500);
    }

    public void playMusic() {
        backgroundMusic.setLooping(true);
        backgroundMusic.start();
    }

    public void hideKeyboard() {
        if (openedK == true) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            userAnswer.clearFocus();
        }
    }

    public void ResetEverything() {

        userAnswer.clearFocus();

        question.setImageDrawable(getResources().getDrawable(R.drawable.empty));
        userAnswer.setText("");
        userLives.setText("10");
        userScore.setText("0");

        currentLives = 10;
        currentScore = 0;

        start.setEnabled(true);
        next.setEnabled(false);
        endQ.setEnabled(false);

        TimeBuff += MillisecondTime;
        handler.removeCallbacks(runnable);

        MillisecondTime = 0;
        StartTime = 0;
        TimeBuff = 0;
        UpdateTime = 0;
        Seconds = 0;
        Minutes = 0;
        MilliSeconds = 0;

        timer.setText("00:00:00");
    }

    //Timer Function
    public Runnable runnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime;

            Seconds = (int) (UpdateTime / 1000);

            Hours = Seconds / 3600;
            Minutes = Seconds / 60;
            Seconds = Seconds % 60;

            MilliSeconds = (int) (UpdateTime % 1000);

            timer.setText("" + String.format("%02d", Hours) + ":" + String.format("%02d", Minutes) + ":"
                    + String.format("%02d", Seconds));

            handler.postDelayed(this, 0);
        }

    };

    //Questions Function
    public void getScoreAndTime() {
        Toast.makeText(this, "GAME ENDED!", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Your Score: " + userScore.getText() + " | Playtime:" + timer.getText(), Toast.LENGTH_LONG).show();
    }

    public void changeQuestion() {
        arrayNo = new Random().nextInt(questions.length);
        question.setImageDrawable(getResources().getDrawable(images[arrayNo]));
        answer = questions[arrayNo];

        if (currentLives == 0) {
            getScoreAndTime();

            start.animate().alpha(1f).setDuration(1500);
            endQ.animate().alpha(0f).setDuration(1500);
            next.animate().alpha(0f).setDuration(1500);

            start.startAnimation(rotateAnimBtns);
            next.startAnimation(rotateAnimBtns);
            endQ.startAnimation(rotateAnimBtns);

            ResetEverything();
        }
    }

}
