package syntaxtree;

import syntaxtree.visitor.Visitor;

public class IntegerType extends Type {
    public String accept(Visitor<String> visitor){
        return visitor.visit(this);
    }
}
