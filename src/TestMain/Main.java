package TestMain;

import AST.Node;
import Lexer.Lexer;
import Parser.Parser;
import TAC.TAC;

public class Main {
    public static void main(String[] args) {
        String input = "" + 
            "PROGRAM prueba;"+
            "VAR"+
            "    x, y : INTEGER;"+
            "    resultado : INTEGER;"+
            "BEGIN"+
            "    x := 10;"+
            "    y := 5;"+
            "   "+
            "    WHILE x > 0 DO"+
            "    BEGIN"+
            "        IF x = 5 THEN"+
            "            resultado := x * y"+
            "        ELSE"+
            "            resultado := x - 1;"+
            "        "+
            "        x := x - 1;"+
            "    END;"+
            "END."+
        "";

        System.out.println("Compilando código...\n");

        Lexer lexer = new Lexer(input);
        Parser parser = new Parser(lexer);
        
        try {
            Node ast = parser.parse();
            TAC tacGen = new TAC();
            tacGen.generate(ast);
            tacGen.printTAC();
            
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
    }
}