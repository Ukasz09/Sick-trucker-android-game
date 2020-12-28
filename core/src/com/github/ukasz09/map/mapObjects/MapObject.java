package com.github.ukasz09.map.mapObjects;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public abstract class MapObject {
    public MapObject(World world, Shape shape, BodyDef.BodyType bodyType, float density) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        Body body = world.createBody(bodyDef);
        body.createFixture(shape, density).setUserData(this);
        shape.dispose();
    }
}
