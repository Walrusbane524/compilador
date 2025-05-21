package syntaxtree;

import syntaxtree.visitor.Visitor;

public class NewObject extends Exp {
    public Identifier i;
    public NewObject(Identifier i) { this.i = i; }

    public <T> T accept(Visitor<T> visitor) { return visitor.visit(this); }
}
