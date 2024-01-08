package utils;

import junit.framework.TestCase;
import legacyGame.LegacyGame;
import model.gamestate.Board;

public class BaseSeriesMethodTestFixture extends TestCase {
	private static final int MAX_MOVES_POSSIBLE = 48;
	protected LegacyGame oldGame;
	protected int result;

	protected int drawTotal;
	public int newGameTotal;
	public int oldGameWinTotal;
	private boolean reporting;
	protected static final int MAIN_LEVEL = 0;

	/*
	 * 
	 * 
	 * 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27
	 * 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 49 50 51
	 * 52 53 54 55 56 57 58 59 60 61 62 63 64 65 66 67 68 69 70 71 72 73 74 75
	 * 76 77 78 79 80 81 82 83 84 85 86 87 88 89 90 91 92 93 94 95 96 97 98 99
	 */

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		oldGame = new LegacyGame();
	}

	public void testKeepJunitHappy() throws Exception {
	}

	protected void takeOldGamePosition(int position, int playerMark, int boardLevel) {
		oldGame.gameBoard[boardLevel][position] = playerMark;
	}

	protected void setGameStateToNewGameWon() {
		oldGame.gameState = 2;
		newGameTotal++;
	}

	protected void setGameStateToOldGameWon() {
		oldGame.gameState = 3;
		oldGameWinTotal++;
	}

	protected boolean newGameWon() {
		return oldGame.win() == LegacyGame.X_MARK_FOR_PLAYER;
	}

	protected boolean oldGameWon() {
		return oldGame.win() == LegacyGame.ZERO_MARK_FOR_COMPUTER;
	}

	protected int firstPlayerTakesAMove(int playerPosition) {
		int x = (playerPosition % Board.MAX_COLUMNS);
		int y = (playerPosition - x) / Board.MAX_COLUMNS;
		int respondingPosition = oldGame.makeComputerMove(x, y, reporting);
		takeOldGamePosition(respondingPosition, LegacyGame.ZERO_MARK_FOR_COMPUTER,
				MAIN_LEVEL);
		return respondingPosition;
	}

	protected int secondPlayerTakesAMove(int computerPosition) {
		int x = (computerPosition % Board.MAX_COLUMNS);
		int y = (computerPosition - x) / Board.MAX_ROWS;
		int position = oldGame.makeComputerMove(x, y, reporting);

		takeOldGamePosition(position, LegacyGame.X_MARK_FOR_PLAYER, MAIN_LEVEL);
		return position;
	}

	protected int getRandomValidMove() {
		int position;
		position = getRandomPosition();
		while (thisPositionIsAlreadyTaken(position)) {
			position = getRandomPosition();
		}
		return position;
	}

	private int getRandomPosition() {
		return (int) (Math.random() * LegacyGame.TOTAL_SQUARES_PER_BOARD);
	}

	protected boolean theMaximumNumberOfMovesHasNotBeenTaken() {
		return oldGame.moveNumber < MAX_MOVES_POSSIBLE;
	}

	private boolean thisPositionIsAlreadyTaken(int position) {
		return oldGame.gameBoard[0][position] != LegacyGame.EMPTY;
	}

}
