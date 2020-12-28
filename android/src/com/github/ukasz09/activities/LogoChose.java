package com.github.ukasz09.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.github.ukasz09.R;
import com.github.ukasz09.player.Player;

public class LogoChose extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_chose);
    }

    public void onLogoClick(View view) {
        onLogoClick(view.getTag().toString());
    }

    private void onLogoClick(String logoPath) {
        Player.logoPath = logoPath;
        final Intent intent = new Intent(this, Game.class);
        startActivity(intent);
    }
}