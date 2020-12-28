package com.github.ukasz09.map.mapObjects;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public abstract class StaticMapObject extends MapObject {
    private static final float DENSITY = 0.0f;

    public StaticMapObject(World world, Shape shape) {
        super(world, shape, BodyDef.BodyType.StaticBody, DENSITY);
    }
}
