package de.scrum_master.dancing_links;

import java.util.Map;

public abstract class Header extends Node {
	int size = 0;
	String name;
	Map<String, Object> data;

	public Header(Column column, String name, Map<String, Object> data) {
		super(column, null);
		this.name = name;
		this.data = data;
	}

	public Header(String name, Map<String, Object> data) {
		this(null, name, data);
	}

	public abstract Header addNode(Node node);

	public Map<String, Object> getData() {
		return data;
	}
}
