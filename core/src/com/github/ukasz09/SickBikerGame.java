package com.github.ukasz09;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.Random;

public class SickBikerGame extends ApplicationAdapter {
	private static final float SCALE = 4.0f;
	public static final float PIXEL_PER_METER = 16f;
	private static final float TIME_STEP = 1 / 60f;
	private static final int VELOCITY_ITERATIONS = 6;
	private static final int POSITION_ITERATIONS = 2;
	private static final float VELOCITY_Y = -9.85f;
	private static final float VELOCITY_X = 0f;
	private static final String MAP_PATH = "map/sick-biker-map.tmx";

	private OrthographicCamera orthographicCamera;
	private Box2DDebugRenderer box2DDebugRenderer;
	private World world;
	private Player player;
	private SpriteBatch batch;
	private Texture texture;
	private OrthogonalTiledMapRenderer tiledMapRenderer;
	private TiledMap tiledMap;
	float last = 0;
	float lastJump = 0;

	Random random = new Random();
	public static ArrayList<Coin> coins = new ArrayList<>();



	@Override
	public void create() {
		orthographicCamera = new OrthographicCamera();
		orthographicCamera.setToOrtho(false, Gdx.graphics.getWidth() / SCALE, Gdx.graphics.getHeight() / SCALE);
		world = new World(new Vector2(VELOCITY_X, VELOCITY_Y), false);
		batch = new SpriteBatch();
		texture = new Texture(Player.PLAYER_IMG_PATH);
		box2DDebugRenderer = new Box2DDebugRenderer();
		tiledMap = new TmxMapLoader().load(MAP_PATH);
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
		MapParser.parseMapLayers(world, tiledMap);
		world.setContactListener(new WorldContactListener(world));

		float playerTextureProportionXToY = (float) (texture.getWidth()) / (float) (texture.getHeight());
		float playerHeight = 16 * 3;
		player = new Player(world, 16 * 2, 16 * 3, PIXEL_PER_METER);
	}

	@Override
	public void render() {
		update();
		Gdx.gl.glClearColor(0.5f, 0.8f, 1f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		tiledMapRenderer.render();
		batch.begin();
		batch.draw(new TextureRegion(texture), player.getBody().getPosition().x * PIXEL_PER_METER - (player.getWidth_px() / 2),
				player.getBody().getPosition().y * PIXEL_PER_METER - (player.getHeight_px() / 2), player.getWidth_px() / 2, player.getHeight_px() / 2, player.getWidth_px(), player.getHeight_px(), 1, 1,
				last * (-10)
		);
//        System.out.println((float)(last*(-10)));

//        float width, float height,float scaleX, float scaleY, float rotation)
//        batch.draw(texture, player.getBody().getPosition().x * PIXEL_PER_METER - (player.getWidth_px() / 2),
//                player.getBody().getPosition().y * PIXEL_PER_METER - (player.getHeight_px() / 2), player.getWidth_px(), player.getHeight_px()
//        );
		batch.end();
		box2DDebugRenderer.render(world, orthographicCamera.combined.scl(PIXEL_PER_METER));
	}

	private void update() {
		world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
		inputUpdate();
		cameraUpdate();
		tiledMapRenderer.setView(orthographicCamera);
		batch.setProjectionMatrix(orthographicCamera.combined);
	}

	private void cameraUpdate() {
		Vector3 position = orthographicCamera.position;
		position.x = player.getBody().getPosition().x * PIXEL_PER_METER;
		position.y = player.getBody().getPosition().y * PIXEL_PER_METER;
		orthographicCamera.position.set(position);
		orthographicCamera.update();
	}

	@Override
	public void resize(int width, int height) {
		orthographicCamera.setToOrtho(false, width / SCALE, height / SCALE);
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
		int horizontalForce = 0;
		boolean isJumping = false;
		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
			touchPos = orthographicCamera.unproject(touchPos);
			if (touchPos.x / PIXEL_PER_METER > player.getBody().getPosition().x)
				horizontalForce += 1;
			if (touchPos.x / PIXEL_PER_METER < player.getBody().getPosition().x)
				horizontalForce -= 1;
			if (touchPos.y / PIXEL_PER_METER > player.getBody().getPosition().y && !player.isJumping())
				isJumping = true;

		}
		playerUpdate(horizontalForce, isJumping);

//        float angle = (float) (45*WorldContactListener.DEGREES_TO_RADIANS);
		if (Math.abs(Math.abs(last) - Math.abs(Gdx.input.getAccelerometerY())) > 0.25) {
			last = Gdx.input.getAccelerometerY();
			player.getBody().setTransform(player.getBody().getWorldCenter(),
					(float) ((-10) * Gdx.input.getAccelerometerY() * WorldContactListener.DEGREES_TO_RADIANS));
//            System.out.println(Gdx.input.getAccelerometerY());
		}

		int test=0;
		for (Coin c : coins) {
			if (c.isCollected()) {
				world.destroyBody(c.getBody());
//                coins.remove(c);
//                this.world.destroyBody(c.getBody());
			}
		}

//        if(Math.abs(Math.abs(lastJump) - Math.abs(Gdx.input.getAccelerometerY())) > 4){
//           player.setJumping(true);
//        }
	}


	private void playerUpdate(int horizontalForce, boolean isJumping) {
		if (player.isDead()) {
			world.destroyBody(player.getBody());
			float playerTextureProportionXToY = (float) (texture.getWidth()) / (float) (texture.getHeight());
			float playerWidth = 128;
			player = new Player(world, 16 * 2, 16 * 3, PIXEL_PER_METER);

		}
		if (isJumping)
			player.getBody().applyForceToCenter(0, Player.JUMP_FORCE, false);


		player.getBody().setLinearVelocity(horizontalForce * Player.RUN_FORCE, player.getBody().getLinearVelocity().y);
	}
}
