package ast;
import lexer.Token;

public class AssignNode extends Node {
    public IdentifierNode left;
    public Token op;
    public Node right;

    public AssignNode(IdentifierNode left, Token op, Node right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }
}