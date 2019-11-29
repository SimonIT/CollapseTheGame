package de.nilsim.collapse;

import ch.asynk.gdx.boardgame.Assets;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class CollapseTheGame extends ApplicationAdapter {

	Stage stage;
	Skin skin = new Skin();
	Board board;
	private Assets assets = new Assets();

	public CollapseTheGame() {
	}

	@Override
	public void create() {
		this.stage = new Stage(new ScreenViewport());
		board = new Board(assets, 8, 10);
		board.addPiece(2,2, new Piece(Color.BLUE, 50));
		board.addPiece(0,0, new Piece(Color.BLUE, 3));
		Table table = new Table();
		table.add(board);
		table.setFillParent(true);
		this.stage.addActor(table);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.stage.act(Gdx.graphics.getDeltaTime());
		this.stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		this.stage.getViewport().update(width, height, true);
		board.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	@Override
	public void dispose() {
		stage.dispose();
		assets.dispose();
	}
}
