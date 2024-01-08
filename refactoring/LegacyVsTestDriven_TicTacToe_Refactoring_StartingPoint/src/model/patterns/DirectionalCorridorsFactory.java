package model.patterns;

import model.gamestate.Board;

public class DirectionalCorridorsFactory {
	private int winningSeriesSize;

	private int getFirstSquareOfLastLowerDiagonalUp() {
		return Board.MAX_COLUMNS * Board.MAX_ROWS - winningSeriesSize;
	}

	private int getFirstSquareOfFirstLowerDiagonalUp() {
		return (Board.MAX_ROWS * (Board.MAX_COLUMNS - 1)) + 1;
	}

	private int getFirstSquareOfFirstUpperDiagonalUp() {
		return Board.MAX_COLUMNS * (winningSeriesSize -1);
	}
	
	private int getFirstSquareOfFirstLowerDiagonalDown() {
		return (Board.MAX_ROWS - winningSeriesSize) * Board.MAX_COLUMNS;
	}
	
	public DirectionalCorridorsFactory(int playerMark) {
		if (playerMark == Board.HUMAN_PLAYER_MARK)
			this.winningSeriesSize = 5;
		else if (playerMark == Board.COMPUTER_PLAYER_MARK)
			this.winningSeriesSize = 6;
		else
			throw new RuntimeException("Invalid playerMark: " + playerMark);
	}
	
	public static final int DIAGONAL_UP_AFTER_INCREMENT = -(Board.MAX_COLUMNS - 1);
	public static final int DIAGONAL_UP_BEFORE_INCREMENT = (Board.MAX_COLUMNS - 1);
	public static final int DIAGONAL_DOWN_AFTER_INCREMENT = (Board.MAX_COLUMNS + 1);
	public static final int DIAGONAL_DOWN_BEFORE_INCREMENT = -(Board.MAX_COLUMNS + 1);
	public static final int VERTICAL_AFTER_INCREMENT = Board.MAX_COLUMNS;
	public static final int VERTICAL_BEFORE_INCREMENT = -Board.MAX_COLUMNS;
	public static final int HORIZONTAL_AFTER_INCREMENT = 1;
	public static final int HORIZONTAL_BEFORE_INCREMENT = -1;

	public GroupOfDirectionalCorridors getAllIndexLists() {
		GroupOfDirectionalCorridors listGroup = new GroupOfDirectionalCorridors();

		listGroup = getIndexHorizontalRows(listGroup);
		listGroup = getIndexVerticalColumns(listGroup);
		listGroup = getIndexDiagonalDowns(listGroup);
		listGroup = getIndexDiagonalUps(listGroup);

		return listGroup;
	}

	public GroupOfDirectionalCorridors getIndexHorizontalRows(GroupOfDirectionalCorridors listGroup) {
		for (int row = 0; row < Board.MAX_ROWS; row++) {
			DirectionalCorridors list = new DirectionalCorridors();

			getAllSpacesInThisRow(row, list);
			listGroup.add(list);
		}
		return listGroup;
	}

	private void getAllSpacesInThisRow(int row, DirectionalCorridors list) {
		int spaceIndex;
		for (int space = 0; space < Board.MAX_COLUMNS; space++) {
			spaceIndex = row * Board.MAX_COLUMNS + space;

			list.add(spaceIndex);
		}
	}

	public GroupOfDirectionalCorridors getIndexVerticalColumns(GroupOfDirectionalCorridors listGroup) {
		for (int column = 0; column < Board.MAX_COLUMNS; column++) {
			DirectionalCorridors list = new DirectionalCorridors();

			getAllSpacesInThisColumn(column, list);
			listGroup.add(list);
		}

		return listGroup;
	}

	private void getAllSpacesInThisColumn(int column, DirectionalCorridors list) {
		int spaceIndex;
		for (int space = 0; space < Board.MAX_BOARD_SIZE; space = space	+ Board.MAX_ROWS) {
			spaceIndex = column + space;

			list.add(spaceIndex);
		}
	}

	public GroupOfDirectionalCorridors getIndexDiagonalDowns(
			GroupOfDirectionalCorridors listGroup) {
		assembleLowerBoardDiagonalDowns(listGroup);
		assembleUpperBoardDiagonalDowns(listGroup);

		return listGroup;
	}

	public GroupOfDirectionalCorridors getIndexDiagonalUps(GroupOfDirectionalCorridors listGroup) {
		assembleUpperBoardDiagonalUps(listGroup);
		assembleLowerBoardDiagonalUps(listGroup);

		return listGroup;
	}

	private void assembleUpperBoardDiagonalDowns(
			GroupOfDirectionalCorridors listGroup) {
		int currentDiagonalDownSize = 9;

		for (int diagonalGroup = 1; diagonalGroup < (Board.MAX_COLUMNS-winningSeriesSize+1); diagonalGroup++) {
			DirectionalCorridors list = new DirectionalCorridors();

			getAllSpacesInThisUpperBoardDiagonalDown(currentDiagonalDownSize,
					diagonalGroup, list);
			listGroup.add(list);
			currentDiagonalDownSize--;
		}
	}

	private void getAllSpacesInThisUpperBoardDiagonalDown(
			int currentDiagonalDownSize, int diagonalGroup, DirectionalCorridors list) {
		int spaceIndex;
		for (int space = 0; space < currentDiagonalDownSize; space++) {
			spaceIndex = diagonalGroup
					+ (space * DIAGONAL_DOWN_AFTER_INCREMENT);

			list.add(spaceIndex);
		}
	}

	private int assembleLowerBoardDiagonalDowns(
			GroupOfDirectionalCorridors listGroup) {
		int currentDiagonalDownSize = winningSeriesSize;

		for (int diagonalGroup = getFirstSquareOfFirstLowerDiagonalDown(); diagonalGroup > -1; diagonalGroup = diagonalGroup
				- VERTICAL_AFTER_INCREMENT) {
			DirectionalCorridors list = new DirectionalCorridors();

			getAllSpacesInThisLowerBoardDiagonalDown(currentDiagonalDownSize,
					diagonalGroup, list);

			listGroup.add(list);
			currentDiagonalDownSize++;
		}
		return currentDiagonalDownSize;
	}

	private void getAllSpacesInThisLowerBoardDiagonalDown(
			int currentDiagonalDownSize, int diagonalGroup, DirectionalCorridors list) {
		int spaceIndex;
		for (int space = 0; space < currentDiagonalDownSize; space++) {
			spaceIndex = diagonalGroup + (space * DIAGONAL_DOWN_AFTER_INCREMENT);

			list.add(spaceIndex);
		}
	}

	private void assembleLowerBoardDiagonalUps(GroupOfDirectionalCorridors listGroup) {
		int currentDiagonalUpSize = 9;

		for (int diagonalGroup = getFirstSquareOfFirstLowerDiagonalUp(); diagonalGroup <= getFirstSquareOfLastLowerDiagonalUp(); diagonalGroup++) {
			DirectionalCorridors list = new DirectionalCorridors();

			getAllSpacesInThisLowerBoardDiagonalUp(currentDiagonalUpSize,
					diagonalGroup, list);

			listGroup.add(list);
			currentDiagonalUpSize--;
		}
	}

	private void getAllSpacesInThisLowerBoardDiagonalUp(
			int currentDiagonalUpSize, int diagonalGroup, DirectionalCorridors list) {
		int spaceIndex;
		for (int space = 0; space < currentDiagonalUpSize; space++) {
			spaceIndex = diagonalGroup - (space * DIAGONAL_UP_BEFORE_INCREMENT);

			list.add(spaceIndex);
		}
	}

	private int assembleUpperBoardDiagonalUps(GroupOfDirectionalCorridors listGroup) {
		int currentDiagonalUpSize = winningSeriesSize;

		for (int diagonalGroup = getFirstSquareOfFirstUpperDiagonalUp(); diagonalGroup < Board.MAX_BOARD_SIZE; diagonalGroup = diagonalGroup
				+ VERTICAL_AFTER_INCREMENT) {
			DirectionalCorridors list = new DirectionalCorridors();

			getAllSpacesInThisUpperBoardDiagonalUp(currentDiagonalUpSize,
					diagonalGroup, list);
			listGroup.add(list);
			currentDiagonalUpSize++;
		}
		return currentDiagonalUpSize;
	}

	private void getAllSpacesInThisUpperBoardDiagonalUp(
			int currentDiagonalUpSize, int diagonalGroup, DirectionalCorridors list) {
		int spaceIndex;
		for (int space = 0; space < currentDiagonalUpSize; space++) {
			spaceIndex = diagonalGroup - (space * DIAGONAL_UP_BEFORE_INCREMENT);

			list.add(spaceIndex);
		}
	}
}
