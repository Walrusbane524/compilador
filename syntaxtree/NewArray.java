package syntaxtree;

import syntaxtree.visitor.Visitor;

public class NewArray extends Exp {
    public Exp size;

    public NewArray(Exp size) {
        this.size = size;
    }

    public <T> T accept(Visitor<T> visitor) { return visitor.visit(this); }
}
