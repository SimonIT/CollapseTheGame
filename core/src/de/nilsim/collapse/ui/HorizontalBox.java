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
	public void computeDimensions() {
		float width = spacing, height = spacing;
		for (Element child : children) {
			child.setPosition(width, 0);
			child.computeDimensions();
			width += child.getWidth() + spacing;
			height = Math.max(height, child.getHeight());
		}
		width += spacing;
		height += spacing;
		rect.setSize(width, height);
		super.computeDimensions();
	}
}
