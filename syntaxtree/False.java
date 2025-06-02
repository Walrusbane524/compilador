package syntaxtree;

import syntaxtree.visitor.Visitor;

public class False extends Exp {
    public <T> T accept(Visitor<T> visitor) { return visitor.visit(this); }
}
