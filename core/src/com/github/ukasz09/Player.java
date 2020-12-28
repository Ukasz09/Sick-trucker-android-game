package com.github.ukasz09;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Player {
    private static final float PLAYER_DENSITY = 1.0f;
    public static final float JUMP_FORCE = 500f;
    public static final float RUN_FORCE = 10f;
    public static final String PLAYER_IMG_PATH = "test.png";
    private static final float PLAYER_START_X = 60f;
    private static final float PLAYER_START_Y = 100f;

    private Body body;
    private float width_px;
    private float height_px;
    private float pixelPerMeter;

    public Player(World world, float width_px, float height_px, float pixelPerMeter) {
        this.width_px = width_px;
        this.height_px = height_px;
        this.pixelPerMeter = pixelPerMeter;
        createBoxBody(world, PLAYER_START_X, PLAYER_START_Y, this.width_px, this.height_px);
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
    }

    public Body getBody() {
        return body;
    }

    public float getWidth_px() {
        return width_px;
    }

    public float getHeight_px() {
        return height_px;
    }

    private boolean isJumping = false;
    private boolean isDead = false;


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

}
