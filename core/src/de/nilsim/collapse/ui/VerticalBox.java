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
	public void computeGeometry() {
		float width = spacing, height = spacing;
		for (Element child : children) {
			child.setPosition(0, height);
			child.computeGeometry();
			height += child.getHeight() + spacing;
			width = Math.max(width, child.getWidth());
		}
		rect.setSize(width, height);
		super.computeGeometry();
		for (Element child : children) {
			child.computeGeometry();
		}
	}
}
