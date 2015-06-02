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

	public Matrix addRowOfNodes(List<String> nodeNames) {
		Node firstNode = null;
		Node previousNode = null;
		for (String nodeInfo : nodeNames) {
			Node node = createNode(nodeInfo);
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

	public Matrix addRowOfNodes(String... nodeNames) {
		return addRowOfNodes(Arrays.asList(nodeNames));
	}

	public Node createNode(String columnName) {
		return new Node(columns.get(columnName));
	}

	@Override
	public String toString() {
		return "Matrix{" +
			"name='" + name + '\'' +
			'}';
	}

	public String rowsToText() {
		// TODO: print line-wise, not column-wise
		if (rootObject.right == rootObject)
			return "<empty matrix>\n";
		StringBuilder buffer = new StringBuilder();
		for (Node column = rootObject.right; column != rootObject; column = column.right)
			buffer.append(column.toShortString()).append(" ");
		buffer.append("\n");

		Set<Column> columnsPrinted = new HashSet<>();
		for (Node column = rootObject.right; column != rootObject; column = column.right) {
			rowLoop:
			for (Node row = column.down; row != column; row = row.down) {
				Node firstNode = row;
				Node node = firstNode;
				StringBuilder rowBuffer = new StringBuilder();
				do {
					if (columnsPrinted.contains(node.column))
						continue rowLoop;
					rowBuffer.append(node.toShortString()).append(" ");
					node = node.right;
				} while (node != firstNode);
				buffer.append(rowBuffer).append("\n");
			}
			columnsPrinted.add(((Column) column));
		}
		return buffer.toString();
	}

	public String columnsToText() {
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
			.addRowOfNodes("A", "D")
			.addRowOfNodes("B", "C", "D")
			.addRowOfNodes("D", "E")
			.addRowOfNodes("A", "E")
			.addRowOfNodes("B", "C")
			.addRowOfNodes("E");
		System.out.println(matrix.columnsToText());
	}
}
