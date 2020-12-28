package com.github.ukasz09;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
        UserData.logoPath = logoPath;
        final Intent intent = new Intent(this, Game.class);
        startActivity(intent);
    }
}