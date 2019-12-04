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

	private final Root root;
	private final Menu menu;
	private Assets assets;
	private Texture bg;
	private MenuActorClickListener clickListener;

	MenuActor(Assets assets, String title, String[] menuPoints) {
		this.assets = assets;
		this.root = new Root(1);
		this.root.setPadding(15);
		this.menu = new Menu(
				assets.getFont(AssetNames.font),
				assets.getNinePatch(AssetNames.button, 16, 17, 40, 40),
				title, menuPoints);
		this.menu.setAlignment(Alignment.MIDDLE_CENTER);
		this.menu.setPaddings(5, 5);
		this.menu.setSpacings(10, 5);
		this.menu.setPadding(20);
		this.menu.setLabelsOffset(10);
		this.root.add(this.menu);
		this.bg = assets.getTexture(AssetNames.bg);

		addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if (menu.touch(x, y) && clickListener != null) {
					clickListener.click(menu.touched());
				}
			}
		});
	}

	public void setClickListener(MenuActorClickListener clickListener) {
		this.clickListener = clickListener;
	}

	@Override
	protected void sizeChanged() {
		this.root.resize(getX(), getY(), getWidth(), getHeight());
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(this.bg, getX(), getY(), getWidth(), getHeight());
		this.root.draw(batch);
	}

	public interface MenuActorClickListener {
		void click(int menu);
	}
}
