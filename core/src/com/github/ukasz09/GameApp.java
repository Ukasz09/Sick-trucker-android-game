package com.github.ukasz09;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.github.ukasz09.map.MapParser;
import com.github.ukasz09.player.Player;

public class GameApp extends ApplicationAdapter {
    private static final float CAMERA_ZOOM = 4.0f;
    public static final float PIXEL_PER_METER = 16f;
    private static final float TIME_STEP = 1 / 60f;
    private static final int VELOCITY_ITERATIONS = 6;
    private static final int POSITION_ITERATIONS = 2;
    private static final float GRAVITY_VELOCITY_Y = -30f;
    private static final float GRAVITY_VELOCITY_X = 0f;
    private static final float ROTATION_DEGREES_THRESHOLD = 5f;
    private static final float FALLING_FORCE_Y = -5f;
    public static final double DEGREES_TO_RADIANS = Math.PI / 180;

    private static final String MAP_PATH = "map/sick-biker-map.tmx";

    private OrthographicCamera camera;
    private Box2DDebugRenderer box2DDebugRenderer;
    private World world;
    private Player player;
    private SpriteBatch batch;
    private Texture texture;
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    private TiledMap tiledMap;
    private Hud hud;
    private ActivityBridge activityBridge;

    public GameApp(ActivityBridge activityBridge) {
        this.activityBridge = activityBridge;
    }

    @Override
    public void create() {
        initCamera();
        initWorld();
        initTextures();
        initPlayerTruck();
        Gdx.graphics.getDeltaTime();
        hud = new Hud(batch, camera);
        activityBridge.showActivity();
    }

    private void initCamera() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth() / CAMERA_ZOOM, Gdx.graphics.getHeight() / CAMERA_ZOOM);
    }

    private void initWorld() {
        Vector2 gravityVector = new Vector2(GRAVITY_VELOCITY_X, GRAVITY_VELOCITY_Y);
        world = new World(gravityVector, false);
        world.setContactListener(new WorldContactListener());
        tiledMap = new TmxMapLoader().load(MAP_PATH);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        MapParser.parseMapLayers(world, tiledMap);
    }

    private void initTextures() {
        box2DDebugRenderer = new Box2DDebugRenderer();
        batch = new SpriteBatch();
        texture = new Texture(Player.MOVING_SHEET_PATH);
    }

    private void initPlayerTruck() {
        player = new Player(world, PIXEL_PER_METER);
    }

    @Override
    public void render() {
        update();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tiledMapRenderer.render();
        batch.begin();
        drawPlayer();
        hud.render();
        batch.end();
//        renderDebugBoxes();
    }

    private void update() {
        world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        inputUpdate();
        cameraUpdate();
        tiledMapRenderer.setView(camera);
        batch.setProjectionMatrix(camera.combined);
        player.updateAnimation(TIME_STEP);
        hud.update();
    }

    private void cameraUpdate() {
        Vector3 position = camera.position;
        position.x = player.getBody().getPosition().x * PIXEL_PER_METER;
        position.y = player.getBody().getPosition().y * PIXEL_PER_METER;
        camera.position.set(position);
        camera.update();
    }

    private void renderDebugBoxes() {
        box2DDebugRenderer.render(world, camera.combined.scl(PIXEL_PER_METER));
    }

    private void drawPlayer() {
        float playerPositionX = player.getBody().getPosition().x * PIXEL_PER_METER - (player.getWidthPx() / 2);
        float playerPositionY = player.getBody().getPosition().y * PIXEL_PER_METER - (player.getHeightPx() / 2);
        float truckOriginX = player.getWidthPx() / 2;
        float truckOriginY = player.getHeightPx() / 2;
        batch.draw(player.getRegion(), playerPositionX, playerPositionY, truckOriginX, truckOriginY, player.getWidthPx(), player.getHeightPx(), 1.2f, 1.2f,
                player.getRotationDegrees()
        );
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width / CAMERA_ZOOM, height / CAMERA_ZOOM);
    }

    @Override
    public void dispose() {
        texture.dispose();
        batch.dispose();
        box2DDebugRenderer.dispose();
        world.dispose();
        tiledMapRenderer.dispose();
        tiledMap.dispose();
        player.dispose();
    }


    private void inputUpdate() {
        if (Gdx.input.isTouched()) {
            player.setPressedGasPedal(true);
            onTouchPositionUpdate();
        } else {
            player.setPressedGasPedal(false);
        }
        float newRotation = calcNewRotation();
        if (needToRotate(newRotation)) {
            rotatePlayer(newRotation);
        }
        playerTruckUpdate();
    }

    private void onTouchPositionUpdate() {
        int horizontalForce = 0;
        Vector3 touchPosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        touchPosition = camera.unproject(touchPosition);
        if (touchPosition.x / PIXEL_PER_METER > player.getBody().getPosition().x) {
            horizontalForce++;
            player.isGoingBackwards = false;
        }
        if (touchPosition.x / PIXEL_PER_METER < player.getBody().getPosition().x) {
            horizontalForce--;
            player.isGoingBackwards = true;
        }
        updatePlayerPosition(horizontalForce);
    }

    private void playerTruckUpdate() {
        if (player.isDestroyed()) {
            world.destroyBody(player.getBody());
            player.stopSounds();
            player.playBoomSound();
            initPlayerTruck();
            hud.resetTimer();
        } else {
            updateEngineSound();
        }
    }

    private void updatePlayerPosition(int horizontalForce) {
        float velocityX = horizontalForce * Player.MOVING_FORCE;
        if (player.isMovingHorizontally())
            player.getBody().setLinearVelocity(velocityX, player.getBody().getLinearVelocity().y);
        else
            player.getBody().setLinearVelocity(velocityX, FALLING_FORCE_Y);
    }

    private void updateEngineSound() {
        if (player.isPressedGasPedal) {
            if (!player.isGoingBackwards) {
                if (!player.gasPressedSoundIsPlaying) {
                    player.stopBackwardEngineSound();
                    player.playGasPressedEngineSound(Player.GAS_PRESSED_SOUND_VOLUME);
                }
            } else {
                if (!player.backwardSoundIsPlaying) {
                    player.stopGasPressedEngineSound();
                    player.playBackwardEngineSound(Player.SOUND_VOLUME);
                }
            }
        } else {
            player.stopGasPressedEngineSound();
            player.stopBackwardEngineSound();
        }
    }

    private float calcNewRotation() {
        return Gdx.input.getAccelerometerY() * (-10);
    }

    private boolean needToRotate(float newRotation) {
        float rotationDiff = Math.abs(player.getRotationDegrees()) - Math.abs(newRotation);
        return Math.abs(rotationDiff) > ROTATION_DEGREES_THRESHOLD;
    }

    private void rotatePlayer(float rotationDegrees) {
        player.setRotationDegrees(rotationDegrees);
        float radians = (float) (rotationDegrees * DEGREES_TO_RADIANS);
        player.getBody().setTransform(player.getBody().getWorldCenter(), radians);
    }
}
