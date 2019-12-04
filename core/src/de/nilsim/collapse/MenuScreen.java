package de.nilsim.collapse;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MenuScreen extends ScreenAdapter {

	private CollapseTheGame collapseTheGame;
	private Stage stage;
	private MenuActor menu;

	MenuScreen(CollapseTheGame collapseTheGame) {
		this.collapseTheGame = collapseTheGame;
		this.stage = new Stage(new ScreenViewport());
		this.menu = new MenuActor(this.collapseTheGame.assets, "Menü", new String[]{"Lokal", "Online", "Einstellungen"});
		this.menu.setClickListener(menu -> {
			switch (menu) {
				case 0:
					this.collapseTheGame.setScreen(this.collapseTheGame.boardScreen);
					break;
				case 1:
					System.out.println("Online");
					break;
				case 3:
					System.out.println("Einstellungen");
					break;

			}
		});
		Table table = new Table();
		table.setFillParent(true);
		table.add(menu);
		this.stage.addActor(table);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
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
		this.menu.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	@Override
	public void dispose() {
		this.stage.dispose();
	}
}
