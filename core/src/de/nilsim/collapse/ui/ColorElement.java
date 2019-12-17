package de.nilsim.collapse.ui;

import ch.asynk.gdx.boardgame.ui.Element;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class ColorElement extends Element {
	private Color color;
	private Texture colorTexture;

	public ColorElement(Color color) {
		this.color = color;
		rect.setSize(25);
		updateTexture();
	}

	public void setColor(Color color) {
		if (!this.color.equals(color)) {
			this.color = color;
			updateTexture();
		}
	}

	public Color getColor() {
		return color;
	}

	private void updateTexture() {
		Pixmap pixmap = new Pixmap((int) getInnerWidth(), (int) getInnerHeight(), Pixmap.Format.RGBA8888);
		pixmap.setColor(color);
		pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
		colorTexture = new Texture(pixmap);
	}

	@Override
	public void setPosition(float x, float y, float w, float h) {
		super.setPosition(x, y, w, h);
		updateTexture();
	}

	@Override
	public void draw(Batch batch) {
		if (!visible) return;
		if (tainted) computeGeometry();

		batch.draw(colorTexture, getInnerX(), getInnerY());
	}
}
