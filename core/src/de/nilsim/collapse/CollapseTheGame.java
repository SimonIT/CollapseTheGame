package de.nilsim.collapse;

import ch.asynk.gdx.boardgame.Assets;
import com.badlogic.gdx.Game;

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
	public void dispose() {
		assets.dispose();
	}
}
