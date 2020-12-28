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
import com.github.ukasz09.player.PlayerTruck;

public class SickBikerGame extends ApplicationAdapter {
    private static final float CAMERA_ZOOM = 4.0f;
    public static final float PIXEL_PER_METER = 16f;
    private static final float TIME_STEP = 1 / 60f;
    private static final int VELOCITY_ITERATIONS = 6;
    private static final int POSITION_ITERATIONS = 2;
    private static final float GRAVITY_VELOCITY_Y = -30f;
    private static final float GRAVITY_VELOCITY_X = 0f;
    private static final float ROTATION_DEGREES_THRESHOLD = 5f;

    private static final String MAP_PATH = "map/sick-biker-map.tmx";

    private OrthographicCamera camera;
    private Box2DDebugRenderer box2DDebugRenderer;
    private World world;
    private PlayerTruck playerTruck;
    private SpriteBatch batch;
    private Texture texture;
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    private TiledMap tiledMap;

    @Override
    public void create() {
        initCamera();
        initWorld();
        initTextures();
        initPlayerTruck();
    }

    private void initCamera() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth() / CAMERA_ZOOM, Gdx.graphics.getHeight() / CAMERA_ZOOM);
    }

    private void initWorld() {
        Vector2 gravityVector = new Vector2(GRAVITY_VELOCITY_X, GRAVITY_VELOCITY_Y);
        world = new World(gravityVector, false);
        world.setContactListener(new WorldContactListener(world));
        tiledMap = new TmxMapLoader().load(MAP_PATH);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        MapParser.parseMapLayers(world, tiledMap);
    }

    private void initTextures() {
        box2DDebugRenderer = new Box2DDebugRenderer();
        batch = new SpriteBatch();
        texture = new Texture(PlayerTruck.PLAYER_IMG_PATH);
    }

    private void initPlayerTruck() {
        playerTruck = new PlayerTruck(world, PIXEL_PER_METER);
    }

    @Override
    public void render() {
        update();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tiledMapRenderer.render();
        batch.begin();
        drawPlayer();
        batch.end();
        renderDebugBoxes();
    }

    private void update() {
        world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        inputUpdate();
        cameraUpdate();
        tiledMapRenderer.setView(camera);
        batch.setProjectionMatrix(camera.combined);
        playerTruck.act(1 / 200f); //TODO:
    }

    private void cameraUpdate() {
        Vector3 position = camera.position;
        position.x = playerTruck.getBody().getPosition().x * PIXEL_PER_METER;
        position.y = playerTruck.getBody().getPosition().y * PIXEL_PER_METER;
        camera.position.set(position);
        camera.update();
    }

    private void renderDebugBoxes() {
        box2DDebugRenderer.render(world, camera.combined.scl(PIXEL_PER_METER));
    }

    private void drawPlayer() {
        float truckPositionX = playerTruck.getBody().getPosition().x * PIXEL_PER_METER - (playerTruck.getWidthPx() / 2);
        float truckPositionY = playerTruck.getBody().getPosition().y * PIXEL_PER_METER - (playerTruck.getHeightPx() / 2);
        float truckOriginX = playerTruck.getWidthPx() / 2;
        float truckOriginY = playerTruck.getHeightPx() / 2;
        batch.draw(playerTruck.region, truckPositionX, truckPositionY, truckOriginX, truckOriginY, playerTruck.getWidthPx(), playerTruck.getHeightPx(), 1.2f, 1.2f,
                playerTruck.getRotation()
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
    }


    private void inputUpdate() {
        if (Gdx.input.isTouched())
            touchInputUpdate();
        else {
//            if (!playerTruck.isJumping()) {
//                playerUpdate(0);
//            }
            playerTruck.setPressedGasPedal(false);
        }


        float newRotation = calcNewRotation();
        if (Math.abs(Math.abs(playerTruck.getRotation()) - Math.abs(newRotation)) > ROTATION_DEGREES_THRESHOLD) {
            playerTruck.setRotation(newRotation);
            playerTruck.getBody().setTransform(playerTruck.getBody().getWorldCenter(),
                    (float) ((-10) * Gdx.input.getAccelerometerY() * WorldContactListener.DEGREES_TO_RADIANS));
//            System.out.println(Gdx.input.getAccelerometerY());
        }


//        if(Math.abs(Math.abs(lastJump) - Math.abs(Gdx.input.getAccelerometerY())) > 4){
//           player.setJumping(true);
//        }
    }

    private float calcNewRotation() {
        return Gdx.input.getAccelerometerY() * (-10);
    }

    private void touchInputUpdate() {
        int horizontalForce = 0;
        Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        touchPos = camera.unproject(touchPos);
        if (touchPos.x / PIXEL_PER_METER > playerTruck.getBody().getPosition().x) {
            horizontalForce += 1;
            playerTruck.setPressedGasPedal(true);
        }
        if (touchPos.x / PIXEL_PER_METER < playerTruck.getBody().getPosition().x) {
            horizontalForce -= 1;
            playerTruck.setPressedGasPedal(true);
        }
//            if (touchPos.y / PIXEL_PER_METER > player.getBody().getPosition().y && !player.isJumping())
//                isJumping = true;
        playerUpdate(horizontalForce);
    }

    private void playerUpdate(int horizontalForce) {
        if (playerTruck.isDead()) {
            world.destroyBody(playerTruck.getBody());
            float playerTextureProportionXToY = (float) (texture.getWidth()) / (float) (texture.getHeight());
            float playerWidth = 128;
            playerTruck = new PlayerTruck(world, PIXEL_PER_METER);

        }

        //FIXME:
        if (playerTruck.getLastPositionX() != playerTruck.getBody().getPosition().x)
            playerTruck.getBody().setLinearVelocity(horizontalForce * PlayerTruck.RUN_FORCE, playerTruck.getBody().getLinearVelocity().y);
        else
            playerTruck.getBody().setLinearVelocity(horizontalForce * PlayerTruck.RUN_FORCE, -5f);

    }

}
