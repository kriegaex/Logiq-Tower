package de.scrum_master.dancing_links;

import java.util.Map;

public class Row extends Header {
	public Row(Column rootObject, String name, Map<String, Object> data) {
		super(rootObject, name, data);
		this.row = this;
	}

	@Override
	public Row addNode(Node node) {
		node.right = this;
		node.left = left;
		left.right = node;
		left = node;
		size++;
		return this;
	}

	@Override
	public String toString() {
		return "Row{" + "name='" + name + '\'' + ", size=" + size + '}';
	}

	@Override
	public String toShortString() {
		return "R{" + name + ':' + size + '}';
	}
}
