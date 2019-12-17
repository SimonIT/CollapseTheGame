package de.nilsim.collapse.ui;

import ch.asynk.gdx.boardgame.ui.Assembly;
import ch.asynk.gdx.boardgame.ui.Element;

public abstract class FlexibleBox extends Assembly {
	protected int spacing = 0;

	public FlexibleBox(int c) {
		super(c);
	}

	public FlexibleBox(Element... elements) {
		this(elements.length);
		for (Element element : elements) {
			add(element);
		}
	}

	@Override
	public void add(Element e) {
		e.taintParent = true;
		super.add(e);
	}

	public int getSpacing() {
		return spacing;
	}

	public void setSpacing(int spacing) {
		this.spacing = spacing;
		tainted = true;
	}
}
