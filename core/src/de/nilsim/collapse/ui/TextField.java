package de.nilsim.collapse.ui;

import ch.asynk.gdx.boardgame.ui.Button;
import ch.asynk.gdx.boardgame.ui.Element;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.utils.Timer;

public class TextField extends Button implements InputProcessor {
	private boolean focused;
	private String placeholder;
	private String text = "";
	private int position;
	private boolean multiline = false;
	private boolean show = true;
	private boolean textChanged = false;
	private final Timer.Task blinkTask = new Timer.Task() {
		public void run() {
			boolean taintedParent = taintParent;
			taintParent = false;
			if (show) {
				TextField.super.write(text.substring(0, position) + "|" + text.substring(position));
			} else {
				TextField.super.write(text.substring(0, position) + " " + text.substring(position));
			}
			taintParent = taintedParent;
			if (textChanged) {
				taint();
				textChanged = false;
			} else {
				tainted = false;
			}
			show = !show;
		}
	};

	public TextField(NinePatch patch, BitmapFont font) {
		super(font, patch);
	}

	public TextField(NinePatch patch, BitmapFont font, float padding) {
		super(font, patch, padding);
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
		if (text.isEmpty()) super.write(placeholder);
	}

	public void setMultiline(boolean multiline) {
		this.multiline = multiline;
	}

	void focus() {
		focused = true;
		blinkTask.cancel();
		Timer.schedule(blinkTask, 0.32f, 0.32f);
		Gdx.input.setOnscreenKeyboardVisible(true);
	}

	void unfocus() {
		focused = false;
		blinkTask.cancel();
		show = false;
		if (text.isEmpty()) super.write(placeholder);
		else super.write(text);
		Gdx.input.setOnscreenKeyboardVisible(false);
	}

	public String getText() {
		return text;
	}

	@Override
	public void write(String text) {
		this.text = text;
		position = text.length();
		textChanged = true;
	}

	@Override
	public Element touch(float x, float y) {
		Element touched = super.touch(x, y);
		if (touched == this) {
			if (!focused) {
				focus();
			}
			return this;
		} else {
			unfocus();
			return touched;
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		if (focused) {
			switch (keycode) {
				case Input.Keys.RIGHT:
					if (position < text.length()) position += Character.charCount(text.codePointAt(position));
					break;
				case Input.Keys.LEFT:
					if (position > 0) position -= Character.charCount(text.codePointBefore(position));
					break;
				case Input.Keys.DEL:
					if (text.length() > 0) {
						int charCount = Character.charCount(text.codePointBefore(position));
						text = text.substring(0, position - charCount) + text.substring(position);
						position -= charCount;
						textChanged = true;
					}
					break;
				case Input.Keys.FORWARD_DEL:
					if (position < text.length()) {
						text = text.substring(0, position) + text.substring(position + Character.charCount(text.codePointAt(position)));
						textChanged = true;
					}
					break;
				case Input.Keys.SPACE:
					type(" ");
					break;
				case Input.Keys.TAB:
					type("\t");
					break;
			}
			return true;
		}
		return false;
	}

	public void type(String input) {
		text = text.substring(0, position) + input + text.substring(position);
		position += input.length();
		textChanged = true;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		if (focused) {
			if (character == '\n' && !multiline) return false;
			if (character == '\b' || character == '\u007F') return false;
			type(String.valueOf(character));
			return true;
		}
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
