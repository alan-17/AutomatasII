package AST;
import Lexer.Token;

public class IdentifierNode extends Node {
    public Token token;
    public String name;

    public IdentifierNode(Token token) {
        this.token = token;
        this.name = token.getLexema();
    }
}