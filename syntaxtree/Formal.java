package syntaxtree;

import syntaxtree.visitor.Visitor;

public class Formal {
    public Type t;
    public Identifier i;

    public Formal(Type t, Identifier i) {
        this.t = t;
        this.i = i;
    }
    public <T> T accept(Visitor<T> visitor) { return visitor.visit(this); }
}
