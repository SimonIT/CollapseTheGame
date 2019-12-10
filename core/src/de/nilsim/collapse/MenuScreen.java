package de.nilsim.collapse;

import ch.asynk.gdx.boardgame.ui.Alignment;
import ch.asynk.gdx.boardgame.ui.Menu;

public class MenuScreen extends AbstractScreen {

	Menu menu;
	private MenuClickListener clickListener;

	MenuScreen(CollapseTheGame collapseTheGame, String title, String[] menuPoints) {
		super(collapseTheGame);
		this.menu = new Menu(
				app.assets.getFont(AssetNames.font),
				app.assets.getNinePatch(AssetNames.button, 16, 17, 40, 40),
				title, menuPoints);
		this.menu.setAlignment(Alignment.MIDDLE_CENTER);
		this.menu.setPaddings(5, 5);
		this.menu.setSpacings(10, 5);
		this.menu.setPadding(20);
		this.menu.setLabelsOffset(10);
		this.root.add(this.menu);
	}

	public Menu getMenu() {
		return this.menu;
	}

	public void setClickListener(MenuClickListener clickListener) {
		this.clickListener = clickListener;
	}

	@Override
	protected void onTouch(int x, int y) {
		if (this.menu.touch(this.touch.x, this.touch.y) && this.clickListener != null) {
			this.clickListener.click(this.menu.touched());
		}
	}

	@Override
	protected void onZoom(float dz) {

	}

	@Override
	protected void onDragged(int dx, int dy) {

	}

	public interface MenuClickListener {
		void click(int menu);
	}
}