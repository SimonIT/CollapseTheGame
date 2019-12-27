package de.nilsim.collapse.ui;

import ch.asynk.gdx.boardgame.ui.Element;
import ch.asynk.gdx.boardgame.utils.Collection;

public class HorizontalBox extends FlexibleBox {
	public HorizontalBox(int c) {
		super(c);
	}

	public HorizontalBox(Element... elements) {
		super(elements);
	}

	public HorizontalBox(Collection<Element> elements) {
		super(elements);
	}

	@Override
	public void computeDimensions() {
		float width = 0, height = 0;
		for (Element child : children) {
			child.setPosition(width, 0);
			child.computeDimensions();
			width += child.getWidth() + spacing;
			height = Math.max(height, child.getHeight());
		}
		rect.setSize(width, height);
		super.computeDimensions();
	}
}
