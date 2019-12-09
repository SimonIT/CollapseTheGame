package de.nilsim.collapse;

import ch.asynk.gdx.boardgame.ui.Root;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public abstract class AbstractScreen implements Screen {
	private static final float INPUT_DELAY = 0.1f;
	private static final float ZOOM_SCROLL_FACTOR = .1f;
	private static final float ZOOM_GESTURE_FACTOR = .01f;

	protected final Vector2 dragPos = new Vector2();
	protected final Vector3 touch = new Vector3();

	protected final CollapseTheGame app;
	protected final SpriteBatch batch;
	protected final Texture bg;
	protected final Root root;
	protected OrthographicCamera camera;
	protected boolean inputBlocked;
	protected float inputDelay;
	protected boolean paused;

	public AbstractScreen(final CollapseTheGame app) {
		this.app = app;
		this.batch = new SpriteBatch();
		this.bg = app.assets.getTexture(AssetNames.bg);
		this.root = new Root(1);
		this.root.setPadding(15);
		this.inputBlocked = false;
		this.inputDelay = 0f;
		this.paused = false;
		this.camera = new OrthographicCamera();
	}

	/**
	 * May override for custom drawings
	 *
	 * @param batch
	 */
	protected void draw(SpriteBatch batch) {
	}

	@Override
	public void render(float delta) {
		if (paused) return;

		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(bg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		draw(batch);
		root.draw(batch);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		root.resize(0, 0, width, height);
		camera.setToOrtho(false, width, height);
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(getMultiplexer());
		paused = false;
	}

	@Override
	public void hide() {
		paused = true;
	}

	@Override
	public void pause() {
		paused = true;
	}

	@Override
	public void resume() {
		paused = false;
	}

	protected abstract void onTouch(int x, int y);

	protected abstract void onZoom(float dz);

	protected abstract void onDragged(int dx, int dy);

	private InputMultiplexer getMultiplexer() {
		final InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(new InputAdapter() {
			@Override
			public boolean scrolled(int amount) {
				onZoom(amount * ZOOM_SCROLL_FACTOR);
				return true;
			}

			@Override
			public boolean touchDown(int x, int y, int pointer, int button) {
				if (inputBlocked) return true;
				if (button == Input.Buttons.LEFT) {
					dragPos.set(x, y);
					touch.set(x, y, 0);
					camera.unproject(touch);
					onTouch(x, y);
				}
				return true;
			}

			@Override
			public boolean touchDragged(int x, int y, int pointer) {
				int dx = (int) (dragPos.x - x);
				int dy = (int) (dragPos.y - y);
				dragPos.set(x, y);
				onDragged(dx, dy);
				return true;
			}
		});
		multiplexer.addProcessor(new GestureDetector(new GestureDetector.GestureAdapter() {
			@Override
			public boolean zoom(float initialDistance, float distance) {
				if (initialDistance > distance)
					onZoom(ZOOM_GESTURE_FACTOR);
				else
					onZoom(-ZOOM_GESTURE_FACTOR);
				inputBlocked = true;
				inputDelay = INPUT_DELAY;
				return true;
			}
		}));

		return multiplexer;
	}

	@Override
	public String toString() {
		return "AbstractScreen{" +
				"dragPos=" + dragPos +
				", touch=" + touch +
				", app=" + app +
				", batch=" + batch +
				", bg=" + bg +
				", root=" + root +
				", camera=" + camera +
				", inputBlocked=" + inputBlocked +
				", inputDelay=" + inputDelay +
				", paused=" + paused +
				'}';
	}
}
