package de.scrum_master.games.logiq_tower;

import java.util.ArrayList;
import java.util.List;

public class Piece {
	public static final List<Piece> PIECES = new ArrayList<>();

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
		PIECES.add(new Piece('0', true, new boolean[][] { { true,  true  } }));
		PIECES.add(new Piece('1', true, new boolean[][] { { true,  false, true  } }));
		PIECES.add(new Piece('2', true, new boolean[][] { { true,  false, false,  true  } }));
		PIECES.add(new Piece('3', true, new boolean[][] { { true,  false, false,  false, true  } }));
		PIECES.add(new Piece('4', true, new boolean[][] { { true,  false, false,  false, false, true  } }));

		PIECES.add(new Piece('I', false, new boolean[][] { { true,  true,  true,  true,  true  } }));
		PIECES.add(new Piece('Y', false, new boolean[][] { { false, false, true,  false }, { true,  true,  true,  true  } }));
		PIECES.add(new Piece('L', false, new boolean[][] { { false, false, false, true  }, { true,  true,  true,  true  } }));
		PIECES.add(new Piece('N', false, new boolean[][] { { false, false, true,  true  }, { true,  true,  true,  false } }));
		PIECES.add(new Piece('U', false, new boolean[][] { { true,  false, true  }, { true,  true,  true  } }));
		PIECES.add(new Piece('Q', false, new boolean[][] { { true,  true,  true  }, { true,  true,  false } }));
		PIECES.add(new Piece('W', false, new boolean[][] { { false, false, true  }, { false, true,  true  }, { true,  true,  false } }));
		PIECES.add(new Piece('F', false, new boolean[][] { { false, true,  false }, { true,  true,  true  }, { true,  false, false } }));
		PIECES.add(new Piece('T', false, new boolean[][] { { true,  true,  true  }, { false, true,  false }, { false, true,  false } }));
		PIECES.add(new Piece('S', false, new boolean[][] { { false, true,  true  }, { false, true,  false }, { true,  true,  false } }));
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
		for (Piece piece : PIECES) {
			System.out.println(piece + "\n");
			System.out.println(piece.getShapeText());
			if (!piece.isPointSymmetric())
				System.out.println(piece.getShapeRotatedText());
		}
	}
}
