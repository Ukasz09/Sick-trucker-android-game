package com.github.ukasz09.player;

import com.badlogic.gdx.Gdx;
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

public class Player {
    public static final float PLAYER_WIDTH_METERS = 3, PLAYER_HEIGHT_METERS = 2;
    private static final int MOVE_FRAMES_COLUMNS = 5, IDLE_FRAME_COLUMNS = 3, FRAME_ROWS = 1;
    private static final float PLAYER_DENSITY = 1.0f;
    public static final float MOVING_FORCE = 11f;
    public static final String MOVING_SHEET_PATH = "sheets/monster-move.png";
    public static final String IDLE_SHEET_PATH = "sheets/monster-idle.png";
    private static final float PLAYER_START_X = 60f;
    private static final float PLAYER_START_Y = 100f;
    private static final float IDLE_FRAME_DURATION = 0.045f, MOVING_FRAME_DURATION = 0.03f;
    public static final float GAS_PRESSED_SOUND_VOLUME = 0.7f;
    public static final float SOUND_VOLUME = 1f;

    public static String nick;
    public static String logoPath;

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
    private Sound idleEngineSound;
    private Sound gasPressedEngineSound;
    private Sound backwardEngineSound;
    private Sound startEngineSound;
    public boolean backwardSoundIsPlaying = false;
    public boolean gasPressedSoundIsPlaying = false;
    public boolean isGoingBackwards = false;


    public Player(World world, float pixelPerMeter) {
        this.widthPx = PLAYER_WIDTH_METERS * pixelPerMeter;
        this.heightPx = PLAYER_HEIGHT_METERS * pixelPerMeter;
        createBoxBody(world);
        lastPositionX = getBody().getPosition().x;
        createAnimation();
        this.initSounds();
        playStartEngineSound(SOUND_VOLUME);
        playIdleEngineSound(SOUND_VOLUME);
    }

    private void playStartEngineSound(float volume) {
        Timer x = new Timer();
        x.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                startEngineSound.play(volume);
            }
        }, 0.2f);
    }

    public void playBackwardEngineSound(float volume) {
        backwardSoundIsPlaying = true;
        Timer x = new Timer();
        x.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                backwardEngineSound.loop(volume);
            }
        }, 0.1f);
    }

    public void playIdleEngineSound(float volume) {
        Timer x = new Timer();
        x.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                idleEngineSound.loop(volume);
            }
        }, 0.2f);
    }

    public void stopIdleEngineSound() {
        idleEngineSound.stop();
    }

    public void stopBackwardEngineSound() {
        backwardSoundIsPlaying = false;
        backwardEngineSound.stop();
    }

    public void playGasPressedEngineSound(float volume) {
        gasPressedSoundIsPlaying = true;
        Timer x = new Timer();
        x.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                gasPressedEngineSound.loop(volume);
            }
        }, 0.1f);
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

    private void initSounds() {
        startEngineSound = Gdx.audio.newSound(Gdx.files.internal("sounds/engine_start.wav"));
        idleEngineSound = Gdx.audio.newSound(Gdx.files.internal("sounds/engine_idle.wav"));
        gasPressedEngineSound = Gdx.audio.newSound(Gdx.files.internal("sounds/engine_pedal_pressed.wav"));
        backwardEngineSound = Gdx.audio.newSound(Gdx.files.internal("sounds/engine_backward.wav"));
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
        idleEngineSound.dispose();
        backwardEngineSound.dispose();
    }

    public void stopSounds() {
        stopGasPressedEngineSound();
        stopIdleEngineSound();
        stopBackwardEngineSound();
        startEngineSound.stop();
    }
}
