package AST;

import Lexer.Token;
import java.util.List;

public class VarDeclNode extends Node {
    public List<String> variables;
    public Token type;
    public VarDeclNode(List<String> variables, Token type) {
        this.variables = variables;
        this.type = type;
    }
}