package syntaxtree;
import syntaxtree.visitor.Visitor;

public class ArrayAssign extends Statement {
    public Identifier i;
    public Exp e1, e2;
    public ArrayAssign(Identifier i, Exp e1, Exp e2) {
        this.i = i;
        this.e1 = e1;
        this.e2 = e2;
    }
    public <T> T accept(Visitor<T> visitor) { return visitor.visit(this); }
}
