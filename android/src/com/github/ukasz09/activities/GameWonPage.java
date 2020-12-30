package com.github.ukasz09.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestQueue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_game_won_page);
        setTimeResult();
        putRankingData();
    }

    public void setTimeResult() {
        TextView resultTime = findViewById(R.id.resultTimeLabel);
        String prefix = "Time: ";
        String timeText = prefix + Hud.timeMs + " " + Hud.TIME_UNIT;
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
                response -> {
                    // response
                    System.out.println(response);
                },
                error -> {
                    // error
                    System.err.println("ER");
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("logoUrl", Player.logoPath);
                params.put("nick", Player.nick);
                params.put("time", String.valueOf(Hud.timeMs));
                return params;
            }
        };
        mRequestQueue.add(postRequest);
    }

    private static String getRecordsEndpoint() {
        String endpoint = Endpoints.BACKEND_ENDPOINT + Endpoints.RANKING_ENDPOINT;
        return endpoint;
    }
}