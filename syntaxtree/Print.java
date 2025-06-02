package syntaxtree;

import syntaxtree.visitor.Visitor;

public class Print extends Statement {
    public Exp e;
    public Print(Exp e) { this.e = e; }
    public <T> T accept(Visitor<T> visitor) { return visitor.visit(this); }
}
