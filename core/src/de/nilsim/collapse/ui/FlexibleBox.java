package de.nilsim.collapse.ui;

import ch.asynk.gdx.boardgame.ui.Element;
import ch.asynk.gdx.boardgame.utils.Collection;
import ch.asynk.gdx.boardgame.utils.IterableSet;
import com.badlogic.gdx.graphics.g2d.Batch;

public abstract class FlexibleBox extends Element {
	protected int spacing = 0;

	public FlexibleBox(int c) {
		this.children = new IterableSet<>(c);
	}

	public FlexibleBox(Element... elements) {
		this.children = new IterableSet<>(elements.length);
		for (Element element : elements) {
			add(element);
		}
	}

	public FlexibleBox(Collection<Element> elements) {
		this.children = new IterableSet<>(elements.size());
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

	@Override
	public void draw(Batch batch) {
		if (tainted) computeGeometry();
		drawChildren(batch);
	}
}
