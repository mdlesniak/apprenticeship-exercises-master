package legacyGame;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;

public class LegacyGameGUI extends Applet implements Runnable {
	private static final long serialVersionUID = 4873261510528018302L;

	MediaTracker tracker;
	Thread thread;

	boolean loadedImages = false;
	Image xMark;
	Image oMark;
	Image filledOMark;
	Image emptySquare;
	Image winningImage;
	Image losingImage;
	Image yourTurnImage;
	Image newGameImage;

	private LegacyGame game;

	public void init() {

		tracker = new MediaTracker(this);
		loadAllGameGraphics(tracker);
		game = new LegacyGame();
		game.resetMainGameBoard(0);
	}

	public void start() {
		thread = new Thread(this);
		thread.start();
	}

	public void stop() {
		thread = null;
	}

	public void run() {
		try // main game engine
		{
			tracker.waitForAll();
			loadedImages = true;
			game.run();
		} catch (InterruptedException e) {
			return;
		}
		repaint();
	}

	public void paint(Graphics g) {
		if (game.moveNumber == -1) {
			setBackground(Color.white);
			if (!loadedImages) {
				g.drawRect(40, 110, 220, 215);
				for (int i = 0; i < 4; i++)
					g.fillRect(50 + i * 57, 285, 30, 30);
				for (int i = 0; i < 3; i++)
					g.fillRect(50, 120 + i * 50, 200, 40);
				g.drawString("LOADING IMAGES, PLEASE WAIT...", 20, 20);
				g.drawImage(xMark, 50, 285, this);
				g.drawImage(filledOMark, 107, 285, this);
				g.drawImage(oMark, 164, 285, this);
				g.drawImage(emptySquare, 221, 285, this);
				g.drawImage(winningImage, 50, 220, this);
				g.drawImage(losingImage, 50, 170, this);
				g.drawImage(yourTurnImage, 50, 120, this);
				return;
			}
		} else {
			for (int r = 0; r < LegacyGame.SQUARES_PER_SIDE; r++) {
				for (int c = 0; c < LegacyGame.SQUARES_PER_SIDE; c++) {
					if (game.gameBoard[0][r * LegacyGame.SQUARES_PER_SIDE + c] == LegacyGame.X_MARK_FOR_PLAYER)
						g.drawImage(xMark, c * 30, r * 30 + 40, this);
					else if (game.gameBoard[0][r * LegacyGame.SQUARES_PER_SIDE
							+ c] == LegacyGame.ZERO_MARK_FOR_COMPUTER) {
						if (r * LegacyGame.SQUARES_PER_SIDE + c == game.lastMove)
							g.drawImage(filledOMark, c * 30, r * 30 + 40, this);
						else
							g.drawImage(oMark, c * 30, r * 30 + 40, this);
					} else
						g.drawImage(emptySquare, c * 30, r * 30 + 40, this);
				}
			}
		}

		g.drawImage(newGameImage, 0, 0, this);
		if (game.gameState == 0)
			g.drawImage(yourTurnImage, LegacyGame.TOTAL_SQUARES_PER_BOARD, 0,
					this);
		else if (game.gameState == 2)
			g.drawImage(winningImage, LegacyGame.TOTAL_SQUARES_PER_BOARD, 0,
					this);
		else if (game.gameState == 3)
			g.drawImage(losingImage, LegacyGame.TOTAL_SQUARES_PER_BOARD, 0,
					this);
	}

	public boolean mouseUp(Event evt, int x, int y) {
		if (!loadedImages)
			return true;

		if (y < 40) {
			if (x < LegacyGame.TOTAL_SQUARES_PER_BOARD) // new game
			{
				game.run();
				repaint();
			}
			return true;
		}

		x = x / 30;
		y = (y - 40) / 30;

		int playerMove = y * LegacyGame.SQUARES_PER_SIDE + x;

		System.out.println("playerMove = " + playerMove);
		System.out.println("player x = " + x);
		System.out.println("player y = " + y);

		if (game.gameBoard[0][playerMove] != LegacyGame.EMPTY
				|| game.gameState != 0 || game.moveNumber > 49) // polje
		{
			return true;
		}
		game.respondToMouseUp(playerMove, x, y);

		repaint();
		return true;
	}

	private void loadAllGameGraphics(MediaTracker tracker) {
		String graphicsDirectory = "../graphics/";
		
		xMark = getImage(getCodeBase(), graphicsDirectory + "xMark.jpg");
		oMark = getImage(getCodeBase(), graphicsDirectory + "oMark.jpg");
		filledOMark = getImage(getCodeBase(), graphicsDirectory + "oMarkFilled.jpg");
		emptySquare = getImage(getCodeBase(), graphicsDirectory + "emptySquare.jpg");
		winningImage = getImage(getCodeBase(), graphicsDirectory + "win.jpg");
		losingImage = getImage(getCodeBase(), graphicsDirectory + "lose.jpg");
		yourTurnImage = getImage(getCodeBase(), graphicsDirectory + "yourTurn.jpg");
		newGameImage = getImage(getCodeBase(), graphicsDirectory + "newgame.jpg");

		tracker.addImage(xMark, 0);
		tracker.addImage(oMark, 0);
		tracker.addImage(filledOMark, 0);
		tracker.addImage(emptySquare, 0);
		tracker.addImage(winningImage, 0);
		tracker.addImage(losingImage, 0);
		tracker.addImage(yourTurnImage, 0);
		tracker.addImage(newGameImage, 0);
	}

}
