package syntaxtree;

import syntaxtree.visitor.Visitor;

public class BooleanType extends Type {
    public String accept(Visitor<String> visitor){
        return visitor.visit(this);
    }
}
