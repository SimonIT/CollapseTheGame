package de.nilsim.collapse.ui;

import ch.asynk.gdx.boardgame.ui.Element;
import ch.asynk.gdx.boardgame.utils.Collection;

public class VerticalBox extends FlexibleBox {
	public VerticalBox(int c) {
		super(c);
	}

	public VerticalBox(Element... elements) {
		super(elements);
	}

	public VerticalBox(Collection<Element> elements) {
		super(elements);
	}

	@Override
	public void computeDimensions() {
		float width = 0, height = 0;
		for (Element child : children) {
			child.setPosition(0, height);
			child.computeDimensions();
			height += child.getHeight() + spacing;
			width = Math.max(width, child.getWidth());
		}
		rect.setSize(width, height);
		super.computeDimensions();
	}
}
