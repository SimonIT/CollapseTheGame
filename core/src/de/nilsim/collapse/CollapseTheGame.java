package de.nilsim.collapse;

import ch.asynk.gdx.boardgame.Assets;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;

public class CollapseTheGame extends Game {

	MenuScreen menuScreen;
	PlayerScreen playerScreen;
	BoardScreen boardScreen;
	Assets assets = new Assets();

	public CollapseTheGame() {
		FileHandleResolver resolver = new InternalFileHandleResolver();
		assets.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		assets.setLoader(BitmapFont.class, ".TTF", new FreetypeFontLoader(resolver));
	}

	@Override
	public void create() {
		FreetypeFontLoader.FreeTypeFontLoaderParameter myBigFont = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
		myBigFont.fontFileName = AssetNames.font;
		myBigFont.fontParameters.size = 20;
		myBigFont.fontParameters.color = Color.BLACK;
		assets.load(AssetNames.font, BitmapFont.class, myBigFont);
		assets.load(AssetNames.bg, Texture.class);
		assets.load(AssetNames.button, Texture.class);
		assets.finishLoading();
		this.playerScreen = new PlayerScreen(this);
		this.menuScreen = new MenuScreen(this, "Menü", new String[]{"Lokal", "Online", "Einstellungen"});
		this.menuScreen.setClickListener(menu -> {
			switch (menu) {
				case 0:
					this.setScreen(this.playerScreen);
					break;
				case 1:
					System.out.println("Online");
					break;
				case 2:
					System.out.println("Einstellungen");
					break;

			}
		});
		setScreen(this.menuScreen);
	}

	@Override
	public void render() {
		super.render();
		assets.update();
	}

	@Override
	public void dispose() {
		super.dispose();
		assets.dispose();
	}
}
