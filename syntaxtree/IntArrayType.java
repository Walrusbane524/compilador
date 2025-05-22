package syntaxtree;

import syntaxtree.visitor.Visitor;

public class IntArrayType extends Type {

    public <T> T accept(Visitor<T> visitor) { return visitor.visit(this); }

    @Override
    public String toString(){
        return "int[]";
    }
}
