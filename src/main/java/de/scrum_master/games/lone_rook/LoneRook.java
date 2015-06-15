package de.scrum_master.games.lone_rook;

import de.scrum_master.dancing_links.ColumnAlreadyExistsException;
import de.scrum_master.dancing_links.Node;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class LoneRook {
	static final int NUM_ROWS = 8;
	static final int NUM_COLS = 8;
	static final int START_ROW = 4;
	static final int START_COL = 3;

	private final Matrix matrix;
	private long startTimeNano = System.nanoTime();

	public LoneRook() throws ColumnAlreadyExistsException {
		this.matrix = new Matrix("Lone Rook");
		populateMatrix();
	}

	private void populateMatrix() throws ColumnAlreadyExistsException {
		populateColumnHeaders();
		populateRows();
		System.out.println(matrix.rowsToText());
	}

	private void populateColumnHeaders() throws ColumnAlreadyExistsException {
		for (int row = 0; row < NUM_ROWS; row++) {
			for (int column = 0; column < NUM_COLS; column++) {
				matrix.addColumns(false, "" + coordinatesToChessNotation(column, row));
			}
		}
	}

	private void populateRows() {
		for (int startRow = 0; startRow < NUM_ROWS; startRow++) {
			for (int startColumn = 0; startColumn < NUM_COLS; startColumn++) {
				for (int endRow = 0; endRow < NUM_ROWS; endRow++) {
					// Skip horizontal moves from start position in order to eliminate some symmetries
					// (e.g. d4-d8 is equivalent to d4-h4 and d4-d1 to d4-a4)
					if (startRow == START_ROW && startColumn == START_COL && endRow == START_ROW) {
						System.out.printf("(%d,%d) / %d%n", startRow, startColumn, endRow);
						continue;
					}
					for (int endColumn = 0; endColumn < NUM_COLS; endColumn++) {
						// Only straight (horizontal or vertical) moves for rooks in chess
						if (startRow != endRow && startColumn != endColumn)
							continue;
						// Start field must be != end field
						if (startRow == endRow && startColumn == endColumn)
							continue;
						matrix.addRowOfNodes(
							coordinatesToChessNotation(startColumn, startRow) + "-" + coordinatesToChessNotation(endColumn, endRow),
							createColumnNameList(startColumn, startRow, endColumn, endRow)
						);
					}
				}
			}
		}
	}

	private List<String> createColumnNameList(int startColumn, int startRow, int endColumn, int endRow) {
		// Must be vertical or horizontal move
		assert
			startColumn == endColumn && startRow != endRow ||
			startColumn != endColumn && startRow == endRow;

		List<String> columnNames = new LinkedList<>();
		int increment;
		if (startColumn == endColumn) {
			// Vertical move
			increment = startRow < endRow ? 1 : -1;
			// Skip end field so as not to block it twice
			for (int row = startRow; row != endRow; row += increment)
				columnNames.add(coordinatesToChessNotation(startColumn, row));
		} else {
			// Horizontal move
			increment = startColumn < endColumn ? 1 : -1;
			// Skip end field so as not to block it twice
			for (int column = startColumn; column != endColumn; column += increment)
				columnNames.add(coordinatesToChessNotation(column, startRow));
		}
		return columnNames;
	}

	private String coordinatesToChessNotation(int column, int row) {
		return "" + (char) ('a' + column) + (char) ('1' + row);
	}

	private int[] chessNotationToCoordinates(String fieldName) {
		return new int[] { fieldName.charAt(0) - 'a', fieldName.charAt(1) - '1' };
	}

	public void solve() {

	}

	class Matrix extends de.scrum_master.dancing_links.Matrix {
		Set<String> uniqueSolutions = new HashSet<>();
		int nonUniqueSolutionsFound = 0;

		public Matrix(String name) {
			super(name, true);
		}

		@Override
		public boolean isValidSolution() {
			// TODO: check if solution is valid in addition to just covering all fields
			return super.isValidSolution();
		}

		@Override
		public void printSolution(int solutionNumber, Iterable<Node> solutionRows) {
			System.out.println("Solution #" + solutionNumber);

//			int fieldRows = playingField.getRows();
//			int fieldColumns = playingField.getColumns();
//			char[][] solutionChars = new char[fieldRows][fieldColumns];
//			for (Node row : solutionRows) {
//				Node firstNode = row;
//				Node node = firstNode;
//				StringBuilder rowBuffer = new StringBuilder();
//				do {
//					rowBuffer.append(node.getColumnName()).append(",");
//					node = node.getRight();
//				} while (node != firstNode);
//				List<String> columnNames = Arrays.asList(rowBuffer.toString().split(","));
//				char pieceName = columnNames
//						.stream()
//						.filter(columnName -> columnName.length() == 1)
//						.findFirst()
//						.get()
//						.charAt(0);
//				columnNames
//						.stream()
//						.filter(columnName -> columnName.matches("[A-Z][0-9]"))
//						.forEach(columnName -> {
//							int pieceRow = columnName.charAt(1) - '1';
//							int pieceColumn = columnName.charAt(0) - 'A';
//							solutionChars[pieceRow][pieceColumn] = pieceName;
//						});
//			}
//
//			String solutionText = createModifiedSolution(solutionChars, 0, false);
//			System.out.println(solutionText);
//			for (boolean rotateBy180 : new boolean[] { true, false }) {
//				for (int i = 0; i < fieldColumns; i++) {
//					String duplicateSolution = createModifiedSolution(solutionChars, i, rotateBy180);
//					if (uniqueSolutions.contains(duplicateSolution)) {
//						System.out.println(
//								"Solution is a duplicate (" +
//										(rotateBy180 ? "" : "un") + "rotated, scrolled by " + i + ") of"
//						);
//						System.out.println(duplicateSolution);
//						nonUniqueSolutionsFound++;
//						return;
//					}
//				}
//			}
//			uniqueSolutions.add(solutionText);
		}

//		private String createModifiedSolution(char[][] originalSolution, int scrollColumns, boolean rotateBy180) {
//			int rows = playingField.getRows();
//			int columns = playingField.getColumns();
//			char[][] modifiedSolution = new char[originalSolution.length][originalSolution[0].length];
//			StringBuilder buffer = new StringBuilder();
//			for (int row = 0; row < rows; row++) {
//				int modifiedRow = rotateBy180 ? rows - 1 - row : row;
//				for (int column = 0; column < columns; column++) {
//					int modifiedColumn = rotateBy180
//							? (columns - 1 - column + scrollColumns) % columns
//							: (column + scrollColumns) % columns;
//					modifiedSolution[row][column] = originalSolution[modifiedRow][modifiedColumn];
//				}
//				buffer.append(modifiedSolution[row]).append('\n');
//			}
//			return buffer.toString();
//		}
	}

	public static void main(String[] args) throws ColumnAlreadyExistsException {
		LoneRook loneRook = new LoneRook();
		loneRook.solve();
		System.out.printf("Program finished after %.3f sec%n", (System.nanoTime() - loneRook.startTimeNano) / 1e9);
	}
}
