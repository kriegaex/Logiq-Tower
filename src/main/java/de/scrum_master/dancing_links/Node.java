package de.scrum_master.dancing_links;

public class Node {
	String name;
	Column column;
	Node left, right, up, down;

	public Node(String name, Column column) {
		this.name = name;
		this.column = column;
		left = right = up = down = this;
		if (column != null)
			column.addNode(this);
	}

	@Override
	public String toString() {
		return "Node{" +
			"name='" + name + '\'' +
			'}';
	}

	public String toShortString() {
		return '[' + name + ']';
	}
}
