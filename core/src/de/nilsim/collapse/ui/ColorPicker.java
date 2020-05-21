package de.nilsim.collapse.ui;

import ch.asynk.gdx.boardgame.ui.Container;
import ch.asynk.gdx.boardgame.ui.Element;
import ch.asynk.gdx.boardgame.utils.IterableSet;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class ColorPicker extends Container {

	private final int colorPadding = 10;
	private final Texture background;
	private final int colorSize = 25;
	private int elementsPerRow;
	private ColorSelectedListener colorSelectedListener;
	private ColorElement selectedElement;

	public ColorPicker() {
		super();
		children = new IterableSet<>(Colors.getColors().size);
		resetToDefault();

		Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.valueOf("fbf297"));
		pixmap.fillRectangle(0, 0, 1, 1);
		background = new Texture(pixmap);
	}

	public void addColor(Color color) {
		children.add(new ColorElement(color, colorSize));
		updateColors();
	}

	public void removeColor(Color color) {
		for (Element colorElement : children) {
			if (((ColorElement) colorElement).getColor().equals(color)) {
				children.remove(colorElement);
				break;
			}
		}
	}

	public void resetToDefault() {
		children.clear();
		for (Color color : Colors.getColors().values()) {
			children.add(new ColorElement(color, colorSize));
		}
		updateColors();
	}

	private void updateColors() {
		elementsPerRow = MathUtils.round((float) Math.sqrt(this.children.size()));
		float size = colorPadding + elementsPerRow * (colorSize + colorPadding);
		setPosition(getX(), getY(), size, size);
	}

	@Override
	public void computeGeometry(Rectangle area, boolean resized) {
		int row = 0, elementNumber = 0;
		for (Element element : children) {
			if (elementNumber >= elementsPerRow) {
				row++;
				elementNumber = 0;
			}
			element.setPosition(getInnerX() + colorPadding + elementNumber * (element.getWidth() + colorPadding), getInnerY() + colorPadding + row * (element.getHeight() + colorPadding));
			elementNumber++;
		}
	}

	@Override
	public void drawReal(Batch batch) {
		batch.draw(background, getX(), getY(), getWidth(), getHeight());
		super.drawReal(batch);
	}

	@Override
	public Element touch(float x, float y) {
		if (!blocked && visible && rect.contains(x, y)) {
			for (Element e : children) {
				final Element t = e.touch(x, y);
				if (t != null) {
					ColorElement colorElement = (ColorElement) t;
					if (colorSelectedListener != null) {
						selectedElement = colorElement;
						colorSelectedListener.onColorSelected(colorElement.getColor());
					}
					return t;
				}
			}
			return this;
		}
		return null;
	}

	public void setColorSelectedListener(ColorSelectedListener colorSelectedListener) {
		this.colorSelectedListener = colorSelectedListener;
	}

	public interface ColorSelectedListener {
		void onColorSelected(Color color);
	}
}
