package com.github.ukasz09.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.github.ukasz09.GameApp;

public class Player {
    public static final float PLAYER_WIDTH_METERS = 3, PLAYER_HEIGHT_METERS = 2;
    private static final int MOVE_FRAMES_COLUMNS = 5, IDLE_FRAME_COLUMNS = 3, FRAME_ROWS = 1;
    private static final float PLAYER_DENSITY = 1.0f;
    public static final float MOVING_FORCE = 13f;
    public static final String MOVING_SHEET_PATH = "sheets/monster-move.png";
    public static final String IDLE_SHEET_PATH = "sheets/monster-idle.png";
    private static final float PLAYER_START_X = 60f;
    private static final float PLAYER_START_Y = 100f;
    private static final float IDLE_FRAME_DURATION = 0.045f, MOVING_FRAME_DURATION = 0.03f;
    public static final float GAS_PRESSED_SOUND_VOLUME = 0.5f, BACKWARD_ENGINE_SOUND_VOLUME = 0.5f, IDLE_ENGINE_SOUND_VOLUME = 0.8f;

    public static String nick = "unknown";
    public static String logoPath = "user_logo_1";

    private Animation<TextureRegion> moveAnimation, idleAnimation;
    private TextureRegion region;
    float stateTime;
    private Body body;
    private float widthPx;
    private float heightPx;
    private float lastPositionX;
    private boolean isDestroyed = false;
    public boolean isPressedGasPedal = false;
    private float rotationDegrees;
    private Music idleEngineMusic;
    private Sound gasPressedEngineSound;
    private Sound backwardEngineSound;
    private Sound boomSound;
    private Sound startEngineSound;
    public boolean backwardSoundIsPlaying = false;
    public boolean gasPressedSoundIsPlaying = false;
    public boolean isGoingBackwards = false;
    public World world;

    public Player(World world) {
        this.world = world;
        createAnimation();
        this.initSoundsEffects();
        reset();
    }

    public void reset() {
        isDestroyed = false;
        isPressedGasPedal = false;
        isGoingBackwards = false;
        this.widthPx = PLAYER_WIDTH_METERS * GameApp.PIXEL_PER_METER;
        this.heightPx = PLAYER_HEIGHT_METERS * GameApp.PIXEL_PER_METER;
        createBoxBody(world);
        lastPositionX = getBody().getPosition().x;
        playStartEngineSound();
        idleEngineMusic.play();
    }

    private void playStartEngineSound() {
        Timer x = new Timer();
        x.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                startEngineSound.play();
            }
        }, 0.2f);
    }

    public void playBackwardEngineSound() {
        backwardSoundIsPlaying = true;
        backwardEngineSound.loop(BACKWARD_ENGINE_SOUND_VOLUME);
    }

    public void playBoomSound() {
        boomSound.play();
    }

    public void stopIdleEngineMusic() {
        idleEngineMusic.stop();
    }

    public void stopBackwardEngineSound() {
        backwardSoundIsPlaying = false;
        backwardEngineSound.stop();
    }

    public void playGasPressedEngineSound() {
        gasPressedSoundIsPlaying = true;
        gasPressedEngineSound.loop(Player.GAS_PRESSED_SOUND_VOLUME);
    }

    public void stopGasPressedEngineSound() {
        gasPressedSoundIsPlaying = false;
        gasPressedEngineSound.stop();
    }

    private void createBoxBody(World world) {
        FixtureDef fixtureDef = createFixtureDef();
        BodyDef bodyDef = createBodyDef();
        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef).setUserData(this);
    }

    private BodyDef createBodyDef() {
        BodyDef bdef = new BodyDef();
        bdef.fixedRotation = true;
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(Player.PLAYER_START_X, Player.PLAYER_START_Y);
        return bdef;
    }

    private FixtureDef createFixtureDef() {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Player.PLAYER_WIDTH_METERS / 2, Player.PLAYER_HEIGHT_METERS / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = PLAYER_DENSITY;
        return fixtureDef;
    }

    private void createAnimation() {
        TextureRegion[] walkFrames = createFrames(MOVING_SHEET_PATH, MOVE_FRAMES_COLUMNS, FRAME_ROWS);
        TextureRegion[] idleFrames = createFrames(IDLE_SHEET_PATH, IDLE_FRAME_COLUMNS, FRAME_ROWS);
        moveAnimation = new Animation<>(MOVING_FRAME_DURATION, walkFrames);
        idleAnimation = new Animation<>(IDLE_FRAME_DURATION, idleFrames);
        stateTime = 0f;
        region = idleAnimation.getKeyFrame(0);
    }

    private TextureRegion[] createFrames(String sheetPath, int frameColumns, int frameRows) {
        Texture sheet = new Texture(Gdx.files.internal(sheetPath));
        TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth() / frameColumns, sheet.getHeight() / frameRows);
        TextureRegion[] frames = new TextureRegion[frameColumns * frameRows];
        int index = 0;
        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameColumns; j++) {
                frames[index++] = tmp[i][j];
            }
        }
        return frames;
    }

    private void initSoundsEffects() {
        startEngineSound = Gdx.audio.newSound(Gdx.files.internal("sounds/engine_start.wav"));
        idleEngineMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/engine_idle.wav"));
        idleEngineMusic.setVolume(IDLE_ENGINE_SOUND_VOLUME);
        idleEngineMusic.setLooping(true);
        gasPressedEngineSound = Gdx.audio.newSound(Gdx.files.internal("sounds/engine_pedal_pressed.wav"));
        backwardEngineSound = Gdx.audio.newSound(Gdx.files.internal("sounds/engine_backward.wav"));
        boomSound = Gdx.audio.newSound(Gdx.files.internal("sounds/boom.wav"));
    }

    public float getRotationDegrees() {
        return rotationDegrees;
    }

    public void setRotationDegrees(float rotationDegrees) {
        this.rotationDegrees = rotationDegrees;
    }

    public Body getBody() {
        return body;
    }

    public float getWidthPx() {
        return widthPx;
    }

    public float getHeightPx() {
        return heightPx;
    }

    public void setPressedGasPedal(boolean isPressed) {
        isPressedGasPedal = isPressed;
    }

    public void destroy() {
        isDestroyed = true;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public void updateAnimation(float delta) {
        stateTime += delta;
        if (isMovingHorizontally() || isPressedGasPedal)
            region = moveAnimation.getKeyFrame(stateTime, true);
        else region = idleAnimation.getKeyFrame(stateTime, true);
        lastPositionX = getBody().getPosition().x;
    }

    public boolean isMovingHorizontally() {
        return lastPositionX != getBody().getPosition().x;
    }

    public TextureRegion getRegion() {
        return region;
    }

    public void dispose() {
        startEngineSound.dispose();
        gasPressedEngineSound.dispose();
        idleEngineMusic.dispose();
        backwardEngineSound.dispose();
    }

    public void stopSoundsEffects() {
        stopGasPressedEngineSound();
        stopIdleEngineMusic();
        stopBackwardEngineSound();
        startEngineSound.stop();
    }
}
