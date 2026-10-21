package lexer;
public class Token{
    private String lexema;
    private TokenType atributo;
    private String value;
    
    public Token(String lexema, TokenType atributo){
        this.lexema = lexema;
        this.atributo = atributo;
        this.value = lexema;
    }
    @Override
    public String toString(){
        return atributo + ", " + lexema;
    }
    public String getLexema(){
        return this.lexema;
    }
    public void setLexema(String lexema){
        this.lexema = lexema;
    }
    public TokenType getAtributo(){
        return this.atributo;
    }
    public void setAtributo(TokenType Atributo){
       this.atributo = atributo;
    }
    public String getValue(){
        return this.value;
    }
    public void setValue(String value){
        this.value = value;
    }
}