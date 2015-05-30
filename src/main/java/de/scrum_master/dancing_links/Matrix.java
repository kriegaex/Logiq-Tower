package de.scrum_master.dancing_links;

import java.util.*;

public class Matrix {
	final String name;
	final Column rootObject = new Column("ROOT");
	final Map<String, Column> columns = new HashMap<>();

	public Matrix(String name) {
		this.name = name;
	}

	public Matrix addColumn(Column column) {
//		assert columns.put(column.name, column) == null;
		columns.put(column.name, column);
		column.left = rootObject.left;
		column.right = rootObject;
		rootObject.left.right = column;
		rootObject.left = column;
		return this;
	}

	public Matrix addRowOfNodes(List<Map.Entry<String, String>> nodeInfos) {
		Node firstNode = null;
		Node previousNode = null;
		for (Map.Entry<String, String> nodeInfo : nodeInfos) {
			Node node = createNode(nodeInfo.getKey(), nodeInfo.getValue());
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

	public static void main(String[] args) {
		Matrix matrix = new Matrix("Test matrix");
//		System.out.println(matrix.toMultiLineText());
		matrix
			.addColumn(new Column("A"))
			.addColumn(new Column("B"))
			.addColumn(new Column("C"))
			.addColumn(new Column("D"))
			.addColumn(new Column("E"));
//		System.out.println(matrix.toMultiLineText());
		List<Map.Entry<String, String>> nodeInfos = new ArrayList<>();
		nodeInfos.add(new AbstractMap.SimpleEntry<String, String>("A0", "A"));
		nodeInfos.add(new AbstractMap.SimpleEntry<String, String>("D0", "D"));
		matrix.addRowOfNodes(nodeInfos);
//		System.out.println(matrix.toMultiLineText());
		nodeInfos.clear();
		nodeInfos.add(new AbstractMap.SimpleEntry<String, String>("B1", "B"));
		nodeInfos.add(new AbstractMap.SimpleEntry<String, String>("C1", "C"));
		nodeInfos.add(new AbstractMap.SimpleEntry<String, String>("D1", "D"));
		matrix.addRowOfNodes(nodeInfos);
//		System.out.println(matrix.toMultiLineText());
		nodeInfos.clear();
		nodeInfos.add(new AbstractMap.SimpleEntry<String, String>("D2", "D"));
		nodeInfos.add(new AbstractMap.SimpleEntry<String, String>("E2", "E"));
		matrix.addRowOfNodes(nodeInfos);
//		System.out.println(matrix.toMultiLineText());
		nodeInfos.clear();
		nodeInfos.add(new AbstractMap.SimpleEntry<String, String>("A3", "A"));
		nodeInfos.add(new AbstractMap.SimpleEntry<String, String>("E3", "E"));
		matrix.addRowOfNodes(nodeInfos);
//		System.out.println(matrix.toMultiLineText());
		nodeInfos.clear();
		nodeInfos.add(new AbstractMap.SimpleEntry<String, String>("B4", "B"));
		nodeInfos.add(new AbstractMap.SimpleEntry<String, String>("C4", "C"));
		matrix.addRowOfNodes(nodeInfos);
//		System.out.println(matrix.toMultiLineText());
		nodeInfos.clear();
		nodeInfos.add(new AbstractMap.SimpleEntry<String, String>("E5", "E"));
		matrix.addRowOfNodes(nodeInfos);
		nodeInfos.clear();
		System.out.println(matrix.toMultiLineText());
	}
}
