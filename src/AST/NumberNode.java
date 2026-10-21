package AST;
import Lexer.Token;

public class NumberNode extends Node {
    public Token token;
    public String value;

    public NumberNode(Token token) {
        this.token = token;
        this.value = token.getLexema();
    }
}