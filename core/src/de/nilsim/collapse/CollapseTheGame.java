package de.nilsim.collapse;

import ch.asynk.gdx.boardgame.Assets;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CollapseTheGame extends ApplicationAdapter {
	private SpriteBatch batch;

	private Assets assets = new Assets();

	@Override
	public void create() {
		batch = new SpriteBatch();
		assets.load("badlogic.jpg", Texture.class);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		assets.finishLoadingAsset("badlogic.jpg");
		batch.begin();
		batch.draw(assets.getTexture("badlogic.jpg"), 0, 0);
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
		assets.dispose();
	}
}
