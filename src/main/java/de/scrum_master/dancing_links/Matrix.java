package de.scrum_master.dancing_links;

import java.util.*;

public class Matrix {
	final String name;
	final Column rootObject = new Column("ROOT", new HashMap<>(), true);
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
			addColumn(new Column(columnName, new HashMap<>(), optional));
		return this;
	}

	public Matrix addRowOfNodes(String rowName, List<String> nodeNames) {
		Row row = new Row(rootObject, rowName, new HashMap<>());
		for (String nodeInfo : nodeNames)
			createNode(nodeInfo, row);
		return this;
	}

	public Matrix addRowOfNodes(String rowName, String... nodeNames) {
		return addRowOfNodes(rowName, Arrays.asList(nodeNames));
	}

	public Node createNode(String columnName, Row row) {
		return new Node(columns.get(columnName), row);
	}

	@Override
	public String toString() {
		return "Matrix{" + "name='" + name + '\'' + '}';
	}

	public String rowsToText() {
		if (rootObject.right == rootObject)
			return "<empty matrix>\n";
		StringBuilder buffer = new StringBuilder();
		for (Node row = rootObject.down; row != rootObject; row = row.down) {
			buffer.append(row.toShortString()).append(" -> ");
			for (Node node = row.right; node != row; node = node.right)
				buffer.append(node.toShortString()).append(" ");
			buffer.append("\n");
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
				if (node instanceof Row)
					continue;
				cover(node.column);
			}
			solve();
			solutionRows.pop();
			for (Node node = row.left; node != row; node = node.left) {
				if (node instanceof Row)
					continue;
				uncover(node.column);
			}
		}
		uncover(column);
	}

	public void printSolution(int solutionNumber, Iterable<Node> solutionRows) {
		System.out.println("Solution #" + solutionNumber);
		for (Node row : solutionRows) {
			StringBuilder rowBuffer = new StringBuilder();
			rowBuffer.append(row.row.toShortString()).append(" -> ");
			for (Node node = row.row.right; node != row.row; node = node.right)
				rowBuffer.append(node.toShortString()).append(" ");
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
			.addRowOfNodes("1", "A", "D")
			.addRowOfNodes("2", "B", "C", "D")
			.addRowOfNodes("3", "D", "E")
			.addRowOfNodes("4", "A", "E")
			.addRowOfNodes("5", "B", "C")
			.addRowOfNodes("6", "E");
		System.out.println(matrix.rowsToText());
//		System.out.println(matrix.columnsToText());
		matrix.solve();
		System.out.println("Total number of solutions found = " + matrix.getSolutionsFound());
	}
}
