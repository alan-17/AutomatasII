package AST;

import java.util.List;

public class BlockNode extends Node {
    public List<Node> declarations;
    public Node compoundStatement;
    public BlockNode(List<Node> declarations, Node compoundStatement) {
        this.declarations = declarations;
        this.compoundStatement = compoundStatement;
    }
}
