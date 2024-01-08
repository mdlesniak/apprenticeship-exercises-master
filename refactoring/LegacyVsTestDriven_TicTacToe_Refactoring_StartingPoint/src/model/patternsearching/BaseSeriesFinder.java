package model.patternsearching;

import model.gamestate.Board;
import model.gamestate.Board.SeriesSize;
import model.patterns.DirectionalCorridors;
import model.patterns.GroupOfDirectionalCorridors;
import model.patterns.ISeries;
import model.patterns.SeriesGroup;

public abstract class BaseSeriesFinder {
	protected Board board;
	protected GroupOfDirectionalCorridors listGroup;
	protected RoomForFiveSeriesVerifier roomForFiveSeriesVerifier;
	protected int bestPosition;
	protected int alternatePosition;
	private SeriesGroup allSeriesFound;

	public BaseSeriesFinder() {
		super();
	}

	public SeriesGroup getAllSeriesOfSize(SeriesSize size, int playerMark) {
		allSeriesFound = new SeriesGroup(board);

		for (int listGroupIndex = 0; listGroupIndex < listGroup.size(); listGroupIndex++) {
			DirectionalCorridors currentIndexList = listGroup.get(listGroupIndex);
			addAllQualifyingSeriesInThisIndexList(size, playerMark,
					currentIndexList);
		}

		return allSeriesFound;
	}

	private void addAllQualifyingSeriesInThisIndexList(SeriesSize size,
			int playerMark, DirectionalCorridors currentIndexList) {
		ISeries currentSeries;
		currentSeries = searchCorridorForSeriesOfSize(currentIndexList, size,
				playerMark);
		allSeriesFound = addQualifyingSeries(playerMark, currentSeries,
				currentIndexList);
	}

	public SeriesGroup addQualifyingSeries(int playerMark,
			ISeries currentSeries, DirectionalCorridors currentIndexList) {
		if (currentSeries.size() != 0) {
			addSeriesWithRoomForFive(playerMark, currentSeries, currentIndexList);
		}

		return allSeriesFound;
	}

	private void addSeriesWithRoomForFive(int playerMark, ISeries currentSeries,
			DirectionalCorridors currentIndexList) {
		if (roomForFiveSeriesVerifier.hasRoomForFive(currentSeries, currentIndexList,
				playerMark)) {
			allSeriesFound.add(currentSeries);
			currentSeries = addBlockingPositionsTo(currentSeries,
					currentIndexList);
		}
	}

	protected abstract ISeries searchCorridorForSeriesOfSize(
			DirectionalCorridors currentIndexList, SeriesSize size, int playerMark);

	protected abstract ISeries addBlockingPositionsTo(ISeries currentSeries,
			DirectionalCorridors currentIndexList);

	protected abstract int getBlockingPositionAfter(
			int lastSeriesPositionListIndex, DirectionalCorridors currentList);

	protected abstract int getBlockingPositionBefore(
			int startingPositionListIndex, DirectionalCorridors currentList);
}