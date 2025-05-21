package syntaxtree;

import syntaxtree.visitor.Visitor;

public class Assign extends Statement {
    public Identifier i;
    public Exp e;
    public Assign(Identifier i, Exp e) {
        this.i = i;
        this.e = e;
    }
    public <T> T accept(Visitor<T> visitor) { return visitor.visit(this); }
}
