package syntaxtree;

import syntaxtree.visitor.Visitor;

public class Not extends Exp {
    public Exp e;
    public Not(Exp e) { this.e = e; }
    public <T> T accept(Visitor<T> visitor) { return visitor.visit(this); }
}
