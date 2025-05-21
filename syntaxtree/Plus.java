package syntaxtree;

import syntaxtree.visitor.Visitor;

public class Plus extends Exp {
    public Exp e1, e2;
    public Plus(Exp e1, Exp e2) { this.e1 = e1; this.e2 = e2; }
    public <T> T accept(Visitor<T> visitor) { return visitor.visit(this); }
}
