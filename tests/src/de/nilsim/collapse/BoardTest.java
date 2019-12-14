package de.nilsim.collapse;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(GdxTestRunner.class)
public class BoardTest {

	Array<Player> players;
	CollapseBoard board;

	@Before
	public void setUpBoard() {
		Player mockedPlayer = mock(Player.class);
		when(mockedPlayer.getDefaultPieceTexture()).thenReturn(new Texture(new Pixmap(0, 0, Pixmap.Format.RGBA8888)));
		this.players = new Array<>();
		this.players.add(mockedPlayer);
		this.board = new CollapseBoard(7, 10, this.players);
	}

	@Test
	public void setPiece() {
		assertTrue(this.board.increaseDotAmount(1, 1));
	}
}
