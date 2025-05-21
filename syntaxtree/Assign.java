package syntaxtree;

import syntaxtree.visitor.Visitor;

public class Assign extends Statement {
    public Identifier i;
    public Exp e;
    public Assign(Identifier i, Exp e) {
        this.i = i;
        this.e = e;
    }
    public String accept(Visitor<String> visitor){
        return visitor.visit(this);
    }
}
