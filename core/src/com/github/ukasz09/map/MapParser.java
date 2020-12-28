package com.github.ukasz09.map;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.github.ukasz09.GameApp;
import com.github.ukasz09.map.mapObjects.Bounds;
import com.github.ukasz09.map.mapObjects.DangerZone;
import com.github.ukasz09.map.mapObjects.Ground;

public class MapParser {
    private static final String MAP_LAYER_NAME_GROUND = "ground";
    private static final String MAP_LAYER_NAME_BOUNDS = "bounds";
    private static final String MAP_LAYER_NAME_DANGERS = "danger";

    public static void parseMapLayers(World world, TiledMap tiledMap) {
        for (MapLayer layer : tiledMap.getLayers()) {
            for (MapObject object : layer.getObjects()) {
                if (object instanceof PolygonMapObject) {
                    addPolygonMapObjectToMap(layer.getName(), (PolygonMapObject) object, world);
                }
            }
        }
    }

    private static void addPolygonMapObjectToMap(String layerName, PolygonMapObject mapObject, World world) {
        Shape shape = createPolygon(mapObject);
        switch (layerName) {
            case MAP_LAYER_NAME_GROUND:
                new Ground(world, shape);
                break;
            case MAP_LAYER_NAME_BOUNDS:
                new Bounds(world, shape);
                break;
            case MAP_LAYER_NAME_DANGERS:
                new DangerZone(world, shape);
        }
    }


    private static ChainShape createPolygon(PolygonMapObject polygonMapObject) {
        float[] vertices = polygonMapObject.getPolygon().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];
        for (int i = 0; i < worldVertices.length; i++) {
            worldVertices[i] = new Vector2(vertices[i * 2] / GameApp.PIXEL_PER_METER,
                    vertices[i * 2 + 1] / GameApp.PIXEL_PER_METER);
        }
        ChainShape cs = new ChainShape();
        cs.createChain(worldVertices);
        return cs;
    }
}