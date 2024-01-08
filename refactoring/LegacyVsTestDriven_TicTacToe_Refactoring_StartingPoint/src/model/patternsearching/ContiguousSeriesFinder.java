package model.patternsearching;

import model.gamestate.Board;
import model.gamestate.Board.SeriesSize;
import model.patterns.DirectionalCorridors;
import model.patterns.DirectionalCorridorsFactory;
import model.patterns.ISeries;
import model.patterns.Series;
import model.patterns.SeriesGroup;

public class ContiguousSeriesFinder extends BaseSeriesFinder implements ISeriesFinder {
	private int playerMark;
	private ISeries seriesFound;

	public ContiguousSeriesFinder(Board board, int playerMark) {
		this.board = board;

		DirectionalCorridorsFactory factory = new DirectionalCorridorsFactory(playerMark);
		roomForFiveSeriesVerifier = new RoomForFiveSeriesVerifier(board);
		listGroup = factory.getAllIndexLists();
	}

	protected ISeries searchCorridorForSeriesOfSize(
			DirectionalCorridors currentCorridor, SeriesSize expectedSize, int playerMark) {
		this.playerMark = playerMark;
		
		resetSeriesFound();
		lookForSeriesOfExpectedSize(currentCorridor, expectedSize);
		if (ourSeriesSoFarIsNotBigEnough(expectedSize, seriesFound)) resetSeriesFound();

		return seriesFound;
	}

	private void lookForSeriesOfExpectedSize(DirectionalCorridors currentCorridor,
			SeriesSize expectedSize) {
		for (int i = 0; i < currentCorridor.size(); i++) {
			int positionContents = getPositionIfPartOfSeries(currentCorridor, i);

			if (weFoundLastPositionInExpectedSeries(expectedSize, positionContents)) break;
		}
	}

	private int getPositionIfPartOfSeries(DirectionalCorridors currentCorridor, int i) {
		int position = currentCorridor.get(i);
		int positionContents = board.getPosition(position);

		if (thisPositionIsPartOfSeries(positionContents)) {
			seriesFound.add(position);
		}
		return positionContents;
	}

	private boolean weFoundLastPositionInExpectedSeries(
			SeriesSize expectedSize, int positionContents) {
		boolean foundSeries = false;

		if (!thisPositionIsPartOfSeries(positionContents)) {
			if (ourSeriesSoFarIsBigEnough(expectedSize, seriesFound)) {
				foundSeries = true;
			} else {
				resetSeriesFound();
			}
		}

		return foundSeries;
	}

	private void resetSeriesFound() {
		seriesFound = new Series();
	}

	private boolean ourSeriesSoFarIsNotBigEnough(SeriesSize expectedSize,
			ISeries seriesFound) {
		return seriesFound.size() < expectedSize.getSize();
	}

	private boolean ourSeriesSoFarIsBigEnough(SeriesSize expectedSize,
			ISeries seriesFound) {
		return seriesFound.size() >= expectedSize.getSize();
	}

	private boolean thisPositionIsPartOfSeries(int positionContents) {
		return positionContents == playerMark;
	}
	
	//TODO Class boundary? What do YOU think?

	protected ISeries addBlockingPositionsTo(ISeries currentSeries,
			DirectionalCorridors currentList) {
		int startingPositionListIndex = currentList.getIndexFor(currentSeries
				.get(0));
		int lastSeriesPositionListIndex = currentList.getIndexFor(currentSeries
				.get(currentSeries.size() - 1));

		int startingBlockingPosition = getBlockingPositionBefore(
				startingPositionListIndex, currentList);
		int endingBlockingPosition = getBlockingPositionAfter(
				lastSeriesPositionListIndex, currentList);

		currentSeries.setStartingBlockingPosition(startingBlockingPosition);
		currentSeries.setEndingBlockingPosition(endingBlockingPosition);

		return currentSeries;
	}

	protected int getBlockingPositionAfter(int index, DirectionalCorridors currentList) {
		if (index < currentList.size() - 1) {
			int position = currentList.get(index + 1);

			if (board.getPosition(position) == Board.EMPTY)
				return position;
		}
		return -1;
	}

	protected int getBlockingPositionBefore(int index, DirectionalCorridors currentList) {
		if (index > 0) {
			int position = currentList.get(index - 1);

			if (board.getPosition(position) == Board.EMPTY)
				return position;
		}
		return -1;
	}

	public int getBestBlockingPositionForSeriesOfSize(SeriesSize size,
			int playerMark) {
		SeriesGroup allSeriesOfSize = getAllSeriesOfSize(size, playerMark);
		bestPosition = -1;
		alternatePosition = -1;

		for (int i = 0; i < allSeriesOfSize.size(); i++) {
			ISeries series = allSeriesOfSize.get(i);

			findBestBlockingPositions(series);
			if (foundGoodBlockingPosition())
				return bestPosition;
		}

		return bestPosition;
	}

	private boolean foundGoodBlockingPosition() {
		return bestPosition != -1;
	}

	private void findBestBlockingPositions(ISeries series) {
		if (seriesHasOpenStartingBlockingPosition(series)) {
			bestPosition = series.getStartingBlockingPosition();
			findAlternateBlockingPositionIfPossible(series);

		} else if (seriesHasOpenEndingBlockingPosition(series)) {
			bestPosition = series.getEndingBlockingPosition();
		}
	}

	private void findAlternateBlockingPositionIfPossible(ISeries series) {
		if (seriesHasOpenEndingBlockingPosition(series)) {
			alternatePosition = series.getEndingBlockingPosition();
		}
	}

	private boolean seriesHasOpenEndingBlockingPosition(ISeries series) {
		return series.getEndingBlockingPosition() != -1;
	}

	private boolean seriesHasOpenStartingBlockingPosition(ISeries series) {
		return series.getStartingBlockingPosition() != -1;
	}

	public int getAlternatePosition() {
		return alternatePosition;
	}

	public boolean containsSeriesOfSize(SeriesSize size, int playerMark) {
		SeriesGroup group = getAllSeriesOfSize(size, playerMark);
		return (group.size() != 0);
	}
}
