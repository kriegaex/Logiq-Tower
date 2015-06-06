package de.scrum_master.dancing_links;

import java.util.*;

public class Matrix {
	final String name;
	final Column rootObject = new Column("ROOT", false);
	final Map<String, Column> columns = new HashMap<>();
	int solutionsFound = 0;
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
			printSolution(++solutionsFound, solutionRows);

		Column column = chooseColumn();
		if (column == null)
			return;
		cover(column);
		for (Node row = column.down; row != column; row = row.down) {
			solutionRows.push(row);
			for (Node node = row.right; node != row; node = node.right) {
				cover(node.column);
			}
			solve();
			solutionRows.pop();
			for (Node node = row.left; node != row; node = node.left) {
				uncover(node.column);
			}
		}
		uncover(column);
	}

	public void printSolution(int solutionNumber, Iterable<Node> solutionRows) {
		System.out.println("Solution #" + solutionNumber);
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
		System.out.println();
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

	private void cover(Column column) {
		// Delete column header
		column.right.left = column.left;
		column.left.right = column.right;

		// Delete rows found for this column
		for (Node row = column.down; row != column; row = row.down) {
			for (Node node = row.right; node != row; node = node.right) {
				// Delete node from column
				node.down.up = node.up;
				node.up.down = node.down;
				node.column.size--;
			}
		}

		// Mark column as covered
		if (column.optional)
			optionalColumnsCovered++;
		else
			mandatoryColumnsCovered++;
	}

	private void uncover(Column column) {
		// Mark column as uncovered
		if (column.optional)
			optionalColumnsCovered--;
		else
			mandatoryColumnsCovered--;

		// Undelete rows found for this column
		for (Node row = column.up; row != column; row = row.up) {
			for (Node node = row.left; node != row; node = node.left) {
				// Undelete node from column
				node.column.size++;
				node.down.up = node;
				node.up.down = node;
			}
		}

		// Undelete column header
		column.right.left = column;
		column.left.right = column;
	}

	public int getSolutionsFound() {
		return solutionsFound;
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
//		System.out.println(matrix.columnsToText());
		matrix.solve();
		System.out.println("Total number of solutions found = " + matrix.getSolutionsFound());
	}
}
