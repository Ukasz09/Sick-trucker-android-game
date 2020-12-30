package com.github.ukasz09;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.github.ukasz09.player.Player;

public class Hud {
    private static final float PLAYER_LOGO_SIZE = 40f;
    private static final float HUD_PADDING = 10f;
    private static final float TIME_LABEL_WIDTH = 150f;
    public static final String TIME_UNIT = "ms";

    private BitmapFont font = new BitmapFont();
    private Batch batch;
    private OrthographicCamera camera;
    private Texture playerLogoTexture;
    public static float timeMs = 0;

    public Hud(Batch batch, OrthographicCamera camera) {
        this.batch = batch;
        this.camera = camera;
        initLogoTexture();
        timeMs = 0;
    }

    private void initLogoTexture() {
        String playerLogoPrefix = "player_logo/";
        String playerLogoExt = ".png";
        String playerLogoFullPath;
        playerLogoFullPath = playerLogoPrefix + Player.logoPath + playerLogoExt;
        playerLogoTexture = new Texture(playerLogoFullPath);
    }

    public void render() {
        drawUserLogo();
        drawUserNick();
        drawTime();
        drawFPS();
//        drawHomeBtn(); //TODO:
    }

    private void drawUserLogo() {
        float posX = getLogoPositionX();
        float posY = getLogoPositionY();
        batch.draw(playerLogoTexture, posX, posY, PLAYER_LOGO_SIZE, PLAYER_LOGO_SIZE);
    }

    private float getLogoPositionX() {
        return camera.position.x - camera.viewportWidth / 2 + HUD_PADDING;
    }

    private float getLogoPositionY() {
        return camera.position.y + camera.viewportHeight / 2 - PLAYER_LOGO_SIZE - HUD_PADDING;
    }

    private void drawUserNick() {
        float posX = getLogoPositionX();
        float posY = getLogoPositionY() - HUD_PADDING;
        font.draw(batch, Player.nick, posX, posY);
    }

    private void drawTime() {
        float posX = getTimePosX();
        float posY = getTimePosY();
        String timeText = "Time: " + timeMs + " ms";
        font.draw(batch, timeText, posX, posY);
    }

    private float getTimePosX() {
        return camera.position.x + camera.viewportWidth / 2 - TIME_LABEL_WIDTH;
    }

    private float getTimePosY() {
        return camera.position.y + camera.viewportHeight / 2 - HUD_PADDING;
    }

    private void drawFPS() {
        float posX = getTimePosX();
        float posY = getTimePosY() - HUD_PADDING * 2;
        String text = "FPS: " + Gdx.graphics.getFramesPerSecond();
        font.draw(batch, text, posX, posY);
    }

    public void update() {
        timeMs += Gdx.graphics.getDeltaTime();
    }

    public void resetTimer() {
        timeMs = 0;
    }
}
