package de.scrum_master.games.logiq_tower;

import java.util.Stack;

public class PlayingField {
	private static final char NULL_CHAR = '\u0000';

	private final int rows;
	private final int columns = 12;

	private boolean[][] isBlocked;
	private char[][] blockedByPiece;

	private Stack<Piece> piecesPlayed = new Stack<>();

	public PlayingField(int rows) throws IllegalFieldSizeException {
		if (rows < 2 || rows > 5)
			throw new IllegalFieldSizeException("illegal number of rows " + rows + ", must be [2..5]");
		this.rows = rows;

		isBlocked = new boolean[rows][columns];
		blockedByPiece = new char[rows][columns];
	}

	public boolean isSquareBlocked(int column, int row) {
		return isBlocked[row][column];
	}

	public boolean isSquareAvailable(int column, int row) {
		return !isBlocked[row][column];
	}
}
