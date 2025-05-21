package syntaxtree;

import syntaxtree.visitor.Visitor;

public class ArrayLength extends Exp {
    public Exp e;
    public ArrayLength(Exp e) { this.e = e; }
    public <T> T accept(Visitor<T> visitor) { return visitor.visit(this); }
}
