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
	Board board;
	private Assets assets = new Assets();

	public CollapseTheGame() {
	}

	@Override
	public void create() {
		this.stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);
		board = new Board(assets, 8, 10);
		Piece.diameter = Gdx.graphics.getWidth(); // TODO better
		board.addPiece(2, 2, new Piece(Color.BLUE));
		Piece p = new Piece(Color.BLUE);
		p.setDotAmount(2);
		board.addPiece(2, 3, p);
		Piece p2 = new Piece(Color.BLUE);
		p2.setDotAmount(3);
		board.addPiece(2, 4, p2);
		Table table = new Table();
		table.setFillParent(true);
		table.add(board);
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
	}

	@Override
	public void dispose() {
		stage.dispose();
		assets.dispose();
	}
}
