package de.scrum_master.dancing_links;

public class Node {
	Column column;
	Row row;
	Node left, right, up, down;

	public Node(Column column, Row row) {
		this.column = column;
		this.row = row;
		left = right = up = down = this;
		if (column != null)
			column.addNode(this);
		if (row != null)
			row.addNode(this);
	}

	public Node getNextInRow() {
		return right;
	}

	public Node getNextInColumn() {
		return down;
	}

	public Row getRow() {
		return row;
	}

	public Column getColumn() {
		return column;
	}

	public String getColumnName() {
		return column.name;
	}

	public String getRowName() {
		return row.name;
	}

	public String toShortString() {
		// TODO: change so as to reflect row name, too?
		return '[' + column.name + ']';
	}
}
