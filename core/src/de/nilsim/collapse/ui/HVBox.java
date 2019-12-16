package de.nilsim.collapse.ui;

import ch.asynk.gdx.boardgame.ui.Assembly;
import ch.asynk.gdx.boardgame.ui.Element;
import com.badlogic.gdx.graphics.g2d.Batch;

public class HVBox extends Assembly {
	private TYPE type = TYPE.HBox;
	private int spacing = 0;
	private int beforeHash = 0;

	public HVBox(int c) {
		super(c);
	}

	public HVBox(Element... elements) {
		super(elements.length);
		for (Element element : elements) {
			children.add(element);
		}
	}

	public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
		tainted = true;
	}

	public int getSpacing() {
		return spacing;
	}

	public void setSpacing(int spacing) {
		this.spacing = spacing;
		tainted = true;
	}

	@Override
	public void computeGeometry() {
		float width = spacing, height = spacing;
		switch (type) {
			case HBox:
				for (Element child : children) {
					child.setPosition(width, 0);
					width += child.getWidth() + spacing;
					height = Math.max(height, child.getHeight());
				}
				break;
			case VBox:
				for (Element child : children) {
					child.setPosition(0, height);
					height += child.getHeight() + spacing;
					width = Math.max(width, child.getWidth());
				}
				break;
		}
		beforeHash = children.hashCode();
		rect.setSize(width, height);
		super.computeGeometry();
	}

	@Override
	public void draw(Batch batch) {
		if (tainted || children.hashCode() != beforeHash) {
			computeGeometry();
		}
		for (Element element : children) element.draw(batch);
	}

	public enum TYPE {
		VBox,
		HBox,
	}
}
