package de.scrum_master.games.logiq_tower;

import de.scrum_master.dancing_links.ColumnAlreadyExistsException;
import de.scrum_master.dancing_links.Node;

import java.util.*;

public class LogiqTowerDLX {
	private final PlayingField playingField;
	private final Matrix matrix;
	private long startTimeNano = System.nanoTime();

	public LogiqTowerDLX(PlayingField playingField) throws ColumnAlreadyExistsException {
		this.playingField = playingField;
		this.matrix = new Matrix("Logiq Tower (" + playingField.getRows() + " rows)");
		populateMatrix();
	}

	private void populateMatrix() throws ColumnAlreadyExistsException {
		populateColumnHeaders();
		populateCentralPieces();
		populateOuterPieces();
//		System.out.println(matrix.rowsToText());
	}

	private void populateColumnHeaders() throws ColumnAlreadyExistsException {
		for (int row = 1; row <= playingField.getRows(); row++)
			matrix.addColumns(false, "*" + row);
		for (int row = 1; row <= playingField.getRows(); row++) {
			for (char column = 'A'; column <= 'L'; column++)
				matrix.addColumns(false, "" + column + row);
		}
		for (Piece piece : Piece.CENTRAL_PIECES)
			matrix.addColumns(true, "" + piece.getSymbol());
		for (Piece piece : Piece.OUTER_PIECES) {
			if (piece.getRows() <= playingField.getRows())
				matrix.addColumns(true, "" + piece.getSymbol());
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
		System.out.println("Number of unique solutions    = " + matrix.uniqueSolutions.size());
		System.out.println("Number of duplicate solutions = " + matrix.nonUniqueSolutionsFound);
		System.out.println("-------------------------------------");
		System.out.println("Total number of solutions     = " + matrix.getSolutionsFound());
		System.out.println();
	}

	class Matrix extends de.scrum_master.dancing_links.Matrix {
		Set<String> uniqueSolutions = new HashSet<>();
		int nonUniqueSolutionsFound = 0;

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

			String solutionText = createModifiedSolution(solutionChars, 0, false);
			System.out.println(solutionText);
			for (boolean rotateBy180 : new boolean[] { true, false }) {
				for (int i = 0; i < fieldColumns; i++) {
					String duplicateSolution = createModifiedSolution(solutionChars, i, rotateBy180);
					if (uniqueSolutions.contains(duplicateSolution)) {
						System.out.println(
							"Solution is a duplicate (" +
							(rotateBy180 ? "" : "un") + "rotated, scrolled by " + i + ") of"
						);
						System.out.println(duplicateSolution);
						nonUniqueSolutionsFound++;
						return;
					}
				}
			}
			uniqueSolutions.add(solutionText);
		}

		private String createModifiedSolution(char[][] originalSolution, int scrollColumns, boolean rotateBy180) {
			int rows = playingField.getRows();
			int columns = playingField.getColumns();
			char[][] modifiedSolution = new char[originalSolution.length][originalSolution[0].length];
			StringBuilder buffer = new StringBuilder();
			for (int row = 0; row < rows; row++) {
				int modifiedRow = rotateBy180 ? rows - 1 - row : row;
				for (int column = 0; column < columns; column++) {
					int modifiedColumn = rotateBy180
						? (columns - 1 - column + scrollColumns) % columns
						: (column + scrollColumns) % columns;
					modifiedSolution[row][column] = originalSolution[modifiedRow][modifiedColumn];
				}
				buffer.append(modifiedSolution[row]).append('\n');
			}
			return buffer.toString();
		}
	}

	public static void main(String[] args) throws IllegalFieldSizeException, ColumnAlreadyExistsException {
		LogiqTowerDLX logiqTower = new LogiqTowerDLX(new PlayingField(5));
		logiqTower.solve();
		System.out.printf("Program finished after %.3f sec%n", (System.nanoTime() - logiqTower.startTimeNano) / 1e9);
	}
}
