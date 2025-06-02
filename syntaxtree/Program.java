package syntaxtree;

import syntaxtree.visitor.Visitor;

public class Program {
    public MainClass m;
    public ClassDeclList cl;
 
    public Program(MainClass m, ClassDeclList cl) {
        this.m = m;
        this.cl = cl;
    }

    public <T> T accept(Visitor<T> visitor) { return visitor.visit(this); }
}