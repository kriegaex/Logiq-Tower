package de.scrum_master.dancing_links;

public class Node {
	Column column;
	Node left, right, up, down;

	public Node(Column column) {
		this.column = column;
		left = right = up = down = this;
		if (column != null)
			column.addNode(this);
	}

	public String toShortString() {
		return '[' + column.name + ']';
	}
}
