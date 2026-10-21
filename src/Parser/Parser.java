package parser;

import ast.*;
import lexer.Lexer;
import lexer.Token;
import lexer.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private Lexer lexer;
    private Token currentToken;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.currentToken = lexer.nextToken();
    }

    private void eat(TokenType tipoEsperado) {
        if (currentToken.getAtributo() == tipoEsperado) {
            currentToken = lexer.nextToken();
        } else {
            throw new RuntimeException("Error de sintaxis: se esperaba " + tipoEsperado + 
                    " pero se encontró " + currentToken.getAtributo() + " (" + currentToken.getLexema() + ")");
        }
    }

    // program → PROGRAM identifier ; block .
    public Node parse() {
        eat(TokenType.PROGRAM);
        String progName = currentToken.getLexema();
        eat(TokenType.ID);
        eat(TokenType.SEMICOLON);
        
        BlockNode blockNode = block();
        
        eat(TokenType.DOT);
        
        if (currentToken.getAtributo() != TokenType.EOF) {
            throw new RuntimeException("Error: código extra después de '.'");
        }
        return new ProgramNode(progName, blockNode);
    }

    // block → declaration_part statement_part
    private BlockNode block() {
        List<Node> declarations = declarationPart();
        Node compoundStmt = statementPart();
        return new BlockNode(declarations, compoundStmt);
    }

    // declaration_part → VAR declaration_list | ε
    private List<Node> declarationPart() {
        List<Node> declarations = new ArrayList<>();
        if (currentToken.getAtributo() == TokenType.VAR) {
            eat(TokenType.VAR);
            declarations = declarationList();
        }
        return declarations;
    }

    // declaration_list → declaration ; declaration_list | declaration
    private List<Node> declarationList() {
        List<Node> declarations = new ArrayList<>();
        declarations.add(declaration());
        
        while (currentToken.getAtributo() == TokenType.SEMICOLON) {
            // Un pequeño truco para mirar hacia adelante: si el siguiente no es un ID, cortamos.
            // Para mantenerlo simple, consumimos el ';' y procesamos otra declaración.
            eat(TokenType.SEMICOLON);
            if (currentToken.getAtributo() == TokenType.ID) {
                declarations.add(declaration());
            } else {
                break;
            }
        }
        return declarations;
    }

    // declaration → identifier_list : type
    private Node declaration() {
        List<String> ids = identifierList();
        eat(TokenType.COLON);
        Token type = currentToken;
        if (type.getAtributo() == TokenType.INTEGER || 
            type.getAtributo() == TokenType.REAL || 
            type.getAtributo() == TokenType.BOOLEAN) {
            eat(type.getAtributo());
        } else {
            throw new RuntimeException("Tipo de dato inválido: " + type.getLexema());
        }
        return new VarDeclNode(ids, type);
    }

    // identifier_list → identifier , identifier_list | identifier
    private List<String> identifierList() {
        List<String> ids = new ArrayList<>();
        ids.add(currentToken.getLexema());
        eat(TokenType.ID);
        
        while (currentToken.getAtributo() == TokenType.COMMA) {
            eat(TokenType.COMMA);
            ids.add(currentToken.getLexema());
            eat(TokenType.ID);
        }
        return ids;
    }

    // statement_part → BEGIN statement_list END
    private Node statementPart() {
        return compoundStatement();
    }

    // compound_statement → BEGIN statement_list END
    private Node compoundStatement() {
        eat(TokenType.BEGIN);
        List<Node> stmts = statementList();
        eat(TokenType.END);
        return new CompoundNode(stmts);
    }

    // statement_list → statement ; statement_list | statement
    private List<Node> statementList() {
        List<Node> stmts = new ArrayList<>();
        stmts.add(statement());
        
        while (currentToken.getAtributo() == TokenType.SEMICOLON) {
            eat(TokenType.SEMICOLON);
            // Si vemos un END, significa que la lista de sentencias ya terminó
            if (currentToken.getAtributo() == TokenType.END) {
                break;
            }
            stmts.add(statement());
        }
        return stmts;
    }

    // statement → assignment | if_statement | while_statement | compound_statement
    private Node statement() {
        if (currentToken.getAtributo() == TokenType.ID) {
            return assignment();
        } else if (currentToken.getAtributo() == TokenType.IF) {
            return ifStatement();
        } else if (currentToken.getAtributo() == TokenType.WHILE) {
            return whileStatement();
        } else if (currentToken.getAtributo() == TokenType.BEGIN) {
            return compoundStatement();
        } else {
            throw new RuntimeException("Sentencia inválida, token inesperado: " + currentToken.getLexema());
        }
    }

    // assignment → identifier := expression
    private Node assignment() {
        IdentifierNode left = new IdentifierNode(currentToken);
        eat(TokenType.ID);
        Token op = currentToken;
        eat(TokenType.ASSIGN); // :=
        Node right = expression();
        return new AssignNode(left, op, right);
    }

    // if_statement → IF expression THEN statement ELSE statement
    private Node ifStatement() {
        eat(TokenType.IF);
        Node cond = expression();
        eat(TokenType.THEN);
        Node thenStmt = statement();
        
        Node elseStmt = null;
        if (currentToken.getAtributo() == TokenType.ELSE) {
            eat(TokenType.ELSE);
            elseStmt = statement();
        }
        return new IfNode(cond, thenStmt, elseStmt);
    }

    // while_statement → WHILE expression DO statement
    private Node whileStatement() {
        eat(TokenType.WHILE);
        Node cond = expression();
        eat(TokenType.DO);
        Node body = statement();
        return new WhileNode(cond, body);
    }

    // expression → simple_expression relop simple_expression | simple_expression
    private Node expression() {
        Node left = simpleExpression();
        TokenType type = currentToken.getAtributo();
        
        // relop → = | <> | < | <= | > | >=
        if (type == TokenType.EQUAL || type == TokenType.NOT_EQUAL || 
            type == TokenType.LESS_THAN || type == TokenType.LESS_EQUAL || 
            type == TokenType.GREATER_THAN || type == TokenType.GREATER_EQUAL) {
            Token op = currentToken;
            eat(type);
            Node right = simpleExpression();
            return new OperatorNode(left, op, right);
        }
        return left;
    }

    // simple_expression → term addop term | term
    private Node simpleExpression() {
        Node left = term();
        TokenType type = currentToken.getAtributo();
        
        // addop → + | - | OR
        if (type == TokenType.SUM || type == TokenType.SUB || type == TokenType.OR) {
            Token op = currentToken;
            eat(type);
            Node right = term();
            return new OperatorNode(left, op, right);
        }
        return left;
    }

    // term → factor mulop factor | factor
    private Node term() {
        Node left = factor();
        TokenType type = currentToken.getAtributo();
        
        // mulop → * | / | AND
        if (type == TokenType.MUL || type == TokenType.DIV || type == TokenType.AND) {
            Token op = currentToken;
            eat(type);
            Node right = factor();
            return new OperatorNode(left, op, right);
        }
        return left;
    }

    // factor → identifier | number | ( expression )
    private Node factor() {
        Token token = currentToken;
        if (token.getAtributo() == TokenType.ID) {
            eat(TokenType.ID);
            return new IdentifierNode(token);
        } else if (token.getAtributo() == TokenType.INT_LITERAL || token.getAtributo() == TokenType.FLOAT_LITERAL) {
            eat(token.getAtributo());
            return new NumberNode(token);
        } else if (token.getAtributo() == TokenType.LPARENT) {
            eat(TokenType.LPARENT);
            Node node = expression();
            eat(TokenType.RPARENT);
            return node;
        }
        throw new RuntimeException("Error en factor: token inesperado " + currentToken.getLexema());
    }
}