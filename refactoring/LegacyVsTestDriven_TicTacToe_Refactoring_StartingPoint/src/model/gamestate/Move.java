package model.gamestate;

import controller.gameplay.TicTacToeGame.MoveScore;

public class Move {
	private int position;
	private MoveScore score = MoveScore.NONE;

	public Move(int position, MoveScore score, String message) {
		this.position = position;
		this.score = score;
	}

	public int getPosition() {
		return position;
	}

	public int getScore() {
		return score.getScore();
	}

	public boolean isValidMove() {
		return getPosition() != -1;
	}
}
