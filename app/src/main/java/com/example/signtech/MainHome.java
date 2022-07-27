package com.example.signtech;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.signtech.databinding.ActivityMainHomeBinding;

public class MainHome extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(MainHome.this, R.style.DialogStyle);
                dialog.setContentView(R.layout.difficulty_dialog);

                Button btnBeg = dialog.findViewById(R.id.btnBeg);
                Button btnInter = dialog.findViewById(R.id.btnInter);
                Button btnAdv = dialog.findViewById(R.id.btnAdv);
                dialog.show();

                btnBeg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Quiz.mode = "Beginner";

                        Intent quiz = new Intent(getApplicationContext(), Quiz.class);
                        startActivity(quiz);
                    }
                });

                btnInter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Quiz.mode = "Intermediate";

                        Intent quiz = new Intent(getApplicationContext(), Quiz.class);
                        startActivity(quiz);
                    }
                });

                btnAdv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainHome.this, "Coming Soon!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_learn, R.id.nav_home, R.id.nav_sign, R.id.nav_profile, R.id.nav_achievement)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public static boolean activityVisible = true;

    public static void activityResumed() {
        activityVisible = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityResumed();
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityPaused();
    }
}