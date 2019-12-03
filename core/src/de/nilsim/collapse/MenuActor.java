package de.nilsim.collapse;

import ch.asynk.gdx.boardgame.Assets;
import ch.asynk.gdx.boardgame.ui.Alignment;
import ch.asynk.gdx.boardgame.ui.Menu;
import ch.asynk.gdx.boardgame.ui.Root;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MenuActor extends Actor {

	/*private final Root root;
	private final Menu menu;
	private Texture bg;

	public MenuActor(Assets assets) {
		this.root = new Root(1);
		this.root.setPadding(15);
		this.menu = new Menu(
				assets.getFont(assets.FONT_25),
				assets.getNinePatch(assets.PATCH, 23, 23, 23, 23),
				"Menü", new String[]{"UI", "Animations", "Board", "Exit"});
		this.menu.setAlignment(Alignment.MIDDLE_CENTER);
		this.menu.setPaddings(5, 5);
		this.menu.setSpacings(10, 5);
		this.menu.setPadding(20);
		this.menu.setLabelsOffset(10);
		this.root.add(this.menu);

		addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if (menu.touch(x, y)) {
					switch (menu.touched()) {
						case 0:
							break;
						case 1:
							break;
						case 2:
							break;
						case 3:
							break;
					}
				}
			}
		});
	}

	@Override
	protected void sizeChanged() {
		this.root.resize(
				getX() - (getWidth() / 2f),
				getY() - (getHeight() / 2f),
				getWidth(),
				getHeight()
		);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(this.bg, getX(), getY(), getWidth(), getHeight());
		this.root.draw(batch);
	}*/
}
