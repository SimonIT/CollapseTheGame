package de.nilsim.collapse.ui;

import ch.asynk.gdx.boardgame.ui.Element;

public class VerticalBox extends FlexibleBox {
	public VerticalBox(int c) {
		super(c);
	}

	public VerticalBox(Element... elements) {
		super(elements);
	}

	@Override
	public void computeDimensions() {
		float width = spacing, height = spacing;
		for (Element child : children) {
			child.setPosition(0, height);
			child.computeDimensions();
			height += child.getHeight() + spacing;
			width = Math.max(width, child.getWidth());
		}
		width += spacing;
		height += spacing;
		rect.setSize(width, height);
		super.computeDimensions();
	}
}
