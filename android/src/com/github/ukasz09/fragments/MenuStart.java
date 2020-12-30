package com.github.ukasz09.fragments;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.github.ukasz09.R;
import com.github.ukasz09.activities.NickChose;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MenuStart extends FragmentActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView navigation;
    private static MediaPlayer backgroundMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_start);
        observeNavigationView();
        loadFragment(new HomeFragment());
        initBackgroundMediaPlayer();
        playBackgroundSound();
    }

    private void initBackgroundMediaPlayer() {
        int rawId = R.raw.background;
        backgroundMediaPlayer = MediaPlayer.create(this, rawId);
        backgroundMediaPlayer.setLooping(true);
    }

    public static void playBackgroundSound() {
        backgroundMediaPlayer.start();
    }

    public static void stopBackgroundSound() {
        backgroundMediaPlayer.stop();
    }

    private void observeNavigationView() {
        navigation = findViewById(R.id.mainNavigation);
        navigation.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return replaceActivity(item.getItemId());
    }

    private boolean replaceActivity(int itemId) {
        Fragment fragment;
        switch (itemId) {
            case R.id.homeMenuItem:
                fragment = new HomeFragment();
                loadFragment(fragment);
                return true;
            case R.id.rankingMenuItem: {
                fragment = new RankingFragment();
                loadFragment(fragment);
                return true;
            }
            case R.id.instructionMenuItem: {
                fragment = new InstructionFragment();
                loadFragment(fragment);
                return true;
            }
            case R.id.aboutMenuItem: {
                fragment = new AboutFragment();
                loadFragment(fragment);
                return true;
            }
            case R.id.exitMenuItem: {
                fragment = new ExitFragment();
                loadFragment(fragment);
                return true;
            }
        }
        return false;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void closeApp(View view) {
        finish();
    }

    public void replaceFragmentToHome(View view) {
        navigation.setSelectedItemId(R.id.homeMenuItem);
    }

    public void openNickChooseActivity(View view) {
        final Intent intent = new Intent(this, NickChose.class);
        startActivity(intent);
    }
}