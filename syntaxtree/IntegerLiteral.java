package syntaxtree;

import syntaxtree.visitor.Visitor;

public class IntegerLiteral extends Exp {
    public int i;
    public IntegerLiteral(int i) { this.i = i; }
    public <T> T accept(Visitor<T> visitor) { return visitor.visit(this); }
}
