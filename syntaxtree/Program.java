package syntaxtree;

import syntaxtree.visitor.Visitor;

public class Program {
    public MainClass m;
    public ClassDeclList cl;

    public Program(MainClass m, ClassDeclList cl) {
        this.m = m;
        this.cl = cl;
    }

    public String accept(Visitor<String> visitor){
        return visitor.visit(this);
    }
}
