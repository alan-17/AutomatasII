package AST;

import java.util.List;

public class CompoundNode extends Node {
    public List<Node> statements;
    public CompoundNode(List<Node> statements) {
        this.statements = statements;
    }
}