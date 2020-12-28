package com.github.ukasz09.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class PlayerTruck {
    public static final float PLAYER_WIDTH_METERS = 3, PLAYER_HEIGHT_METERS = 2;
    private static final int MOVE_FRAMES_COLUMNS = 5, IDLE_FRAME_COLUMNS = 3, FRAME_ROWS = 1;
    private static final float PLAYER_DENSITY = 1.0f;
    public static final float MOVING_FORCE = 12f;
    public static final String MOVING_SHEET_PATH = "sheets/monster-move.png";
    public static final String IDLE_SHEET_PATH = "sheets/monster-idle.png";
    private static final float PLAYER_START_X = 60f;
    private static final float PLAYER_START_Y = 100f;
    private static final float IDLE_FRAME_DURATION = 0.025f, MOVING_FRAME_DURATION = 0.025f;

    private Animation<TextureRegion> moveAnimation, idleAnimation;
    private TextureRegion region;
    float stateTime;
    private Body body;
    private float widthPx;
    private float heightPx;
    private float lastPositionX;
    private boolean isDestroyed = false;
    private boolean isPressedGasPedal = false;
    private float rotationDegrees;

    public PlayerTruck(World world, float pixelPerMeter) {
        this.widthPx = PLAYER_WIDTH_METERS * pixelPerMeter;
        this.heightPx = PLAYER_HEIGHT_METERS * pixelPerMeter;
        createBoxBody(world);
        lastPositionX = getBody().getPosition().x;
        createAnimation();
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
        bdef.position.set(PlayerTruck.PLAYER_START_X, PlayerTruck.PLAYER_START_Y);
        return bdef;
    }

    private FixtureDef createFixtureDef() {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(PlayerTruck.PLAYER_WIDTH_METERS / 2, PlayerTruck.PLAYER_HEIGHT_METERS / 2);
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
}
