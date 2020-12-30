package com.github.ukasz09;

import android.content.Context;
import android.content.Intent;

import com.github.ukasz09.activities.GameWonPage;

/**
 * Bridge to connect core with android activities
 */
public class GameWonPageBridge implements ActivityBridge {
    private Context context;


    public GameWonPageBridge(Context context) {
        this.context = context;
    }

    @Override
    public void showActivity() {
        final Intent intent = new Intent(context, GameWonPage.class);
        context.startActivity(intent);
    }
}
