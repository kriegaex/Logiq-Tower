package de.scrum_master.games.logiq_tower;

import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;

public class LogiqTower {
	private final PlayingField playingField;
	private int solutionCount = 0;
	private final SortedSet<String> solutions = new TreeSet<>();
	private long startTimeNano = System.nanoTime();

	public LogiqTower(PlayingField playingField) {
		this.playingField = playingField;
	}

	public void solve() {
		int movesPlayed = playingField.getMovesPlayed();
		if (movesPlayed == 0) {
			// Play first central piece as anker at (0,0)
			for (Piece piece : new HashSet<Piece>(Piece.CENTRAL_PIECES_AVAILABLE)) {
				Move move = Move.createMove(piece, 0, 0, false);
				playingField.push(move);
				solve();
				playingField.pop();
			}
		} else {
			int columns = playingField.getColumns();
			if (movesPlayed < playingField.getRows()) {
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
	}

	public static void main(String[] args) throws IllegalFieldSizeException {
		LogiqTower logiqTower = new LogiqTower(new PlayingField(3));
		logiqTower.solve();
		System.out.printf("Program finished after %.3f sec%n", (System.nanoTime() - logiqTower.startTimeNano) / 1e9);
	}
}
