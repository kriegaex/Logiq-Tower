package de.scrum_master.games.logiq_tower;

public class PlayingField {
	public static final int MAX_ROWS = 5;
	public static final int MAX_COLUMNS = 12;

	private static final int MIN_ROWS = 2;

	private final int rows;
	private final int columns;

	public PlayingField(int rows) throws IllegalFieldSizeException {
		if (rows < MIN_ROWS || rows > MAX_ROWS) {
			throw new IllegalFieldSizeException(
				"illegal number of rows " + rows +
				", must be [" + MIN_ROWS + ".." + MAX_ROWS + "]"
			);
		}
		this.rows = rows;
		this.columns = MAX_COLUMNS;
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}
}
