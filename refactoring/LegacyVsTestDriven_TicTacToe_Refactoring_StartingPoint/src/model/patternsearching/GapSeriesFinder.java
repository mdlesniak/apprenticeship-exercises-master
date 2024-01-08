package model.patternsearching;

import model.gamestate.Board;
import model.gamestate.Board.SeriesSize;
import model.patterns.DirectionalCorridors;
import model.patterns.GapSeries;
import model.patterns.ISeries;
import model.patterns.Series;

public class GapSeriesFinder extends ContiguousSeriesFinder implements ISeriesFinder {
	private int playerMark;
	private ISeries seriesFound;
	private SeriesSize expectedSize;

	public GapSeriesFinder(Board board) {
		super(board, Board.HUMAN_PLAYER_MARK);
	}

	protected ISeries searchCorridorForSeriesOfSize(
			DirectionalCorridors currentIndexList, SeriesSize expectedSize, int playerMark) {
		this.playerMark = playerMark;
		this.seriesFound = new Series();
		this.expectedSize = expectedSize;

		for (int i = 0; i < currentIndexList.size() - expectedSize.getSize()
				+ 1; i++) {
			seriesFound = addSeriesIfItQualifies(getNextCandidateListOfExpectedSize(
					currentIndexList, i));
		}

		return seriesFound;
	}

	private ISeries addSeriesIfItQualifies(DirectionalCorridors candidateIndexList) {
		int firstPosition = candidateIndexList.get(0);
		int lastPosition = candidateIndexList
				.get(candidateIndexList.size() - 1);

		if (weHaveASeries(firstPosition, lastPosition)) {
			seriesFound = qualifyingGapSeries(expectedSize, candidateIndexList);
		}
		return seriesFound;
	}

	private ISeries qualifyingGapSeries(SeriesSize expectedSize,
			DirectionalCorridors candidateIndexList) {
		if ((candidateListContainsBoardMark(candidateIndexList, Board.EMPTY) && (!candidateListContainsBoardMark(
				candidateIndexList, board.getOppositePlayerMarkFor(playerMark))))) {

			seriesFound = createGapSeriesIfIndexListIsOfExpectedSize(candidateIndexList);
		}

		return seriesFound;
	}

	private ISeries createGapSeriesIfIndexListIsOfExpectedSize(
			DirectionalCorridors candidateIndexList) {
		if (candidateIndexList.size() == expectedSize.getSize()) {
			seriesFound = createGapSeries(candidateIndexList);
		}
		return seriesFound;
	}

	protected DirectionalCorridors getNextCandidateListOfExpectedSize(
			DirectionalCorridors currentIndexList, int currentStartingPositionIndex) {
		DirectionalCorridors candidateList = new DirectionalCorridors();
		for (int j = currentStartingPositionIndex; j < currentStartingPositionIndex
				+ expectedSize.getSize(); j++) {
			candidateList.add(currentIndexList.get(j));
		}

		return candidateList;
	}

	private boolean weHaveASeries(int firstPosition, int lastPosition) {
		return (positionEquals(firstPosition, playerMark))
				&& positionEquals(lastPosition, playerMark);
	}

	protected ISeries createGapSeries(DirectionalCorridors candidateList) {
		GapSeries series = new GapSeries();

		for (int i = 0; i < candidateList.size(); i++) {
			int position = candidateList.get(i);
			series.add(position);
			markPositionAsEmptyIfEmpty(series, position);
		}
		return series;
	}

	private void markPositionAsEmptyIfEmpty(GapSeries series, int position) {
		if (board.getPosition(position) == Board.EMPTY) {
			series.addEmptyPosition(position);
		}
	}

	protected boolean candidateListContainsBoardMark(DirectionalCorridors candidateList,
			int mark) {
		for (int positionIndex = 0; positionIndex < candidateList.size(); positionIndex++) {
			if (positionContainsBoardMark(positionIndex, mark, candidateList)) {
				return true;
			}
		}
		return false;
	}

	private boolean positionContainsBoardMark(int positionIndex, int mark,
			DirectionalCorridors candidateList) {
		return board.getPosition(candidateList.get(positionIndex)) == mark;
	}

	protected boolean positionEquals(int position, int playerMark) {
		boolean positionContainsMark = board.getPosition(position) == playerMark;
		return positionContainsMark;
	}

	protected ISeries addBlockingPositionsTo(ISeries currentSeries,
			DirectionalCorridors currentList) {
		currentSeries.setBlockingPositionsDependingOnNumberOfEmptySpaces();

		return currentSeries;
	}

}
