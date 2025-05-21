package syntaxtree;

import syntaxtree.visitor.Visitor;

public class NewArray extends Exp {
    public Exp size;

    public NewArray(Exp size) {
        this.size = size;
    }

    public String accept(Visitor<String> visitor){
        return visitor.visit(this);
    }
}
