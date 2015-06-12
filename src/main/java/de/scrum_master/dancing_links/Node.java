package de.scrum_master.dancing_links;

public class Node {
	Column column;
	String name;
	Node left, right, up, down;

	public Node(Column column, String name) {
		this.column = column;
		this.name = name;
		left = right = up = down = this;
		if (column != null)
			column.addNode(this);
	}

	public Node getLeft() {
		return left;
	}

	public Node getRight() {
		return right;
	}

	public String getColumnName() {
		return column.name;
	}

	public String toShortString() {
		return '[' + column.name + ']';
	}
}
