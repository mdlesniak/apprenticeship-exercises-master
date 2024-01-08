package model.patternsearching;

import model.gamestate.Board;
import model.patterns.DirectionalCorridors;
import model.patterns.ISeries;

public class RoomForFiveSeriesVerifier {
	private Board board;
	private int thisPlayerMark;

	public RoomForFiveSeriesVerifier(Board board) {
		this.board = board;
	}

	public boolean hasRoomForFiveSeriesForPlayerMark(DirectionalCorridors list, int trialPosition,
			int thisPlayerMark) {
		this.thisPlayerMark = thisPlayerMark;
		int roomBefore = getRoomBeforeThisPosition(list, trialPosition);
		int roomAfter = getRoomAfterThisPosition(trialPosition, list);

		return ((roomBefore + roomAfter) >= Board.SERIES_OF_FIVE_SIZE);
	}

	private int getRoomAfterThisPosition(int trialPosition, DirectionalCorridors list) {
		int roomAfter = 0;
		int trialPositionIndex = list.getIndexFor(trialPosition);

		for (int i = trialPositionIndex + 1; i < list.size(); i++) {
			if (!board.within1DBoardPositionIndexBounds(i))
				return roomAfter;
			if (weHaveEncounteredOtherPlayerMark(list.get(i)))
				return roomAfter;

			roomAfter++;
		}

		return roomAfter;
	}

	private int getRoomBeforeThisPosition(DirectionalCorridors list, int trialPosition) {
		int roomBefore = 0;
		int trialPositionIndex = list.getIndexFor(trialPosition);

		for (int i = trialPositionIndex; i > -1; i--) {
			if (!board.within1DBoardPositionIndexBounds(i))
				return roomBefore;
			if (weHaveEncounteredOtherPlayerMark(list.get(i)))
				return roomBefore;

			roomBefore++;
		}

		return roomBefore;
	}

	private boolean weHaveEncounteredOtherPlayerMark(int boardIndex) {
		return board.getPosition(boardIndex) == board
				.getOppositePlayerMarkFor(thisPlayerMark);
	}

	public boolean hasRoomForFive(ISeries currentSeries, DirectionalCorridors currentList,
			int thisPlayerMark) {
		return hasRoomForFiveSeriesForPlayerMark(currentList, currentSeries.get(0),
				thisPlayerMark);
	}
}
