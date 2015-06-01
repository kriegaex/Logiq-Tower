package de.scrum_master.dancing_links;

import java.util.*;

public class Matrix {
	final String name;
	final Column rootObject = new Column("ROOT");
	final Map<String, Column> columns = new HashMap<>();

	public Matrix(String name) {
		this.name = name;
	}

	public Matrix addColumn(Column column) throws ColumnAlreadyExistsException {
		if (columns.put(column.name, column) != null)
			throw new ColumnAlreadyExistsException("column '" + column.name + "' already exists");
		column.left = rootObject.left;
		column.right = rootObject;
		rootObject.left.right = column;
		rootObject.left = column;
		return this;
	}

	public Matrix addColumns(String... columnNames) throws ColumnAlreadyExistsException {
		for (String columnName : columnNames)
			addColumn(new Column(columnName));
		return this;
	}

	public Matrix addRowOfNodes(String... nodeInfos) {
		Node firstNode = null;
		Node previousNode = null;
		for (String nodeInfo : nodeInfos) {
			int separatorIndex = nodeInfo.indexOf("|");
			Node node = createNode(nodeInfo.substring(0, separatorIndex), nodeInfo.substring(separatorIndex + 1));
			if (firstNode == null)
				firstNode = previousNode = node;
			node.right = firstNode;
			node.left = previousNode;
			firstNode.left = node;
			previousNode.right = node;
			previousNode = node;
		}
		return this;
	}

	public Node createNode(String nodeName, String columnName) {
		return new Node(nodeName, columns.get(columnName));
	}

	@Override
	public String toString() {
		return "Matrix{" +
			"name='" + name + '\'' +
			'}';
	}

	public String toMultiLineText() {
		if (rootObject.right == rootObject)
			return "<empty matrix>\n";
		StringBuilder buffer = new StringBuilder();
		for (Node column = rootObject.right; column != rootObject; column = column.right) {
			buffer.append(column.toShortString()).append(" -> ");
			for (Node node = column.down; node != column; node = node.down)
				buffer.append(node.toShortString()).append(" ");
			buffer.append("\n");
		}
		return buffer.toString();
	}

	public static void main(String[] args) throws ColumnAlreadyExistsException {
		Matrix matrix = new Matrix("Test matrix");
		matrix
			.addColumns("A", "B", "C", "D", "E")
			.addRowOfNodes("A0|A", "D0|D")
			.addRowOfNodes("B1|B", "C1|C", "D1|D")
			.addRowOfNodes("D2|D", "E2|E")
			.addRowOfNodes("A3|A", "E3|E")
			.addRowOfNodes("B4|B", "C4|C")
			.addRowOfNodes("E5|E");
		System.out.println(matrix.toMultiLineText());
	}
}
