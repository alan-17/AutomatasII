package ast;

import ast.Node;

public class ProgramNode extends Node {
    public String name;
    public BlockNode block;
    public ProgramNode(String name, BlockNode block) {
        this.name = name;
        this.block = block;
    }
}