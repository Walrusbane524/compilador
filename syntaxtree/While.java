package syntaxtree;

import syntaxtree.visitor.Visitor;

public class While extends Statement {
    public Exp e;
    public Statement s;
    public While(Exp e, Statement s) {
        this.e = e;
        this.s = s;
    }

    public <T> T accept(Visitor<T> visitor) { return visitor.visit(this); }
}
