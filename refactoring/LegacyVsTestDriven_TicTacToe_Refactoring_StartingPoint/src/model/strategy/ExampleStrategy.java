package model.strategy;

import model.gamestate.Board;
import model.gamestate.Move;
import model.gamestate.MoveGroup;
import model.gamestate.Board.SeriesSize;
import model.patternsearching.PatternFinder;
import controller.gameplay.TicTacToeGame.MoveScore;

public class ExampleStrategy implements IStrategy {
	private static final int NO_GOOD_MOVE_TO_MAKE = -1;
	private Board board;
	private PatternFinder patternFinder;

	public ExampleStrategy() {
		this.board = new Board();
		this.patternFinder = new PatternFinder(board);
	}

	public IStrategy newInstance() {
		return new ExampleStrategy();
	}

	public Board getBoard() {
		return board;
	}

	public boolean wonTheGame(int playerMark, SeriesSize winningSeriesSize) {
		return patternFinder.canFindSeriesOfSize(winningSeriesSize, playerMark);
	}

	public int makeMove() {
		Move highestScoringMove = getAllPossibleMoves().bestOverallMove();
		
		if (foundAGoodMove(highestScoringMove)) {
			return highestScoringMove.getPosition();
		}

		throw new RuntimeException("Could not find any moves to make.");
	}

	private boolean foundAGoodMove(Move highestScoringMove) {
		return ExampleStrategy.weFoundAGoodPosition(highestScoringMove.getPosition());
	}

	private MoveGroup getAllPossibleMoves() {
		MoveGroup moveGroup = new MoveGroup();

		moveGroup = gatherMovesForSeriesOfFive(moveGroup);
		moveGroup = gatherMovesForSeriesOfFour(moveGroup);
		moveGroup = gatherMovesForSeriesOfThree(moveGroup);
		moveGroup = gatherLowerPriorityMoves(moveGroup);
		return moveGroup;
	}

	private MoveGroup gatherMovesForSeriesOfFive(MoveGroup moveGroup) {
		gatherFiveSeriesCappingMoves(moveGroup);
		gatherFourSeriesCappingMoves(moveGroup);
		gatherSeriesOfFiveBlockingMoves(moveGroup);
		gatherSeriesOfFourBlockingMoves(moveGroup);
		gatherSeriesOfFiveGapFillingMoves(moveGroup);

		return moveGroup;
	}

	private MoveGroup gatherMovesForSeriesOfFour(MoveGroup moveGroup) {
		gatherSeriesOfThreeBlockingMoves(moveGroup);
		gatherSeriesOfFourGapBlockingMoves(moveGroup);
		gatherSeriesOfThreeCappingMoves(moveGroup);
		gatherSeriesOfFourGapFillingMoves(moveGroup);

		return moveGroup;
	}

	private MoveGroup gatherMovesForSeriesOfThree(MoveGroup moveGroup) {
		gatherSeriesOfThreeGapBlockingMoves(moveGroup);
		gatherPairBlockingMoves(moveGroup);
		gatherSeriesOfThreeGapFillingMoves(moveGroup);
		gatherPairCappingMoves(moveGroup);

		return moveGroup;
	}

	private MoveGroup gatherLowerPriorityMoves(MoveGroup moveGroup) {
		gatherShadowCornerMoves(moveGroup);
		findRandomMidBoardMove(moveGroup);
		findRandomOpenPosition(moveGroup);

		return moveGroup;
	}

	//TODO Extract duplication
	private void gatherSeriesOfFiveGapFillingMoves(MoveGroup moveGroup) {
		String message;
		message = "Series of five gap-filling position: ";
		moveGroup.add(new Move(patternFinder.getGapForGapSeriesOfSize(
				SeriesSize.FIVE, Board.HUMAN_PLAYER_MARK), MoveScore.NINE,
				message));
		
		moveGroup.add(new Move(patternFinder
				.getAlternateBlockingPositionForHumanWithSeries(), MoveScore.NINE,
				message));
	}

	private void gatherSeriesOfFourBlockingMoves(MoveGroup moveGroup) {
		String message;
		message = "Series of four blocking position: ";
		moveGroup.add(new Move(patternFinder
				.getBestBlockingPositionForHumanWithSeriesOfSize(SeriesSize.FOUR), MoveScore.NINE, message));
		moveGroup.add(new Move(patternFinder
				.getAlternateBlockingPositionForHumanWithSeries(), MoveScore.NINE,
				message));
	}

	private void gatherSeriesOfFiveBlockingMoves(MoveGroup moveGroup) {
		String message;
		message = "Series of five gap-blocking position: ";
		moveGroup
				.add(new Move(patternFinder.getGapForGapSeriesOfSize(
						SeriesSize.FIVE, Board.COMPUTER_PLAYER_MARK), MoveScore.TEN,
						message));
		moveGroup.add(new Move(patternFinder
				.getAlternateBlockingPositionForComputerWithSeries(), MoveScore.TEN,
				message));
		}

	private void gatherFourSeriesCappingMoves(MoveGroup moveGroup) {
		String message;
		message = "Series of four capping position: ";
		moveGroup.add(new Move(patternFinder
				.getBestBlockingPositionForComputerWithSeriesOfSize(SeriesSize.FOUR), MoveScore.TEN, message));
		moveGroup.add(new Move(patternFinder
				.getAlternateBlockingPositionForComputerWithSeries(), MoveScore.TEN,
				message));
	}

	private void gatherFiveSeriesCappingMoves(MoveGroup moveGroup) {
		String message;
		message = "Series of five capping position: ";
		moveGroup.add(new Move(patternFinder
				.getBestBlockingPositionForComputerWithSeriesOfSize(SeriesSize.FIVE), MoveScore.TEN, message));
		moveGroup.add(new Move(patternFinder
				.getAlternateBlockingPositionForComputerWithSeries(), MoveScore.TEN,
				message));
	}

	private void gatherSeriesOfFourGapFillingMoves(MoveGroup moveGroup) {
		String message;
		message = "Series of four gap-filling position: ";
		moveGroup.add(new Move(patternFinder.getGapForGapSeriesOfSize(
				SeriesSize.FOUR, Board.COMPUTER_PLAYER_MARK), MoveScore.SEVEN,
				message));
		moveGroup.add(new Move(patternFinder
				.getAlternateBlockingPositionForHumanWithSeries(), MoveScore.SEVEN,
				message));
	}

	private void gatherSeriesOfThreeCappingMoves(MoveGroup moveGroup) {
		String message;
		message = "Series of three capping position: ";
		moveGroup.add(new Move(patternFinder
				.getBestBlockingPositionForComputerWithSeriesOfSize(SeriesSize.THREE), MoveScore.SEVEN, message));
		moveGroup.add(new Move(patternFinder
				.getAlternateBlockingPositionForComputerWithSeries(), MoveScore.SEVEN,
				message));
	}

	private void gatherSeriesOfFourGapBlockingMoves(MoveGroup moveGroup) {
		String message;
		message = "Series of four gap-blocking position: ";
		moveGroup.add(new Move(patternFinder.getGapForGapSeriesOfSize(
				SeriesSize.FOUR, Board.HUMAN_PLAYER_MARK), MoveScore.EIGHT,
				message));
		moveGroup.add(new Move(patternFinder
				.getAlternateBlockingPositionForHumanWithSeries(), MoveScore.EIGHT,
				message));
	}

	private void gatherSeriesOfThreeBlockingMoves(MoveGroup moveGroup) {
		String message;
		message = "Series of three blocking position: ";
		moveGroup.add(new Move(patternFinder
				.getBestBlockingPositionForHumanWithSeriesOfSize(SeriesSize.THREE), MoveScore.EIGHT, message));
		moveGroup.add(new Move(patternFinder
				.getAlternateBlockingPositionForHumanWithSeries(), MoveScore.EIGHT,
				message));
	}

	private void gatherPairCappingMoves(MoveGroup moveGroup) {
		String message;
		message = "Pair-capping position: ";
		moveGroup.add(new Move(patternFinder
				.getBestBlockingPositionForComputerWithSeriesOfSize(SeriesSize.TWO), MoveScore.FIVE, message));
	}

	private void gatherSeriesOfThreeGapFillingMoves(MoveGroup moveGroup) {
		String message;
		message = "Series of three gap-filling position: ";
		moveGroup.add(new Move(patternFinder.getGapForGapSeriesOfSize(
				SeriesSize.THREE, Board.COMPUTER_PLAYER_MARK), MoveScore.FIVE,
				message));
	}

	private void gatherPairBlockingMoves(MoveGroup moveGroup) {
		String message;
		message = "Pair-blocking position: ";
		moveGroup.add(new Move(patternFinder
				.getBestBlockingPositionForHumanWithSeriesOfSize(SeriesSize.TWO), MoveScore.SIX, message));
	}

	private void gatherSeriesOfThreeGapBlockingMoves(MoveGroup moveGroup) {
		String message;
		message = "Series of three gap-blocking position: ";
		moveGroup.add(new Move(patternFinder.getGapForGapSeriesOfSize(
				SeriesSize.THREE, Board.HUMAN_PLAYER_MARK), MoveScore.SIX,
				message));
	}

	private void findRandomOpenPosition(MoveGroup moveGroup) {
		String message;
		message = "random open position: ";
		moveGroup.add(new Move(patternFinder.findRandomEmptyPosition(), MoveScore.ZERO, message));
	}

	private void findRandomMidBoardMove(MoveGroup moveGroup) {
		String message;
		message = "random open mid-board position: ";
		moveGroup.add(new Move(patternFinder.findRandomEmptyMidBoardPosition(),MoveScore.ONE, message));
	}

	private void gatherShadowCornerMoves(MoveGroup moveGroup) {
		String message;
		message = "Shadow corner near opposing player position: ";
		moveGroup.add(new Move(patternFinder.getBestShadowPosition(Board.COMPUTER_PLAYER_MARK), MoveScore.TWO, message));
	}

	private static boolean weFoundAGoodPosition(int position) {
		return position != NO_GOOD_MOVE_TO_MAKE;
	}

}
