package de.scrum_master.games.logiq_tower;

public class Piece {
	private final char symbol;
	private final boolean[][] shape;
	private final boolean[][] shapeUpsideDown;
	private final int columns;
	private final int rows;

	public Piece(char symbol, boolean[][] shape) {
		this.symbol = symbol;
		this.shape = shape;

		columns = shape.length;
		rows = shape[0].length;

		shapeUpsideDown = new boolean[columns][rows];
		for (int column = 0; column < columns; column++) {
			for (int row = 0; row < rows; row++)
				shapeUpsideDown[column][rows - 1 - row] = shape[column][row];
		}
	}

	public int getColumns() {
		return columns;
	}

	public int getRows() {
		return rows;
	}

	public String shapeToString() {
		StringBuilder builder = new StringBuilder();
		for (int column = 0; column < columns; column++) {
			for (int row = 0; row < rows; row++)
				builder.append(shape[column][row] ? symbol : ' ');
			builder.append("\n");
		}
		return builder.toString();
	}

	public String shapeUpsideDownToString() {
		StringBuilder builder = new StringBuilder();
		for (int column = 0; column < columns; column++) {
			for (int row = 0; row < rows; row++)
				builder.append(shapeUpsideDown[column][row] ? symbol : ' ');
			builder.append("\n");
		}
		return builder.toString();
	}
}
