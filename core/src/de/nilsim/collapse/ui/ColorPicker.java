package de.nilsim.collapse.ui;

import ch.asynk.gdx.boardgame.ui.Element;
import ch.asynk.gdx.boardgame.utils.IterableSet;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;

public class ColorPicker extends Element {

	private int elementsPerRow, colorPadding = 10;

	public ColorPicker() {
		super();
		children = new IterableSet<>(Colors.getColors().size);
		resetToDefault();
	}

	public void addColor(Color color) {
		children.add(new ColorElement(color));
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
			children.add(new ColorElement(color));
		}
		updateColors();
	}

	private void updateColors() {
		elementsPerRow = MathUtils.round((float) Math.sqrt(this.children.size()));
	}

	@Override
	public void computePosition() {
		super.computePosition();

		int row = 0, elementNumber = 0;
		for (Element element : children) {
			System.out.println(element);
			if (elementNumber >= elementsPerRow) {
				row++;
				elementNumber = 0;
			}
			element.setPosition(getInnerX() + elementNumber * (element.getWidth() + colorPadding), getInnerY() + row * (element.getHeight() + colorPadding));
			elementNumber++;
		}
	}

	@Override
	public void draw(Batch batch) {
		if (!visible) return;
		if (tainted) computeGeometry();

		drawChildren(batch);
	}
}
