package de.scrum_master.dancing_links;

public class Node {
    Node left;
    Node right;
    Node up;
    Node down;
    Column column;

    public Node(Column column) {
        left = this;
        right = this;
        up = this;
        down = this;
        this.column = column;
        column.appendNode(this);
    }
}
