package de.scrum_master.dancing_links;

import java.util.*;

public class Matrix {
	final String name;
	final Column rootObject = new Column("ROOT", false);
	final Map<String, Column> columns = new HashMap<>();
	int mandatoryColumns = 0;
	int optionalColumns = 0;

	final Stack<Node> solutionRows = new Stack<>();
	int mandatoryColumnsCovered = 0;
	int optionalColumnsCovered = 0;

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
		if (column.optional)
			optionalColumns++;
		else
			mandatoryColumns++;
		return this;
	}

	public Matrix addColumns(boolean optional, String... columnNames) throws ColumnAlreadyExistsException {
		for (String columnName : columnNames)
			addColumn(new Column(columnName, optional));
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

	public void solve() {
		assert mandatoryColumns >= mandatoryColumnsCovered;
		assert optionalColumns >= optionalColumnsCovered;
		if (mandatoryColumnsCovered == mandatoryColumns)
			printSolution();
		Column column = chooseColumn();
		cover(column);
		// TODO: implement DLX algorithm here
		for (Node row = column.down; row != column; row = row.down) {
			solutionRows.push(row);
			for (Node nodeInRow = row.right; nodeInRow != row; nodeInRow = nodeInRow.right) {
				cover(nodeInRow.column); ###
			}
			solutionRows.pop();
		}
		uncover(column);
	}

	private void cover(Column column) {
		mandatoryColumnsCovered++;
		column.column.left.right = column.column.right;
		column.column.right.left = column.column.left;

		Node firstNode = column;
		Node node = firstNode;
		do {
			for (Node rowInColumn = node.column.down; rowInColumn != node.column; rowInColumn = rowInColumn.down) {

			}
			node = node.right;
		} while (node != firstNode);
		mandatoryColumnsCovered++;
	}

	private Column chooseColumn() {
		Column mandatoryColumnWithFewestRemainingRows = null;
		int fewestRemainingRows = Integer.MAX_VALUE;
		for (Column column = (Column) rootObject.right; column != rootObject; column = (Column) column.right) {
			if (!column.optional && column.size < fewestRemainingRows)
				fewestRemainingRows = column.size;
				mandatoryColumnWithFewestRemainingRows = column;
		}
		return mandatoryColumnWithFewestRemainingRows;
	}

	private void printSolution() {
		// TODO: implement printing routine here
		for (Node row : solutionRows) {
			Node firstNode = row;
			Node node = firstNode;
			StringBuilder rowBuffer = new StringBuilder();
			do {
				rowBuffer.append(node.toShortString()).append(" ");
				node = node.right;
			} while (node != firstNode);
			System.out.println(rowBuffer);
		}
	}

	public static void main(String[] args) throws ColumnAlreadyExistsException {
		Matrix matrix = new Matrix("Test matrix");
		matrix
			.addColumns(false, "A", "B", "C", "D", "E")
			.addRowOfNodes("A", "D")
			.addRowOfNodes("B", "C", "D")
			.addRowOfNodes("D", "E")
			.addRowOfNodes("A", "E")
			.addRowOfNodes("B", "C")
			.addRowOfNodes("E");
		System.out.println(matrix.rowsToText());
		System.out.println(matrix.columnsToText());
	}
}
