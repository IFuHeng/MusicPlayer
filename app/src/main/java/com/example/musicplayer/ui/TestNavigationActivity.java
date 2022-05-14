package com.example.musicplayer.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.musicplayer.R;
import com.example.musicplayer.databinding.ActivityTestNavigationBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TestNavigationActivity extends AppCompatActivity {

    private ActivityTestNavigationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTestNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        {
            BottomNavigationView navView = findViewById(R.id.nav_view);
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                    .build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_test_navigation);
//            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(binding.navView, navController);
        }
        {
//            BottomNavigationView navView123 = findViewById(R.id.nav_view_123);
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            AppBarConfiguration appBarConfiguration123 = new AppBarConfiguration.Builder(
                    R.id.navigation_1, R.id.navigation_2, R.id.navigation_3)
                    .build();
            NavController navController123 = Navigation.findNavController(this, R.id.nav_host_fragment_123_navigation);
//            NavigationUI.setupActionBarWithNavController(this, navController123, appBarConfiguration123);
            NavigationUI.setupWithNavController(binding.navView123, navController123);
        }
    }

}