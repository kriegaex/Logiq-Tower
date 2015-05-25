package de.scrum_master.games.logiq_tower;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class PlayingField {
	public static final int MAX_ROWS = 5;
	public static final int MAX_COLUMNS = 12;

	private static final int MIN_ROWS = 2;
	private static final char DOT_CHAR = '\u00b7';

	private final int rows;
	private final int columns;

	private boolean[][] isBlocked;
	private char[][] blockedByPiece;

	private Stack<Move> moves = new Stack<>();
	private Set<Piece> pieces = new HashSet<>();

	private int freeCubesCount;

	private final boolean[][] floodFillArray;
	private int floodFillCount;

	public PlayingField(int rows) throws IllegalFieldSizeException {
		if (rows < MIN_ROWS || rows > MAX_ROWS) {
			throw new IllegalFieldSizeException(
				"illegal number of rows " + rows +
				", must be [" + MIN_ROWS + ".." + MAX_ROWS + "]"
			);
		}
		this.rows = rows;
		this.columns = MAX_COLUMNS;

		isBlocked = new boolean[rows][columns];
		blockedByPiece = new char[rows][columns];
		floodFillArray = new boolean[rows][columns];
		freeCubesCount = rows * columns;
	}

	public boolean isSquareBlocked(int row, int column) {
		return isBlocked[row][column];
	}

	public boolean isSquareAvailable(int row, int column) {
		return !isBlocked[row][column];
	}

	public int getMovesPlayed() {
		return moves.size();
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}

	public int getFreeCubesCount() {
		return freeCubesCount;
	}

	public Move push(Move move) {
		Piece piece = move.getPiece();
		int row = move.getRow();
		int column = move.getColumn();
		boolean isRotated = move.isRotated();

		for (int[] coordinates : piece.getBlockedPositionsAt(row, column, isRotated))
			isBlocked[coordinates[0]][coordinates[1]] = true;

		freeCubesCount -= piece.getCubeCount();
		piece.setAvailable(false);
		pieces.add(piece);
		return moves.push(move);
	}

	public Move pop() {
		Move move = moves.pop();
		Piece piece = move.getPiece();
		pieces.remove(piece);
		int row = move.getRow();
		int column = move.getColumn();
		boolean isRotated = move.isRotated();

		piece.setAvailable(true);
		freeCubesCount += piece.getCubeCount();
		for (int[] coordinates : piece.getBlockedPositionsAt(row, column, isRotated))
			isBlocked[coordinates[0]][coordinates[1]] = false;

		return move;
	}

	public boolean canBePlayed(Move move) {
		Piece piece = move.getPiece();
		if (pieces.contains(piece))
			return false;
		int row = move.getRow();
		int column = move.getColumn();
		boolean isRotated = move.isRotated();

		if (row + piece.getRows() > rows)
			return false;

		for (int arrayRow = 0; arrayRow < rows; arrayRow++)
			System.arraycopy(isBlocked[arrayRow], 0, floodFillArray[arrayRow], 0, columns);

		for (int[] coordinates : piece.getBlockedPositionsAt(row, column, isRotated)) {
			if (isBlocked[coordinates[0]][coordinates[1]])
				return false;
			floodFillArray[coordinates[0]][coordinates[1]] = true;
		}

		for (int floodFillRow = 0; floodFillRow < rows; floodFillRow++) {
			for (int floodFillColumn = 0; floodFillColumn < columns; floodFillColumn++) {
				if (floodFillArray[floodFillRow][floodFillColumn])
					continue;
				floodFillCount = 0;
				floodFill(floodFillColumn, floodFillRow);
				if (floodFillCount < 5 || !piece.isCentral() && floodFillCount % 5 != 0)
					return false;
			}
		}

		return true;
	}

	public String getText() {
		refreshText();
		StringBuilder builder = new StringBuilder();
		for (int row = 0; row < rows; row++) {
			builder.append(new String(blockedByPiece[row]));
			builder.append("\n");
		}
		return builder.toString();
	}

	private void refreshText() {
		for (Move move : moves) {
			int row = move.getRow();
			int column = move.getColumn();
			Piece piece = move.getPiece();
			char symbol = piece.getSymbol();
			boolean isRotated = move.isRotated();
			for (int[] coordinates : piece.getBlockedPositionsAt(row, column, isRotated))
				blockedByPiece[coordinates[0]][coordinates[1]] = symbol;
		}
	}

	private void floodFill_N(int x, int y)
	{
		if (--y < 0 || floodFillArray[y][x])
			return;
		floodFillArray[y][x] = true;
		floodFillCount++;
		floodFill_N(x, y);
		floodFill_E(x, y);
		floodFill_W(x, y);
	}

	private void floodFill_S(int x, int y)
	{
		if (++y >= rows || floodFillArray[y][x])
			return;
		floodFillArray[y][x] = true;
		floodFillCount++;
		floodFill_S(x, y);
		floodFill_E(x, y);
		floodFill_W(x, y);
	}

	private void floodFill_E(int x, int y)
	{
		if (++x >= columns)
			x -= columns;
		if (floodFillArray[y][x])
			return;
		floodFillArray[y][x] = true;
		floodFillCount++;
		floodFill_N(x, y);
		floodFill_S(x, y);
		floodFill_E(x, y);
	}

	private void floodFill_W(int x, int y)
	{
		if (--x < 0)
			x += columns;
		if (floodFillArray[y][x])
			return;
		floodFillArray[y][x] = true;
		floodFillCount++;
		floodFill_N(x, y);
		floodFill_S(x, y);
		floodFill_W(x, y);
	}

	public void floodFill(int x, int y)
	{
		if (x < 0 || x >= columns || y < 0 || y >= rows || floodFillArray[y][x])
			return;
		floodFillArray[y][x] = true;
		floodFillCount++;
		floodFill_N(x, y);
		floodFill_S(x, y);
		floodFill_E(x, y);
		floodFill_W(x, y);
	}

	public static void main(String[] args) throws IllegalFieldSizeException {
		PlayingField playingField = new PlayingField(2);
		playingField.push(Move.createMove(Piece.CENTRAL_PIECES.get(0), 0, 0, false));
		playingField.push(Move.createMove(Piece.OUTER_PIECES.get(4), 0, 4, true));
		playingField.push(Move.createMove(Piece.OUTER_PIECES.get(8), 2, 7, false));
		playingField.pop();
		playingField.push(Move.createMove(Piece.OUTER_PIECES.get(8), 2, 11, false));
		System.out.print(playingField.getText());
	}
}
