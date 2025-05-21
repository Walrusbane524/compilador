package syntaxtree;

import syntaxtree.visitor.Visitor;

public class IdentifierType extends Type {
    public String s;

    public IdentifierType(String s) {
        this.s = s;
    }

    public String accept(Visitor<String> visitor){
        return visitor.visit(this);
    }
}
