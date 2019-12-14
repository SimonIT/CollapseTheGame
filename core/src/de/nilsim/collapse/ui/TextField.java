package de.nilsim.collapse.ui;

import ch.asynk.gdx.boardgame.ui.Button;
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
	private int pos;
	private boolean multiline = false;
	private boolean show = true;
	private final Timer.Task blinkTask = new Timer.Task() {
		public void run() {
			if (show) {
				write(text.substring(0, pos) + "|" + text.substring(pos));
			} else {
				write(text.substring(0, pos) + " " + text.substring(pos));
			}
			show = !show;
		}
	};

	public TextField(NinePatch patch, BitmapFont font) {
		this(patch, font, 0);
	}

	public TextField(NinePatch patch, BitmapFont font, float padding) {
		super(font, patch, padding);
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
		if (this.text.isEmpty()) write(placeholder);
	}

	public void setMultiline(boolean multiline) {
		this.multiline = multiline;
	}

	void focus() {
		this.focused = true;
		this.blinkTask.cancel();
		Timer.schedule(this.blinkTask, 0.32f, 0.32f);
		Gdx.input.setOnscreenKeyboardVisible(true);
	}

	void unfocus() {
		this.focused = false;
		this.blinkTask.cancel();
		this.show = false;
		if (this.text.isEmpty()) write(this.placeholder);
		else write(this.text);
		Gdx.input.setOnscreenKeyboardVisible(false);
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
		this.pos = text.length();
	}

	@Override
	public boolean touch(float x, float y) {
		if (super.touch(x, y)) {
			if (!this.focused) {
				focus();
			}
			return true;
		} else {
			unfocus();
			return false;
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		if (this.focused) {
			switch (keycode) {
				case Input.Keys.RIGHT:
					if (this.pos < this.text.length()) this.pos++;
					break;
				case Input.Keys.LEFT:
					if (this.pos > 0) this.pos--;
					break;
				case Input.Keys.DEL:
					if (this.text.length() > 0) {
						this.text = this.text.substring(0, this.pos - 1) + this.text.substring(this.pos);
						this.pos--;
					}
					break;
				case Input.Keys.FORWARD_DEL:
					if (this.pos < this.text.length()) {
						this.text = this.text.substring(0, this.pos) + this.text.substring(this.pos + 1);
					}
					break;
				case Input.Keys.SPACE:
					type(" ");
					break;
				case Input.Keys.TAB:
					type("\t");
					break;
				case Input.Keys.ENTER:
					if (this.multiline) type("\n");
					break;
			}
			return true;
		}
		return false;
	}

	public void type(String in) {
		this.text = this.text.substring(0, this.pos) + in + this.text.substring(this.pos);
		this.pos += in.length();
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		if (this.focused) {
			if (character != '\b') type(String.valueOf(character));
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
