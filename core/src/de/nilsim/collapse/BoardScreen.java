package de.nilsim.collapse;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class BoardScreen extends ScreenAdapter {
	private CollapseTheGame collapseTheGame;
	private Stage stage;
	private BoardActor board;

	BoardScreen(CollapseTheGame collapseTheGame) {
		this.collapseTheGame = collapseTheGame;
		this.stage = new Stage(new ScreenViewport());
		ObjectMap<Player, Label> playerLabel = new ObjectMap<>();
		playerLabel.put(new Player(Color.valueOf("eb3935"), "Simon"), new Label("0", new Label.LabelStyle(collapseTheGame.assets.getFont(AssetNames.font), Color.WHITE)));
		playerLabel.put(new Player(Color.valueOf("679ed7"), "Nils"), new Label("0", new Label.LabelStyle(collapseTheGame.assets.getFont(AssetNames.font), Color.WHITE)));
		this.board = new BoardActor(collapseTheGame.assets, 5, 7, playerLabel.keys().toArray());
		this.board.addListener(new ActorGestureListener() {
			@Override
			public void tap(InputEvent event, float x, float y, int count, int button) {
				if (board.increaseDotAmount(board.getXFromPixels(x), board.getYFromPixels(y), true)) {
					Player player = board.getCurrentPlayer();
					playerLabel.get(player).setText(String.valueOf(player.getPoints()));
					board.nextPlayer();
				}
			}

			@Override
			public boolean longPress(Actor actor, float x, float y) {
				return true;
			}
		});
		Table table = new Table();
		for (Label label : playerLabel.values()) {
			table.add(label);
		}
		table.row();
		table.setFillParent(true);
		table.add(this.board);
		this.stage.addActor(table);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		Piece.diameter = Gdx.graphics.getWidth(); // TODO better
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.stage.getBatch().begin();
		this.stage.getBatch().draw(collapseTheGame.assets.getTexture("bg.png"), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.stage.getBatch().end();
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
