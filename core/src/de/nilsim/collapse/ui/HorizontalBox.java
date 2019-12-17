package de.nilsim.collapse.ui;

import ch.asynk.gdx.boardgame.ui.Element;

public class HorizontalBox extends FlexibleBox {
	public HorizontalBox(int c) {
		super(c);
	}

	public HorizontalBox(Element... elements) {
		super(elements);
	}

	@Override
	public void computeGeometry() {
		float width = spacing, height = spacing;
		for (Element child : children) {
			child.setPosition(width, 0);
			child.computeGeometry();
			width += child.getWidth() + spacing;
			height = Math.max(height, child.getHeight());
		}
		rect.setSize(width, height);
		super.computeGeometry();
		for (Element child : children) {
			child.computeGeometry();
		}
	}
}
