package de.scrum_master.dancing_links;

public class Column extends Node {
	int size = 0;
	String name;

	public Column(String name) {
		super(null);
		this.column = this;
		this.name = name;
	}

	public Column addNode(Node node) {
		node.down = this;
		node.up = up;
		up.down = node;
		up = node;
		size++;
		return this;
	}

	@Override
	public String toString() {
		return "Column{" +
			"name='" + name + '\'' +
			", size=" + size +
			'}';
	}

	public String toShortString() {
		return '{' + name + ':' + size + '}';
	}
}
