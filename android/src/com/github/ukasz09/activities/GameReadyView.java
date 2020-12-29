package com.github.ukasz09.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ukasz09.fragments.MenuStart;
import com.github.ukasz09.R;
import com.github.ukasz09.player.Player;

public class GameReadyView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_ready_view);
        setUserNickAndLogoView();
    }

    private void setUserNickAndLogoView() {
        ImageView logo = findViewById(R.id.chosenUserLogo);
        final int logoResourceId = getResources().getIdentifier(Player.logoPath, "drawable", getPackageName());
        logo.setImageResource(logoResourceId);
        TextView nick = findViewById(R.id.chosenUserNick);
        nick.setText(Player.nick);
    }

    public void goHome(View view) {
        final Intent intent = new Intent(this, MenuStart.class);
        startActivity(intent);
    }

    public void startGame(View view) {
        final Intent intent = new Intent(this, GameLauncher.class);
        startActivity(intent);
    }
}