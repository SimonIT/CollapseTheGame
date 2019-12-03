package de.nilsim.collapse;

import ch.asynk.gdx.boardgame.Assets;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class CollapseTheGame extends Game {

	private BoardScreen boardScreen;
	private Assets assets = new Assets();

	public CollapseTheGame() {
	}

	@Override
	public void create() {
		this.boardScreen = new BoardScreen(this.assets);
		setScreen(this.boardScreen);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		System.out.println("rener");
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void dispose() {
		assets.dispose();
	}
}
