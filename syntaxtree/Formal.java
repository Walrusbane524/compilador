package syntaxtree;

import syntaxtree.visitor.Visitor;

public class Formal {
    public Type t;
    public Identifier i;

    public Formal(Type t, Identifier i) {
        this.t = t;
        this.i = i;
    }
    public String accept(Visitor<String> visitor){
        return visitor.visit(this);
    }
}
