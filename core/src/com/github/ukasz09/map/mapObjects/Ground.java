package com.github.ukasz09.map.mapObjects;

import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class Ground extends StaticMapObject {
    public Ground(World world, Shape shape) {
        super(world, shape);
    }
}
