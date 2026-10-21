package ast;

public class IfNode extends Node {
    public Node condition;
    public Node thenStmt;
    public Node elseStmt;
    public IfNode(Node condition, Node thenStmt, Node elseStmt) {
        this.condition = condition;
        this.thenStmt = thenStmt;
        this.elseStmt = elseStmt;
    }
}