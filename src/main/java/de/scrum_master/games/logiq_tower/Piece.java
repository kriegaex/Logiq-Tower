package de.scrum_master.games.logiq_tower;

public class Piece {
	private final char symbol;
	private final boolean isCentral;
	private final boolean isRotationalSymmetric;
	private final boolean[][] shape;
	private final boolean[][] shapeUpsideDown;
	private final String shapeText;
	private final String shapeUpsideDownText;
	private final int columns;
	private final int rows;

	public Piece(char symbol, boolean isCentral, boolean[][] shape) {
		this.symbol = symbol;
		this.isCentral = isCentral;
		this.shape = shape;

		rows = shape.length;
		columns = shape[0].length;

		shapeUpsideDown = new boolean[rows][columns];
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++)
				shapeUpsideDown[rows - 1 - row][columns - 1 - column] = shape[row][column];
		}

		StringBuilder builder = new StringBuilder();
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++)
				builder.append(shape[row][column] ? symbol : ' ');
			builder.append("\n");
		}
		shapeText = builder.toString();

		builder = new StringBuilder();
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++)
				builder.append(shapeUpsideDown[row][column] ? symbol : ' ');
			builder.append("\n");
		}
		shapeUpsideDownText = builder.toString();

		isRotationalSymmetric = shapeText.equals(shapeUpsideDownText);
	}

	public int getColumns() {
		return columns;
	}

	public int getRows() {
		return rows;
	}

	public boolean isCentral() {
		return isCentral;
	}

	public boolean isRotationalSymmetric() {
		return isRotationalSymmetric;
	}

	public String getShapeText() {
		return shapeText;
	}

	public String getShapeUpsideDownText() {
		return shapeUpsideDownText;
	}

	@Override
	public String toString() {
		return "Piece{" +
				"symbol=" + symbol +
				", isCentral=" + isCentral +
				", isRotationalSymmetric=" + isRotationalSymmetric +
				", columns=" + columns +
				", rows=" + rows +
				'}';
	}

	public static void main(String[] args) {
		for (Piece piece : PIECES) {
			System.out.println(piece + "\n");
			System.out.println(piece.getShapeText());
			if (!piece.isRotationalSymmetric())
				System.out.println(piece.getShapeUpsideDownText());
		}
	}
}
