package syntaxtree;

import syntaxtree.visitor.Visitor;

public class MainClass {
    public Identifier i1, i2;
    public Statement s;

    public MainClass(Identifier i1, Identifier i2, Statement s) {
        this.i1 = i1;
        this.i2 = i2;
        this.s = s;
    }
    public <T> T accept(Visitor<T> visitor) { return visitor.visit(this); }
}
