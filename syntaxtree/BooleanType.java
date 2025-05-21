package syntaxtree;

import syntaxtree.visitor.Visitor;

public class BooleanType extends Type {
    public <T> T accept(Visitor<T> visitor) { return visitor.visit(this); }
}
