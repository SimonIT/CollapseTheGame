package de.nilsim.collapse;

import ch.asynk.gdx.boardgame.ui.Alignment;
import ch.asynk.gdx.boardgame.ui.Label;
import ch.asynk.gdx.boardgame.ui.Menu;

public class MenuScreen extends AbstractScreen {

	Menu menu;
	private MenuClickListener clickListener;

	MenuScreen(CollapseTheGame collapseTheGame, String title, String[] menuPoints) {
		super(collapseTheGame);
		menu = new Menu(
				app.assets.getFont(AssetNames.font),
				app.assets.getNinePatch(AssetNames.button, 16, 17, 40, 40),
				title, menuPoints);
		menu.setAlignment(Alignment.MIDDLE_CENTER);
		menu.setPaddings(5, 5);
		menu.setSpacings(10, 5);
		menu.setPadding(20);
		menu.setLabelsOffset(10);
		root.add(menu);
	}

	public Menu getMenu() {
		return menu;
	}

	public void setClickListener(MenuClickListener clickListener) {
		this.clickListener = clickListener;
	}

	@Override
	protected void onTouch(int x, int y) {
		if (menu.touch(touch.x, touch.y) != null && clickListener != null) {
			clickListener.click((Label) menu.touch(touch.x, touch.y));
		}
	}

	@Override
	protected void onZoom(float dz) {

	}

	@Override
	protected void onDragged(int dx, int dy) {

	}

	public interface MenuClickListener {
		void click(Label menu);
	}
}
