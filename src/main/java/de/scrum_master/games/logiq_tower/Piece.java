package de.scrum_master.games.logiq_tower;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Piece {
	public static final List<Piece> CENTRAL_PIECES = new ArrayList<>();
	public static final List<Piece> OUTER_PIECES   = new ArrayList<>();
	public static final Set<Piece> CENTRAL_PIECES_AVAILABLE = new HashSet<>();
	public static final Set<Piece> OUTER_PIECES_AVAILABLE   = new HashSet<>();

	private final char symbol;
	private final boolean isCentral;
	private final boolean isPointSymmetric;
	private final boolean[][] shape;
	private final boolean[][] shapeRotated;
	private final int[][][][] blockedPositions = new int[PlayingField.MAX_ROWS][PlayingField.MAX_COLUMNS][][];
	private final int[][][][] blockedPositionsRotated = new int[PlayingField.MAX_ROWS][PlayingField.MAX_COLUMNS][][];
	private final String shapeText;
	private final String shapeRotatedText;
	private final int columns;
	private final int rows;
	private final int cubeCount;
	private boolean available = true;

	static {
		// Central pieces have the following properties:
		//  - height == 1, i.e. they always block cubes in only one column
		//  - exactly one CP per column
		//  - always point-symmetric
		//  - always need 2 cubes (possibly non-contiguous) of free space
		CENTRAL_PIECES.add(new Piece(true, "00"));
		CENTRAL_PIECES.add(new Piece(true, "1·1"));
		CENTRAL_PIECES.add(new Piece(true, "2··2"));
		CENTRAL_PIECES.add(new Piece(true, "3···3"));
		CENTRAL_PIECES.add(new Piece(true, "4····4"));

		for (Piece piece : CENTRAL_PIECES)
			CENTRAL_PIECES_AVAILABLE.add(piece);

		// Outer pieces have the following properties:
		//  - 1 <= height <= 3, i.e. they can block cubes in multiple columns
		//  - often not point-symmetric
		//  - always need 5 cubes (contiguous) of free space-
		OUTER_PIECES.add(new Piece(false, "IIIII"));
		OUTER_PIECES.add(new Piece(false, "··Y·,YYYY"));
		OUTER_PIECES.add(new Piece(false, "···L,LLLL"));
		OUTER_PIECES.add(new Piece(false, "··NN,NNN·"));
		OUTER_PIECES.add(new Piece(false, "U·U,UUU"));
		OUTER_PIECES.add(new Piece(false, "QQQ,QQ·"));
		OUTER_PIECES.add(new Piece(false, "··W,·WW,WW·"));
		OUTER_PIECES.add(new Piece(false, "·F·,FFF,F··"));
		OUTER_PIECES.add(new Piece(false, "TTT,·T·,·T·"));
		OUTER_PIECES.add(new Piece(false, "·SS,·S·,SS·"));

		for (Piece piece : OUTER_PIECES)
			OUTER_PIECES_AVAILABLE.add(piece);
	}

	public char getSymbol() {
		return symbol;
	}

	public boolean[][] getShape() {
		return shape;
	}

	public boolean[][] getShapeRotated() {
		return shapeRotated;
	}

	public Piece(char symbol, boolean isCentral, boolean[][] shape) {
		this.symbol = symbol;
		this.isCentral = isCentral;
		this.shape = shape;

		rows = shape.length;
		columns = shape[0].length;

		int cubeCount = 0;
		shapeRotated = new boolean[rows][columns];
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				shapeRotated[rows - 1 - row][columns - 1 - column] = shape[row][column];
				if (shape[row][column])
					cubeCount++;
			}
		}
		this.cubeCount = cubeCount;

		StringBuilder builder = new StringBuilder();
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++)
				builder.append(shape[row][column] ? symbol : '·');
			builder.append("\n");
		}
		shapeText = builder.toString();

		builder = new StringBuilder();
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++)
				builder.append(shapeRotated[row][column] ? symbol : '·');
			builder.append("\n");
		}
		shapeRotatedText = builder.toString();

		isPointSymmetric = shapeText.equals(shapeRotatedText);

		for (int fieldRow = 0; fieldRow < PlayingField.MAX_ROWS; fieldRow++) {
			if (fieldRow + rows > PlayingField.MAX_ROWS)
				continue;
			for (int fieldColumn = 0; fieldColumn < PlayingField.MAX_COLUMNS; fieldColumn++) {
				blockedPositions[fieldRow][fieldColumn] = new int[cubeCount][2];
				int cubeIndex = 0;
				for (int row = 0; row < rows; row++) {
					for (int column = 0; column < columns; column++) {
						if (shape[row][column]) {
							blockedPositions[fieldRow][fieldColumn][cubeIndex][0] = fieldRow + row;
							blockedPositions[fieldRow][fieldColumn][cubeIndex][1] = (fieldColumn + column) % PlayingField.MAX_COLUMNS;
							cubeIndex++;
						}
					}
				}
				blockedPositionsRotated[fieldRow][fieldColumn] = new int[cubeCount][2];
				cubeIndex = 0;
				for (int row = 0; row < rows; row++) {
					for (int column = 0; column < columns; column++) {
						if (shapeRotated[row][column]) {
							blockedPositionsRotated[fieldRow][fieldColumn][cubeIndex][0] = fieldRow + row;
							blockedPositionsRotated[fieldRow][fieldColumn][cubeIndex][1] = (fieldColumn + column) % PlayingField.MAX_COLUMNS;
							cubeIndex++;
						}
					}
				}
			}
		}

	}

	public Piece(boolean isCentral, String shapeText) {
		this(shapeText.replaceAll("[·,]", "").charAt(0), isCentral, textToShape(shapeText));
	}

	private static boolean[][] textToShape(String shapeText) {
		String[] textRows = shapeText.split(",");
		int rows = textRows.length;
		int columns = textRows[0].length();
		boolean[][] shape = new boolean[rows][columns];
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				char character = textRows[row].charAt(column);
				shape[row][column] = character != '·';
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
			", cubeCount=" + cubeCount +
			'}';
	}

	public static void main(String[] args) {
		System.out.println("CENTRAL PIECES");
		System.out.println("==============\n");
		for (Piece piece : CENTRAL_PIECES) {
			System.out.println(piece + "\n");
			System.out.println(piece.getShapeText());
			if (!piece.isPointSymmetric())
				System.out.println(piece.getShapeRotatedText());
		}
		System.out.println("OUTER PIECES");
		System.out.println("=============\n");
		for (Piece piece : OUTER_PIECES) {
			System.out.println(piece + "\n");
			System.out.println(piece.getShapeText());
			if (!piece.isPointSymmetric())
				System.out.println(piece.getShapeRotatedText());
		}
	}
}
