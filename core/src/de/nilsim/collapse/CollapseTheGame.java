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
		assets.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
	}

	@Override
	public void create() {
		FreetypeFontLoader.FreeTypeFontLoaderParameter normal = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
		normal.fontFileName = "ARLRDBD.TTF";
		normal.fontParameters.size = 20;
		normal.fontParameters.color = Color.BLACK;
		assets.load(AssetNames.font, BitmapFont.class, normal);
		FreetypeFontLoader.FreeTypeFontLoaderParameter shadow = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
		shadow.fontFileName = "ARLRDBD.TTF";
		shadow.fontParameters.size = 20;
		shadow.fontParameters.color = Color.BLACK;
		shadow.fontParameters.borderColor = Color.CYAN;
		shadow.fontParameters.borderWidth = 2;
		assets.load(AssetNames.shadowFont, BitmapFont.class, shadow);
		FreetypeFontLoader.FreeTypeFontLoaderParameter red = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
		red.fontFileName = "ARLRDBD.TTF";
		red.fontParameters.size = 20;
		red.fontParameters.color = Color.RED;
		assets.load(AssetNames.redFont, BitmapFont.class, red);
		assets.load(AssetNames.background, Texture.class);
		assets.load(AssetNames.button, Texture.class);
		assets.finishLoading();
		playerScreen = new PlayerScreen(this);
		menuScreen = new MenuScreen(this, "Menü", new String[]{"Lokal", "Online", "Einstellungen"});
		menuScreen.setClickListener(menu -> {
			switch (menu.getText()) {
				case "Lokal":
					setScreen(playerScreen);
					break;
				case "Online":
					System.out.println("Online");
					break;
				case "Einstellungen":
					System.out.println("Einstellungen");
					break;

			}
		});
		setScreen(menuScreen);
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
