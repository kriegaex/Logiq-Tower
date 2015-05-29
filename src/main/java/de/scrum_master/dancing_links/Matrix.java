package de.scrum_master.dancing_links;

import java.util.Deque;
import java.util.LinkedList;

public class Matrix {
    final Column rootObject = new Column("ROOT");
    final Deque<Column> columns = new LinkedList<>();

    public Matrix addColumn(Column column) {
        // TODO: This is not really how to construct a sparse matrix.
        // Probably it makes more sense to add sparse rows consisting of
        // nodes pointing to the corresponding columns.

        Node rightNeigbour = columns.peekFirst();
        Node leftNeighbour = columns.peekLast();
        columns.add(column);
        if (rightNeigbour == null)
            return this;
        Node node = column;
        do {
            node.left = leftNeighbour;
            leftNeighbour.right = node;
            node.right = rightNeigbour;
            rightNeigbour.left = node;

            node = node.down;
            leftNeighbour = leftNeighbour.down;
            rightNeigbour = rightNeigbour.down;
        } while (node != column);
        return this;
    }
}
