package harness.twogame;

import java.math.BigDecimal;

import legacyGame.LegacyGame;
import model.gamestate.Board;
import model.strategy.ExampleStrategy;
import utils.BaseSeriesMethodTestFixture;
import controller.gameplay.StubView;
import controller.gameplay.TicTacToeGame;

public class OldGameAgainstNewGameTests extends BaseSeriesMethodTestFixture {
	private TicTacToeGame newGame;
	private int moveNumberTotals;
	private BigDecimal newGamePercentage;
	private BigDecimal oldGamePercentage;
	private BigDecimal drawPercentage;
	
	private int averageMovesPerGame;
	private boolean reporting;
	private long endTime;
	private long durationInSeconds;
	private Long longMinutes;
	private int minutes;
	private int secondsRemainder;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		newGame = new TicTacToeGame(new ExampleStrategy(), new StubView());
		drawTotal = 0;
		oldGameWinTotal = 0;
		newGameTotal = 0;
		moveNumberTotals = 0;
	}

	// TODO: Need board-comparison utility between moves, to ensure that only
	// one place changed!
	public void testNewGameBeatsOrDrawsOldGameMostOfTheTime() throws Exception {
		reporting = false;
		int totalGamesPlayed = 1000;
		long startTime = System.currentTimeMillis();

		playThisManyGamesOneAgainstTheOther(totalGamesPlayed);
		prepareResults(totalGamesPlayed, startTime);
		printResults(totalGamesPlayed);
		assertNewGameMostlyWon();
	}

	private void assertNewGameMostlyWon() {
		assertTrue(averageMovesPerGame > 15);
		assertTrue(newGamePercentage.floatValue() > 45);
		assertTrue(oldGamePercentage.floatValue() < 16);
		assertTrue(drawPercentage.floatValue() > 35);
	}

	private void playThisManyGamesOneAgainstTheOther(int totalGamesPlayed) {
		for (int i = 0; i < totalGamesPlayed; i++) {
			playNewGameAgainstOldGame();
			if (reporting)
				System.out.println("************************************************ STARTING OVER ***********************************************");
			initGames();
		}
	}

	private void initGames() {
		oldGame = null;
		oldGame = new LegacyGame();
		newGame = null;
		newGame = new TicTacToeGame(new ExampleStrategy(), new StubView());
	}

	private void playNewGameAgainstOldGame() {
		int newGamePosition = getRandomValidMove();
		oldGame.moveNumber++;
		int oldGamePosition = 0;

		while (oldGameReportsNoOneHasWonYet() && (theMaximumNumberOfMovesHasNotBeenTaken())) {
			oldGame.moveNumber++;
			newGamePosition = newGameMakesMove(newGamePosition, oldGamePosition);
			oldGamePosition = oldGameMakesMove(newGamePosition, oldGamePosition);
		}

		determineWinner();
	}

	private boolean oldGameReportsNoOneHasWonYet() {
		return (oldGame.gameState < 2);
	}

	private void prepareResults(int totalGamesPlayed, long startTime) {
		endTime = System.currentTimeMillis();
		durationInSeconds = (endTime - startTime) / 1000;
		longMinutes = durationInSeconds / 60;
		minutes = longMinutes.intValue();
		secondsRemainder = new Long(durationInSeconds % 60).intValue();
	}

	private void printResults(int totalGamesPlayed) {
		System.out.println();
		System.out.println("Total duration = " + minutes + " minutes and "
				+ secondsRemainder + " seconds.");
		System.out.println("Games played = " + totalGamesPlayed);
		newGamePercentage = getPercentageOfTime(newGameTotal, totalGamesPlayed);
		oldGamePercentage = getPercentageOfTime(oldGameWinTotal,
				totalGamesPlayed);
		drawPercentage = getPercentageOfTime(drawTotal, totalGamesPlayed);
		averageMovesPerGame = moveNumberTotals / totalGamesPlayed;
		System.out.println("Average moves per game = " + averageMovesPerGame);
		System.out.println("New Game won " + newGameTotal + " times, or "
				+ newGamePercentage + "%");
		System.out.println("Old Game won " + oldGameWinTotal + " times, or "
				+ oldGamePercentage + "%");
		System.out.println("Nobody won " + drawTotal + "  times, or "
				+ drawPercentage + "%");
	}

	private BigDecimal getPercentageOfTime(int total, int totalGamesPlayed) {
		Float totalFloat = new Float(total);
		Float gamesPlayedTotalFloat = new Float(totalGamesPlayed);
		Float percentageFloat = (totalFloat / gamesPlayedTotalFloat) * 100;
		BigDecimal percentage = new BigDecimal(percentageFloat);
		percentage = percentage.setScale(2, BigDecimal.ROUND_UP);

		return percentage;
	}

	private void determineWinner() {
		String winner = "Nobody";
		if (oldGame.gameState == 2)
			winner = "New Game";
		if (oldGame.gameState == 3)
			winner = "Old Game";

		if (winner == "Nobody")
			drawTotal++;
		moveNumberTotals += oldGame.moveNumber;

		System.out.println(winner + " wins in " + oldGame.moveNumber
				+ " moves.");
		if (reporting)
			System.out.println(oldGame
					.returnPrintableBoard(LegacyGame.CR_CHARACTER));
	}

	private int oldGameMakesMove(int newGamePosition, int oldGamePosition) {
		if (!newGameWon()) oldGamePosition = oldGameMakesAMove(newGamePosition);
		if (reporting)
			System.out.println(oldGame.returnPrintableBoard(LegacyGame.CR_CHARACTER));

		if (oldGameWon()) setGameStateToOldGameWon();
		return oldGamePosition;
	}

	private int newGameMakesMove(int newGamePosition, int oldGamePosition) {
		if (!oldGameWon()) newGamePosition = newGameMakesAMove(); 
		if (reporting) System.out.println(oldGame.returnPrintableBoard(LegacyGame.CR_CHARACTER));
		if (newGameWon()) setGameStateToNewGameWon();
			
		return newGamePosition;
	}

	private int oldGameMakesAMove(int opponentPosition) {
		int x = (opponentPosition % Board.MAX_COLUMNS);
		int y = (opponentPosition - x) / Board.MAX_ROWS;
		int respondingPosition = oldGame.makeComputerMove(x, y, reporting);
		takeOldGamePosition(respondingPosition, LegacyGame.ZERO_MARK_FOR_COMPUTER,
				MAIN_LEVEL);
		return respondingPosition;
	}

	private int newGameMakesAMove() {
		newGame.setBoard(oldGame.gameBoard[0]);
		int respondingPosition = newGame.makeMove();
		takeOldGamePosition(respondingPosition, LegacyGame.X_MARK_FOR_PLAYER, MAIN_LEVEL);

		return respondingPosition;
	}

}