package de.scrum_master.games.logiq_tower;

import de.scrum_master.dancing_links.Column;
import de.scrum_master.dancing_links.ColumnAlreadyExistsException;
import de.scrum_master.dancing_links.Matrix;

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
		System.out.println(matrix.toMultiLineText());
	}

	private void populateColumnHeaders() throws ColumnAlreadyExistsException {
		for (Piece piece : Piece.CENTRAL_PIECES)
			matrix.addColumns("" + piece.getSymbol());
		for (Piece piece : Piece.OUTER_PIECES) {
			if (piece.getRows() > playingField.getRows())
				continue;
			matrix.addColumns("" + piece.getSymbol());
		}
		for (int row = 1; row <= playingField.getRows(); row++)
			matrix.addColumns("*" + row);
		for (int row = 1; row <= playingField.getRows(); row++) {
			for (char column = 'A'; column <= 'L'; column++)
				matrix.addColumns("" + column + row);
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
					System.out.println(nodeNames);
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
					System.out.println(nodeNames);
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
					System.out.println(nodeNames);
					matrix.addRowOfNodes(nodeNames);
				}
			}
		}
	}

	public void solve() {
		int movesPlayed = playingField.getMovesPlayed();
		int columns = playingField.getColumns();
		if (movesPlayed == 0) {
			// Play first central piece as anker at (0,0)
			for (Piece piece : new HashSet<Piece>(Piece.CENTRAL_PIECES_AVAILABLE)) {
				Move move = Move.createMove(piece, 0, 0, false);
				playingField.push(move);
				solve();
				playingField.pop();
			}
		} else if (movesPlayed < playingField.getRows()) {
			// Play more central pieces
			for (Piece piece : new HashSet<Piece>(Piece.CENTRAL_PIECES_AVAILABLE)) {
				for (int column = 0; column < columns; column++) {
					Move move = Move.createMove(piece, movesPlayed, column, false);
					if (playingField.canBePlayed(move)) {
						playingField.push(move);
						solve();
						playingField.pop();
					}
				}
			}
		} else if (playingField.getFreeCubesCount() == 0) {
			// Solution found => print it
			String playingFieldText = playingField.getText();
			if (!solutions.add(playingFieldText))
				return;
			solutionCount++;
			java.awt.Toolkit.getDefaultToolkit().beep();
			System.out.printf("Solution #%d, found after %.3f sec%n", solutionCount, (System.nanoTime() - startTimeNano) / 1e9);
			System.out.println(playingFieldText);
		} else {
			// Play outer pieces
			for (Piece piece : new HashSet<Piece>(Piece.OUTER_PIECES_AVAILABLE)) {
				int maxRow = playingField.getRows() - piece.getRows();
				for (int row = 0; row <= maxRow; row++) {
					for (int column = 0; column < columns; column++) {
						Move move = Move.createMove(piece, row, column, false);
						if (playingField.canBePlayed(move)) {
							playingField.push(move);
							solve();
							playingField.pop();
						}
						if (!piece.isPointSymmetric())
							continue;
						move = Move.createMove(piece, row, column, true);
						if (playingField.canBePlayed(move)) {
							playingField.push(move);
							solve();
							playingField.pop();
						}
					}
				}
			}
		}
	}

	public static void main(String[] args) throws IllegalFieldSizeException, ColumnAlreadyExistsException {
		LogiqTowerDLX logiqTower = new LogiqTowerDLX(new PlayingField(5));
//		logiqTower.solve();
		System.out.printf("Program finished after %.3f sec%n", (System.nanoTime() - logiqTower.startTimeNano) / 1e9);
	}
}
