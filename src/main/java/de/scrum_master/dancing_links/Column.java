package de.scrum_master.dancing_links;

import java.util.Map;

public class Column extends Header {
	boolean optional;

	public Column(String name, Map<String, Object> data, boolean optional) {
		super(name, data);
		this.column = this;
		this.optional = optional;
	}

	@Override
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
		return "Column{" + "name='" + name + '\'' + ", size=" + size + ", optional=" + optional + '}';
	}

	@Override
	public String toShortString() {
		return "C{" + name + ':' + size + ':' + (optional ? 'T' : 'F') + '}';
	}
}
