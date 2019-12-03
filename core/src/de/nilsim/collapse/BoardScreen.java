package de.nilsim.collapse;

import ch.asynk.gdx.boardgame.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class BoardScreen extends ScreenAdapter {
	private Stage stage;
	private BoardActor board;

	BoardScreen(Assets assets) {
		this.stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);
		this.board = new BoardActor(assets, 5, 7);
		Table table = new Table();
		table.setFillParent(true);
		table.add(this.board);
		this.stage.addActor(table);
	}

	@Override
	public void show() {
		Piece.diameter = Gdx.graphics.getWidth(); // TODO better
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.stage.act(Gdx.graphics.getDeltaTime());
		this.stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		this.stage.getViewport().update(width, height, true);
		this.board.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	@Override
	public void dispose() {
		this.stage.dispose();
	}
}
