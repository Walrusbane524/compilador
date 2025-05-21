package syntaxtree;

import syntaxtree.visitor.Visitor;

public class Call extends Exp {
    public Exp e;
    public Identifier i;
    public ExpList el;
    public Call(Exp e, Identifier i, ExpList el) {
        this.e = e;
        this.i = i;
        this.el = el;
    }
    public <T> T accept(Visitor<T> visitor) { return visitor.visit(this); }
}