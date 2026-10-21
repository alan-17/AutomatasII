package lexer;

public enum TokenType {
    // Operadores matemáticos (addop / mulop)
    SUM, SUB, MUL, DIV, OR, AND,
    
    // Símbolos
    LPARENT, RPARENT, SEMICOLON, 
    COLON,     // :
    COMMA,     // ,
    DOT,       // .
    
    // Operadores relacionales (relop)
    EQUAL,         // =
    NOT_EQUAL,     // <>
    LESS_THAN,     // <
    LESS_EQUAL,    // <=
    GREATER_THAN,  // >
    GREATER_EQUAL, // >=
    
    // Asignación
    ASSIGN,        // :=
    
    // Tipos de datos
    INTEGER, REAL, BOOLEAN,
    
    // Literales y Variables
    INT_LITERAL, FLOAT_LITERAL, STRING_LITERAL, ID,
    
    // Palabras reservadas (Estructura)
    PROGRAM, VAR, BEGIN, END,
    
    // Palabras reservadas (Control de flujo)
    IF, THEN, ELSE,
    WHILE, DO,
    
    EOF
}