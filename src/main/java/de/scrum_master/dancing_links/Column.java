package de.scrum_master.dancing_links;

public class Column extends Node {
	int size = 0;
	boolean optional;

	public Column(String name, boolean optional) {
		super(null, name);
		this.column = this;
		this.optional = optional;
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
				", optional=" + optional +
				'}';
	}

	public String toShortString() {
		return '{' + name + ':' + size + ':' + (optional ? 'T' : 'F') + '}';
	}
}
