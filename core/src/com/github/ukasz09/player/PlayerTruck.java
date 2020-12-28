package com.github.ukasz09.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class PlayerTruck {
    public static final float PLAYER_WIDTH_METERS = 3;
    public static final float PLAYER_HEIGHT_METERS = 2;
    private static final float PLAYER_DENSITY = 1.0f;
    public static final float JUMP_FORCE = 500f;
    public static final float RUN_FORCE = 15f;
    public static final String PLAYER_IMG_PATH = "sheets/monster-move.png";
    public static final String PLAYER_IDLE_IMG_PATH = "sheets/monster-idle.png";

    private static final int FRAME_MOVE_COLS = 5, FRAME_IDLE_COLS = 3, FRAME_ROWS = 1;
    Animation<TextureRegion> moveAnimation;
    Animation<TextureRegion> idleAnimation;
    Texture walkSheet;
    Texture idleSheet;
    private static final float PLAYER_START_X = 60f;
    private static final float PLAYER_START_Y = 100f;
    public TextureRegion region;
    float stateTime;

    private Body body;
    private float width_px; //TODO: get rid of
    private float height_px; //TODO: get rid of
    private float pixelPerMeter;

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    private float rotation;


    public PlayerTruck(World world, float pixelPerMeter) {
        this.width_px = PLAYER_WIDTH_METERS * pixelPerMeter;
        this.height_px = PLAYER_HEIGHT_METERS * pixelPerMeter;
        this.pixelPerMeter = pixelPerMeter;
        createBoxBody(world, PLAYER_START_X, PLAYER_START_Y, this.width_px, this.height_px);
        createAnimation();
    }

    private void createAnimation() {
        walkSheet = new Texture(Gdx.files.internal(PLAYER_IMG_PATH));

        TextureRegion[][] tmp = TextureRegion.split(walkSheet,
                walkSheet.getWidth() / FRAME_MOVE_COLS,
                walkSheet.getHeight() / FRAME_ROWS);

        TextureRegion[] walkFrames = new TextureRegion[FRAME_MOVE_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_MOVE_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }

        idleSheet = new Texture(Gdx.files.internal(PLAYER_IDLE_IMG_PATH));

        TextureRegion[][] tmp2 = TextureRegion.split(idleSheet,
                idleSheet.getWidth() / FRAME_IDLE_COLS,
                idleSheet.getHeight() / FRAME_ROWS);

        TextureRegion[] idleFrames = new TextureRegion[FRAME_IDLE_COLS * FRAME_ROWS];
        index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_IDLE_COLS; j++) {
                idleFrames[index++] = tmp2[i][j];
            }
        }

        moveAnimation = new Animation<>(0.025f, walkFrames);
        idleAnimation = new Animation<>(0.025f, idleFrames);
        stateTime = 0f;
        region = idleAnimation.getKeyFrame(0);
    }

    private void createBoxBody(World world, float x, float y, float width_px, float height_px) {
        BodyDef bdef = new BodyDef();
        bdef.fixedRotation = true;
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(x, y);
        PolygonShape shape = new PolygonShape();

        shape.setAsBox(width_px / pixelPerMeter / 2, height_px / pixelPerMeter / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = PLAYER_DENSITY;
        body = world.createBody(bdef);
        body.createFixture(fixtureDef).setUserData(this);
        body.setMassData(new MassData());
    }

    public Body getBody() {
        return body;
    }

    public float getWidthPx() {
        return width_px;
    }

    public float getHeightPx() {
        return height_px;
    }

    private boolean isJumping = false;
    private boolean isDead = false;
    private boolean isMoving = false;

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public void hit() {
        isDead = true;
    }

    public void setJumping(boolean jumping) {
        isJumping = jumping;
    }

    public boolean isJumping() {
        return isJumping;
    }

    public boolean isDead() {
        return isDead;
    }


    public void act(float delta) {
        stateTime += delta;
        if (isMoving)
            region = moveAnimation.getKeyFrame(stateTime, true);
        else region = idleAnimation.getKeyFrame(stateTime, true);
    }
}
