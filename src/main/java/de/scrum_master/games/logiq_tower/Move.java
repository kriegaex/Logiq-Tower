package de.scrum_master.games.logiq_tower;

public class Move {
	private Piece piece;
	private int row;
	private int column;
	private boolean isRotated;

	public Move(Piece piece, int row, int column, boolean isRotated) {
		this.piece = piece;
		this.row = row;
		this.column = column;
		this.isRotated = isRotated;
	}

	public Piece getPiece() {
		return piece;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public boolean isRotated() {
		return isRotated;
	}
}
