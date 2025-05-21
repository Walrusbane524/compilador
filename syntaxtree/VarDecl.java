package syntaxtree;

import syntaxtree.visitor.Visitor;

public class VarDecl {
    public Type t;
    public Identifier i;

    public VarDecl(Type t, Identifier i) {
        this.t = t;
        this.i = i;
    }

    public String accept(Visitor<String> visitor){
        return visitor.visit(this);
    }
}
