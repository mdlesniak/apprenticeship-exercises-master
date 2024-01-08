package model.patternsearching;

import java.util.ArrayList;

import model.gamestate.Board;
import model.patterns.DirectionalCorridors;
import model.patterns.DirectionalCorridorsFactory;
import model.patterns.GroupOfDirectionalCorridors;
import model.patterns.ShadowPosition;

public class ShadowPositionFinder {
	private static final int MAX_OPEN_LINES = 4;
	private Board board;
	private RoomForFiveSeriesVerifier roomForFiveSeriesVerifier;
	private ArrayList<ShadowPosition> potentialShadowPositions;
	private int thisPlayerMark;

	public ShadowPositionFinder(Board board) {
		this.board = board;
		this.roomForFiveSeriesVerifier = new RoomForFiveSeriesVerifier(board);
		this.potentialShadowPositions = new ArrayList<ShadowPosition>();
	}

	public int getPositionInMostOpenSeriesNearOppositePlayer(int ourPlayerMark) {
		int theirPlayerMark = board.getOppositePlayerMarkFor(ourPlayerMark);
		ShadowPosition trialShadowPosition = new ShadowPosition(-1);

		ArrayList<Integer> allOppositePlayerPositions = board
				.getAllPositionsOccupiedByPlayer(theirPlayerMark);

		trialShadowPosition = findOppositePlayerPositionWithHighScoringShadow(
				ourPlayerMark, trialShadowPosition, allOppositePlayerPositions);
		return trialShadowPosition.getPosition();
	}

	private ShadowPosition findOppositePlayerPositionWithHighScoringShadow(
			int playerMark, ShadowPosition trialShadowPosition,
			ArrayList<Integer> allOppositePlayerPositions) {
		for (int i = 0; i < allOppositePlayerPositions.size(); i++) {
			int oppositePlayerPosition = allOppositePlayerPositions.get(i);

			potentialShadowPositions = tryAllFourShadowCornersOfPlayerPosition(
					oppositePlayerPosition, playerMark);
			if (potentialShadowPositions.size() == 0)
				return new ShadowPosition(-1);
			trialShadowPosition = findHighestScoringShadowPosition(
					trialShadowPosition, potentialShadowPositions);
			potentialShadowPositions.remove(trialShadowPosition);

			if (trialShadowPosition.getScore() == MAX_OPEN_LINES)
				break;
		}
		return trialShadowPosition;
	}

	private ShadowPosition findHighestScoringShadowPosition(
			ShadowPosition trialShadowPosition,
			ArrayList<ShadowPosition> potentialShadowPositionsForThisPlayerPosition) {
		for (int j = 0; j < potentialShadowPositionsForThisPlayerPosition
				.size(); j++) {
			ShadowPosition thisPosition = potentialShadowPositionsForThisPlayerPosition
					.get(j);

			if (thisPosition.getScore() > trialShadowPosition.getScore()) {
				trialShadowPosition = thisPosition;
			}

			if (trialShadowPosition.getScore() == MAX_OPEN_LINES)
				break;
		}
		return trialShadowPosition;
	}

	private ArrayList<ShadowPosition> tryAllFourShadowCornersOfPlayerPosition(
			int oppositePlayerPosition, int thisPlayerMark) {
		ArrayList<ShadowPosition> potentialPositions = new ArrayList<ShadowPosition>();

		addDiagonalDownShadowIfPossible(oppositePlayerPosition, thisPlayerMark,
				potentialPositions);
		addDiagonalUpAfterShadowIfPossible(oppositePlayerPosition,
				thisPlayerMark, potentialPositions);
		addDiagonalDownBeforePositionIfPossible(oppositePlayerPosition,
				thisPlayerMark, potentialPositions);
		addDiagonalUpBeforeShadowIfPossible(oppositePlayerPosition,
				thisPlayerMark, potentialPositions);

		return potentialPositions;
	}

	private void addDiagonalUpBeforeShadowIfPossible(
			int oppositePlayerPosition, int thisPlayerMark,
			ArrayList<ShadowPosition> potentialPositions) {
		ShadowPosition position;
		position = getPositionNearTheirPosition(oppositePlayerPosition,
				DirectionalCorridorsFactory.DIAGONAL_UP_BEFORE_INCREMENT, thisPlayerMark);
		if (position.getPosition() != -1) {
			potentialPositions.add(position);
		}
	}

	private void addDiagonalDownBeforePositionIfPossible(
			int oppositePlayerPosition, int thisPlayerMark,
			ArrayList<ShadowPosition> potentialPositions) {
		ShadowPosition position;
		position = getPositionNearTheirPosition(oppositePlayerPosition,
				DirectionalCorridorsFactory.DIAGONAL_DOWN_BEFORE_INCREMENT, thisPlayerMark);
		if (position.getPosition() != -1) {
			potentialPositions.add(position);
		}
	}

	private void addDiagonalUpAfterShadowIfPossible(int oppositePlayerPosition,
			int thisPlayerMark, ArrayList<ShadowPosition> potentialPositions) {
		ShadowPosition position;
		position = getPositionNearTheirPosition(oppositePlayerPosition,
				DirectionalCorridorsFactory.DIAGONAL_UP_AFTER_INCREMENT, thisPlayerMark);
		if (position.getPosition() != -1) {
			potentialPositions.add(position);
		}
	}

	private void addDiagonalDownShadowIfPossible(int oppositePlayerPosition,
			int thisPlayerMark, ArrayList<ShadowPosition> potentialPositions) {
		ShadowPosition position;
		position = getPositionNearTheirPosition(oppositePlayerPosition,
				DirectionalCorridorsFactory.DIAGONAL_DOWN_AFTER_INCREMENT, thisPlayerMark);
		if (position.getPosition() != -1) {
			potentialPositions.add(position);
		}
	}

	public ShadowPosition getPositionNearTheirPosition(int playerPosition,
			int increment, int thisPlayerMark) {
		ShadowPosition trialPosition = new ShadowPosition(
				getIndexOfTrialShadowPosition(playerPosition, increment));

		if (positionIsOutOfBounds(trialPosition))
			return new ShadowPosition(-1);
		if (positionIsEmpty(trialPosition))
			return new ShadowPosition(-1);

		int numberOfOpenLinesFound = openLinesWithPentaRoomForPosition(
				trialPosition, thisPlayerMark);
		trialPosition.setScore(numberOfOpenLinesFound);
		if (numberOfOpenLinesFound == 0)
			return new ShadowPosition(-1);

		return trialPosition;
	}

	private int getIndexOfTrialShadowPosition(int playerPosition, int increment) {
		return playerPosition + increment;
	}

	private boolean positionIsEmpty(ShadowPosition trialPosition) {
		return !board.positionIsEmpty(trialPosition.getPosition());
	}

	private boolean positionIsOutOfBounds(ShadowPosition trialPosition) {
		return !board.within1DBoardPositionIndexBounds(trialPosition
				.getPosition());
	}

	public int openLinesWithPentaRoomForPosition(ShadowPosition trialPosition,
			int thisPlayerMark) {
		this.thisPlayerMark = thisPlayerMark;
		int openLinesThroughThisPoint = 0;
		GroupOfDirectionalCorridors allIndexLists = getAllIndexListGroups();

		for (int i = 0; i < allIndexLists.size(); i++) {
			DirectionalCorridors currentIndexList = allIndexLists.get(i);

			openLinesThroughThisPoint = incrementOpenLinesCountIfThisLineIsOpen(
					trialPosition, openLinesThroughThisPoint, currentIndexList);
		}

		return openLinesThroughThisPoint;
	}

	private int incrementOpenLinesCountIfThisLineIsOpen(
			ShadowPosition trialPosition, int openLinesThroughThisPoint,
			DirectionalCorridors list) {
		if (list.contains(trialPosition.getPosition())) {
			if (roomForFiveSeriesVerifier.hasRoomForFiveSeriesForPlayerMark(list, trialPosition
					.getPosition(), thisPlayerMark)) {
				openLinesThroughThisPoint++;
			}
		}
		return openLinesThroughThisPoint;
	}

	private GroupOfDirectionalCorridors getAllIndexListGroups() {
		DirectionalCorridorsFactory listFactory = new DirectionalCorridorsFactory(Board.HUMAN_PLAYER_MARK);
		GroupOfDirectionalCorridors allIndexLists = listFactory.getAllIndexLists();
		return allIndexLists;
	}

}
