package de.scrum_master.dancing_links;

public class Column extends Node {
    int size = 0;
    String name;

    public Column(String name) {
        super(null);
        this.column = this;
        this.name = name;
    }

    public Column appendNode(Node node) {
        up.down = node;
        node.up = up;
        up = node;
        node.down = this;
        size++;
        return this;
    }
}
