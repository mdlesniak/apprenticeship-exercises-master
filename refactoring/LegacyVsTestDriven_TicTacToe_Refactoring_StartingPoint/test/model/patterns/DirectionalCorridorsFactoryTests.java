package model.patterns;

import junit.framework.TestCase;
import model.gamestate.Board;

import org.junit.Test;

public class DirectionalCorridorsFactoryTests extends TestCase {

	private DirectionalCorridorsFactory playerFactory;
	private DirectionalCorridorsFactory computerFactory;
	private GroupOfDirectionalCorridors listGroup;
	
	public void setUp() throws Exception {
		playerFactory = new DirectionalCorridorsFactory(Board.HUMAN_PLAYER_MARK);
		computerFactory = new DirectionalCorridorsFactory(Board.COMPUTER_PLAYER_MARK);
		listGroup = new GroupOfDirectionalCorridors();
	}

	public void testNumber_of_rows_should_be_number_of_board_rows() {
		playerFactory.getIndexHorizontalRows(listGroup);
		assertEquals(Board.MAX_ROWS, listGroup.size());
	}

	@Test
	public void number_of_columns_should_be_number_of_board_columns() {
		playerFactory.getIndexHorizontalRows(listGroup);
		assertEquals(Board.MAX_COLUMNS, listGroup.size());
	}

	public void testNumber_of_diagonal_downs_for_five_in_row_should_be_11() {
		int topDiagonalDowns = Board.MAX_COLUMNS - 4;
		int bottomDiagonalDowns = Board.MAX_COLUMNS - 4;
		int commonDiagonalDowns = 1;
		
		playerFactory.getIndexDiagonalDowns(listGroup);
		assertEquals(topDiagonalDowns + bottomDiagonalDowns - commonDiagonalDowns, listGroup.size());
	}	

	public void testNumber_of_diagonal_ups_for_five_in_row_should_be_11() {
		int topDiagonalUps = Board.MAX_COLUMNS - 4;
		int bottomDiagonalUps = Board.MAX_COLUMNS - 4;
		int commonDiagonalUps = 1;
		
		playerFactory.getIndexDiagonalDowns(listGroup);
		assertEquals(topDiagonalUps + bottomDiagonalUps - commonDiagonalUps, listGroup.size());
	}	

	public void testNumber_of_diagonal_downs_for_six_in_row_should_be_99() {
		int topDiagonalDowns = Board.MAX_COLUMNS - 5;
		int bottomDiagonalDowns = Board.MAX_COLUMNS - 5;
		int commonDiagonalDowns = 1;
		
		computerFactory.getIndexDiagonalDowns(listGroup);
		assertEquals(topDiagonalDowns + bottomDiagonalDowns - commonDiagonalDowns, listGroup.size());
	}	
}
