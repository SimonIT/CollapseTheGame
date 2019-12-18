package de.nilsim.collapse.ui;

import ch.asynk.gdx.boardgame.ui.Assembly;
import ch.asynk.gdx.boardgame.ui.Element;
import ch.asynk.gdx.boardgame.utils.Collection;
import ch.asynk.gdx.boardgame.utils.IterableSet;

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

	public FlexibleBox(Collection<Element> elements) {
		this(elements.size());
		for (Element element : elements) {
			add(element);
		}
	}

	public IterableSet<Element> getChildren() {
		return children;
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
