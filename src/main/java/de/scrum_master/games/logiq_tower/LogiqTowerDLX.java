package de.scrum_master.games.logiq_tower;

import de.scrum_master.dancing_links.Column;
import de.scrum_master.dancing_links.ColumnAlreadyExistsException;
import de.scrum_master.dancing_links.Matrix;

import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;

public class LogiqTowerDLX {
	private final PlayingField playingField;
	private final Matrix matrix;
	private int solutionCount = 0;
	private final SortedSet<String> solutions = new TreeSet<>();
	private long startTimeNano = System.nanoTime();

	public LogiqTowerDLX(PlayingField playingField) throws ColumnAlreadyExistsException {
		this.playingField = playingField;
		this.matrix = new Matrix("Logiq Tower (" + playingField.getRows() + " rows)");
		populateMatrix();
	}

	private void populateMatrix() throws ColumnAlreadyExistsException {
		matrix.addColumns("0", "1", "2", "3", "4", "I", "Y", "L", "N", "U", "Q", "W", "F", "T", "S");
		for (int row = 1; row <= playingField.getRows(); row++)
			matrix.addColumns("*" + row);
		for (int row = 1; row <= playingField.getRows(); row++) {
			for (char column = 'A'; column <= 'L'; column++)
				matrix.addColumns("" + column + row);
		}
		System.out.println(matrix.toMultiLineText());
//		matrix.addRowOfNodes("A0|A", "D0|D");
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