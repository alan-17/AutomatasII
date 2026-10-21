package ast;
import lexer.Token;

public class OperatorNode extends Node {
    public Node left;
    public Token op;
    public Node right;

    public OperatorNode(Node left, Token op, Node right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }
}