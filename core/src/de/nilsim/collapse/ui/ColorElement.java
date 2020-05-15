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
		this(color, 25);
	}

	public ColorElement(Color color, int size) {
		this.color = color;
		rect.setSize(size);
		updateTexture();
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		if (!this.color.equals(color)) {
			this.color = color;
			updateTexture();
		}
	}

	private void updateTexture() {
		Pixmap pixmap = new Pixmap((int) getInnerWidth(), (int) getInnerHeight(), Pixmap.Format.RGBA8888);
		pixmap.setColor(color);
		pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
		colorTexture = new Texture(pixmap);
		pixmap.dispose();
	}

	@Override
	public void setPosition(float x, float y, float w, float h) {
		super.setPosition(x, y, w, h);
		updateTexture();
	}

	@Override
	public void drawReal(Batch batch) {
		batch.draw(colorTexture, getInnerX(), getInnerY(), getInnerWidth(), getInnerHeight());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ColorElement that = (ColorElement) o;

		return color.equals(that.color);
	}

	@Override
	public int hashCode() {
		return color.hashCode();
	}
}
