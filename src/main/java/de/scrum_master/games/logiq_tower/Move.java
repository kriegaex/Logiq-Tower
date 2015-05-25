package de.scrum_master.games.logiq_tower;

import java.util.*;

public class Move {
	private static final Map<Piece, Move> instancePool = new HashMap<>();

	private Piece piece;
	private int row;
	private int column;
	private boolean isRotated;

	static {
		for (Piece piece : Piece.CENTRAL_PIECES)
			instancePool.put(piece, new Move(piece, 0, 0, false));
		for (Piece piece : Piece.OUTER_PIECES)
			instancePool.put(piece, new Move(piece, 0, 0, false));
	}

	private Move(Piece piece, int row, int column, boolean isRotated) {
		this.piece = piece;
		this.row = row;
		this.column = column;
		this.isRotated = isRotated;
	}

	public static Move createMove(Piece piece, int row, int column, boolean isRotated) {
		Move move = instancePool.get(piece);
		move.row = row;
		move.column = column;
		move.isRotated = isRotated;
		return move;
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
