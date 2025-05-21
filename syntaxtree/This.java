package syntaxtree;

import syntaxtree.visitor.Visitor;

public class This extends Exp {
    public <T> T accept(Visitor<T> visitor) { return visitor.visit(this); }
}
