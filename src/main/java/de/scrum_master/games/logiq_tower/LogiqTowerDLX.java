package de.scrum_master.games.logiq_tower;

import de.scrum_master.dancing_links.ColumnAlreadyExistsException;
import de.scrum_master.dancing_links.Node;

import java.util.*;

public class LogiqTowerDLX {
	private final PlayingField playingField;
	private final Matrix matrix;
	private int solutionCount = 0;
	private final SortedSet<String> solutions = new TreeSet<>();
	private long startTimeNano = System.nanoTime();

	private final int PIECES_INDEX_BASE;
	private final int CENTRAL_INDEX_BASE;
	private final int COORDS_INDEX_BASE;
	private final int PIECES_COUNT;
	private final int CENTRAL_COUNT;
	private final int COORDS_COUNT;

	public LogiqTowerDLX(PlayingField playingField) throws ColumnAlreadyExistsException {
		this.playingField = playingField;
		this.matrix = new Matrix("Logiq Tower (" + playingField.getRows() + " rows)");
		PIECES_INDEX_BASE = 0;
		PIECES_COUNT = Piece.CENTRAL_PIECES.size() + Piece.OUTER_PIECES.size();
		CENTRAL_INDEX_BASE = PIECES_INDEX_BASE + PIECES_COUNT;
		CENTRAL_COUNT = playingField.getRows();
		COORDS_INDEX_BASE = CENTRAL_INDEX_BASE + CENTRAL_COUNT;
		COORDS_COUNT = playingField.getColumns() * playingField.getRows();
		populateMatrix();
	}

	private void populateMatrix() throws ColumnAlreadyExistsException {
		populateColumnHeaders();
		populateCentralPieces();
		populateOuterPieces();
//		System.out.println(matrix.rowsToText());
	}

	private void populateColumnHeaders() throws ColumnAlreadyExistsException {
		for (Piece piece : Piece.CENTRAL_PIECES)
			matrix.addColumns(true, "" + piece.getSymbol());
		for (Piece piece : Piece.OUTER_PIECES) {
			if (piece.getRows() > playingField.getRows())
				continue;
			matrix.addColumns(true, "" + piece.getSymbol());
		}
		for (int row = 1; row <= playingField.getRows(); row++)
			matrix.addColumns(false, "*" + row);
		for (int row = 1; row <= playingField.getRows(); row++) {
			for (char column = 'A'; column <= 'L'; column++)
				matrix.addColumns(false, "" + column + row);
		}
	}

	private void populateCentralPieces() {
		for (Piece piece : Piece.CENTRAL_PIECES) {
			for (int row = 0; row < playingField.getRows(); row++) {
				for (int column = 0; column < playingField.getColumns(); column++) {
					if (row == 0 && column > 0) {
						// Central pieces in row 0 are only placed in column 0
						// in order to avoid lots of rotated solutions
						continue;
					}
					List<String> nodeNames = new ArrayList<>();
					nodeNames.add("" + piece.getSymbol());
					nodeNames.add("*" + (row + 1));
					boolean[][] shape = piece.getShape();
					for (int shapeRow = 0; shapeRow < piece.getRows(); shapeRow++) {
						for (int shapeColumn = 0; shapeColumn < piece.getColumns(); shapeColumn++) {
							if (shape[shapeRow][shapeColumn])
								nodeNames.add("" + (char)('A' + (column + shapeColumn) % playingField.getColumns()) + (row + shapeRow + 1));
						}
					}
//					System.out.println(nodeNames);
					matrix.addRowOfNodes(nodeNames);
				}
			}
		}
	}

	private void populateOuterPieces() {
		for (Piece piece : Piece.OUTER_PIECES) {
			if (piece.getRows() > playingField.getRows())
				continue;
			for (int row = 0; row <= playingField.getRows() - piece.getRows(); row++) {
				for (int column = 0; column < playingField.getColumns(); column++) {
					if (row == 0 && column == 0) {
						// No outer piece may be placed at row 0, column 0 because
						// per definition there is always a central piece there
						continue;
					}
					List<String> nodeNames = new ArrayList<>();
					nodeNames.add("" + piece.getSymbol());
					boolean[][] shape = piece.getShape();
					for (int shapeRow = 0; shapeRow < piece.getRows(); shapeRow++) {
						for (int shapeColumn = 0; shapeColumn < piece.getColumns(); shapeColumn++) {
							if (shape[shapeRow][shapeColumn])
								nodeNames.add("" + (char)('A' + (column + shapeColumn) % playingField.getColumns()) + (row + shapeRow + 1));
						}
					}
//					System.out.println(nodeNames);
					matrix.addRowOfNodes(nodeNames);

					if (piece.isPointSymmetric())
						continue;

					nodeNames.clear();
					nodeNames.add("" + piece.getSymbol());
					shape = piece.getShapeRotated();
					for (int shapeRow = 0; shapeRow < piece.getRows(); shapeRow++) {
						for (int shapeColumn = 0; shapeColumn < piece.getColumns(); shapeColumn++) {
							if (shape[shapeRow][shapeColumn])
								nodeNames.add("" + (char)('A' + (column + shapeColumn) % playingField.getColumns()) + (row + shapeRow + 1));
						}
					}
//					System.out.println(nodeNames);
					matrix.addRowOfNodes(nodeNames);
				}
			}
		}
	}

	public void solve() {
		matrix.solve();
		System.out.println("Total number of solutions found = " + matrix.getSolutionsFound());
	}

	class Matrix extends de.scrum_master.dancing_links.Matrix {
		public Matrix(String name) {
			super(name);
		}

		@Override
		public void printSolution(int solutionNumber, Iterable<Node> solutionRows) {
			System.out.println("Solution #" + solutionNumber);

			int fieldRows = playingField.getRows();
			int fieldColumns = playingField.getColumns();
			char[][] solutionChars = new char[fieldRows][fieldColumns];
			for (Node row : solutionRows) {
				Node firstNode = row;
				Node node = firstNode;
				StringBuilder rowBuffer = new StringBuilder();
				do {
					rowBuffer.append(node.getColumnName()).append(",");
					node = node.getRight();
				} while (node != firstNode);
				List<String> columnNames = Arrays.asList(rowBuffer.toString().split(","));
				char pieceName = columnNames
					.stream()
					.filter(columnName -> columnName.length() == 1)
					.findFirst()
					.get()
					.charAt(0);
				columnNames
					.stream()
					.filter(columnName -> columnName.matches("[A-Z][0-9]"))
					.forEach(columnName -> {
						int pieceRow = columnName.charAt(1) - '1';
						int pieceColumn = columnName.charAt(0) - 'A';
						solutionChars[pieceRow][pieceColumn] = pieceName;
					});
			}

			for (int row = 0; row < fieldRows; row++)
				System.out.println(new String(solutionChars[row]));
			System.out.println();
		}
	}

	public static void main(String[] args) throws IllegalFieldSizeException, ColumnAlreadyExistsException {
		LogiqTowerDLX logiqTower = new LogiqTowerDLX(new PlayingField(5));
		logiqTower.solve();
		System.out.printf("Program finished after %.3f sec%n", (System.nanoTime() - logiqTower.startTimeNano) / 1e9);
	}
}
