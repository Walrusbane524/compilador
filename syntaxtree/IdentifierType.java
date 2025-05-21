package syntaxtree;

import syntaxtree.visitor.Visitor;

public class IdentifierType extends Type {
    public String s;

    public IdentifierType(String s) {
        this.s = s;
    }

    public <T> T accept(Visitor<T> visitor) { return visitor.visit(this); }
}
