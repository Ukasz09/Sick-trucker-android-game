package com.github.ukasz09.activities;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.ukasz09.Hud;
import com.github.ukasz09.R;
import com.github.ukasz09.data.Endpoints;
import com.github.ukasz09.fragments.MenuStart;
import com.github.ukasz09.player.Player;

import java.util.HashMap;
import java.util.Map;

public class GameWonPage extends Activity {
    private RequestQueue mRequestQueue;
    private MediaPlayer applauseSoundPlayer;

    private TextView errTextView;
    private TextView successTextView;
    private Button tryAgainSaveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestQueue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_game_won_page);
        initApplauseEffect();
        applauseSoundPlayer.start();
        setTimeResult();
        putRankingData();
        initConnectionStatusElem();
        hideConnectionStatusElem();
    }

    private void initConnectionStatusElem() {
        errTextView = findViewById(R.id.errorDataSavingText);
        successTextView = findViewById(R.id.successfulDataSavingText);
        tryAgainSaveBtn = findViewById(R.id.tryAgainSaveResultBtn);
    }

    private void hideConnectionStatusElem() {
        errTextView.setVisibility(View.GONE);
        successTextView.setVisibility(View.GONE);
        tryAgainSaveBtn.setVisibility(View.GONE);
    }

    private void initApplauseEffect() {
        int rawId = R.raw.applause;
        applauseSoundPlayer = MediaPlayer.create(this, rawId);
        applauseSoundPlayer.setLooping(false);
    }

    public void setTimeResult() {
        TextView resultTime = findViewById(R.id.resultTimeLabel);
        String prefix = "Time: ";
        String timeText = prefix + Hud.timeSec + " " + Hud.TIME_UNIT;
        resultTime.setText(timeText);
    }

    public void goHome(View view) {
        final Intent intent = new Intent(this, MenuStart.class);
        startActivity(intent);
    }

    public void startGame(View view) {
        final Intent intent = new Intent(this, GameLauncher.class);
        startActivity(intent);
    }

    private void putRankingData() {
        StringRequest postRequest = new StringRequest(Request.Method.PUT, getRecordsEndpoint(),
                this::onSuccessfulDataUpdate,
                error -> onDataUpdateError(error.getMessage())
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("logoUrl", Player.logoPath);
                params.put("nick", Player.nick);
                params.put("time", String.valueOf(Hud.timeSec));
                return params;
            }
        };
        mRequestQueue.add(postRequest);
    }

    public void onTryAgainSaveBtnClick(View view) {
        putRankingData();
    }

    private void onDataUpdateError(String errMsg) {
        hideConnectionStatusElem();
        errTextView.setText(errMsg);
        errTextView.setVisibility(View.VISIBLE);
        tryAgainSaveBtn.setVisibility(View.VISIBLE);
    }

    private void onSuccessfulDataUpdate(String response) {
        hideConnectionStatusElem();
        successTextView.setText(response);
        successTextView.setVisibility(View.VISIBLE);
    }

    private static String getRecordsEndpoint() {
        return Endpoints.BACKEND_ENDPOINT + Endpoints.RANKING_ENDPOINT;
    }
}