package com.github.ukasz09;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class Coin {
    private static final float DENSITY = 1.0f;
    private Body body;
    private boolean isCollected = false;

    public Coin(World world, Shape shape) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bodyDef);
        body.createFixture(shape, DENSITY).setUserData(this);
        shape.dispose();
    }

    public Body getBody() {
        return body;
    }

    public void setCollected(boolean value) {
        this.isCollected = value;
    }

    public boolean isCollected() {
        return isCollected;
    }
}