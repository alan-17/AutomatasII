package TAC;

import AST.*;
import java.util.ArrayList;
import java.util.List;

public class TAC {
    private int tempCounter = 1;
    private int labelCounter = 1;
    private List<String> instruccionesTAC = new ArrayList<>();

    private String getNewTemp() {
        return "t" + (tempCounter++);
    }

    private String getNewLabel() {
        return "L" + (labelCounter++);
    }

    private void emit(String instruccion) {
        instruccionesTAC.add(instruccion);
    }

    public void generate(Node raiz) {
        visit(raiz);
    }

    public void printTAC() {
        System.out.println("--- Código de Tres Direcciones ---");
        for (String inst : instruccionesTAC) {
            System.out.println(inst);
        }
    }

    private String visit(Node nodo) {
        if (nodo == null) return null;

        if (nodo instanceof ProgramNode) {
            ProgramNode program = (ProgramNode) nodo;
            emit("; Inicia programa: " + program.name);
            visit(program.block);
            emit("; Fin del programa");
            return null;

        } else if (nodo instanceof BlockNode) {
            BlockNode block = (BlockNode) nodo;
            for (Node decl : block.declarations) {
                visit(decl);
            }
            visit(block.compoundStatement);
            return null;

        } else if (nodo instanceof VarDeclNode) {
            VarDeclNode varDecl = (VarDeclNode) nodo;
            for (String var : varDecl.variables) {
                emit("; Declarar " + var + " como " + varDecl.type.getLexema());
            }
            return null;

        } else if (nodo instanceof CompoundNode) {
            CompoundNode compound = (CompoundNode) nodo;
            for (Node stmt : compound.statements) {
                visit(stmt);
            }
            return null;

        } else if (nodo instanceof NumberNode) {
            return ((NumberNode) nodo).value;

        } else if (nodo instanceof IdentifierNode) {
            return ((IdentifierNode) nodo).name;

        } else if (nodo instanceof OperatorNode) {
            OperatorNode binNode = (OperatorNode) nodo;
            String leftVal = visit(binNode.left);
            String rightVal = visit(binNode.right);
            String temp = getNewTemp();
            emit(temp + " = " + leftVal + " " + binNode.op.getLexema() + " " + rightVal);
            return temp;

        } else if (nodo instanceof AssignNode) {
            AssignNode assignNode = (AssignNode) nodo;
            String rightVal = visit(assignNode.right);
            emit(assignNode.left.name + " = " + rightVal);
            return null;

        } else if (nodo instanceof IfNode) {
            IfNode ifNode = (IfNode) nodo;
            
            String condTemp = visit(ifNode.condition);
            
            String labelElse = getNewLabel();
            String labelEnd = getNewLabel();

            emit("ifFalse " + condTemp + " goto " + labelElse);
            
            visit(ifNode.thenStmt);
            emit("goto " + labelEnd);
            
            emit(labelElse + ":");
            if (ifNode.elseStmt != null) {
                visit(ifNode.elseStmt);
            }
            
            emit(labelEnd + ":");
            return null;

        } else if (nodo instanceof WhileNode) {
            WhileNode whileNode = (WhileNode) nodo;
            
            String labelStart = getNewLabel();
            String labelEnd = getNewLabel();

            emit(labelStart + ":");
            
            String condTemp = visit(whileNode.condition);
            
            emit("ifFalse " + condTemp + " goto " + labelEnd);
            
            visit(whileNode.body);
            
            emit("goto " + labelStart);
            
            emit(labelEnd + ":");
            return null;
        }

        throw new RuntimeException("Nodo desconocido en la generación de código TAC: " + nodo.getClass().getSimpleName());
    }
}