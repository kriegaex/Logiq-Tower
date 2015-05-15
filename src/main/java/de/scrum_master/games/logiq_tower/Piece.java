package de.scrum_master.games.logiq_tower;

import java.util.ArrayList;
import java.util.List;

public class Piece {
	public static final List<Piece> CENTRAL_PIECES = new ArrayList<>();
	public static final List<Piece> OUTER_PIECES   = new ArrayList<>();

	private final char symbol;
	private final boolean isCentral;
	private final boolean isPointSymmetric;
	private final boolean[][] shape;
	private final boolean[][] shapeRotated;
	private final String shapeText;
	private final String shapeRotatedText;
	private final int columns;
	private final int rows;

	static {
		CENTRAL_PIECES.add(new Piece(true, "00"));
		CENTRAL_PIECES.add(new Piece(true, "1 1"));
		CENTRAL_PIECES.add(new Piece(true, "2  2"));
		CENTRAL_PIECES.add(new Piece(true, "3   3"));
		CENTRAL_PIECES.add(new Piece(true, "4    4"));

		OUTER_PIECES.add(new Piece(false, "IIIII"));
		OUTER_PIECES.add(new Piece(false, "  Y ,YYYY"));
		OUTER_PIECES.add(new Piece(false, "   L,LLLL"));
		OUTER_PIECES.add(new Piece(false, "  NN,NNN "));
		OUTER_PIECES.add(new Piece(false, "U U,UUU"));
		OUTER_PIECES.add(new Piece(false, "QQQ,QQ "));
		OUTER_PIECES.add(new Piece(false, "  W, WW,WW "));
		OUTER_PIECES.add(new Piece(false, " F ,FFF,F  "));
		OUTER_PIECES.add(new Piece(false, "TTT, T , T "));
		OUTER_PIECES.add(new Piece(false, " SS, S ,SS "));
	}

	public Piece(char symbol, boolean isCentral, boolean[][] shape) {
		this.symbol = symbol;
		this.isCentral = isCentral;
		this.shape = shape;

		rows = shape.length;
		columns = shape[0].length;

		shapeRotated = new boolean[rows][columns];
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++)
				shapeRotated[rows - 1 - row][columns - 1 - column] = shape[row][column];
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
				builder.append(shapeRotated[row][column] ? symbol : ' ');
			builder.append("\n");
		}
		shapeRotatedText = builder.toString();

		isPointSymmetric = shapeText.equals(shapeRotatedText);
	}

	public Piece(boolean isCentral, String shapeText) {
		this(shapeText.replaceAll("[ ,]", "").charAt(0), isCentral, textToShape(shapeText));
	}

	private static boolean[][] textToShape(String shapeText) {
		String[] textRows = shapeText.split(",");
		int rows = textRows.length;
		int columns = textRows[0].length();
		boolean[][] shape = new boolean[rows][columns];
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				char character = textRows[row].charAt(column);
				shape[row][column] = character != ' ';
			}
		}
		return shape;
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

	public boolean isPointSymmetric() {
		return isPointSymmetric;
	}

	public String getShapeText() {
		return shapeText;
	}

	public String getShapeRotatedText() {
		return shapeRotatedText;
	}

	@Override
	public String toString() {
		return "Piece{" +
				"symbol=" + symbol +
				", isCentral=" + isCentral +
				", isPointSymmetric=" + isPointSymmetric +
				", columns=" + columns +
				", rows=" + rows +
				'}';
	}

	public static void main(String[] args) {
		System.out.println("CENTRAL PIECES");
		System.out.println("==============\n");
		for (Piece piece : CENTRAL_PIECES) {
//			System.out.println(piece + "\n");
			System.out.println(piece.getShapeText());
			if (!piece.isPointSymmetric())
				System.out.println(piece.getShapeRotatedText());
		}
		System.out.println("OUTER PIECES");
		System.out.println("=============\n");
		for (Piece piece : OUTER_PIECES) {
//			System.out.println(piece + "\n");
			System.out.println(piece.getShapeText());
			if (!piece.isPointSymmetric())
				System.out.println(piece.getShapeRotatedText());
		}
	}
}
