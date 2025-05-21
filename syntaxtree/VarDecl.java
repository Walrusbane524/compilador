package syntaxtree;

import syntaxtree.visitor.Visitor;

public class VarDecl {
    public Type t;
    public Identifier i;

    public VarDecl(Type t, Identifier i) {
        this.t = t;
        this.i = i;
    }

    public <T> T accept(Visitor<T> visitor) { return visitor.visit(this); }
}
