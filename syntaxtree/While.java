package syntaxtree;

import syntaxtree.visitor.Visitor;

public class While extends Statement {
    public Exp e;
    public Statement s;
    public While(Exp e, Statement s) {
        this.e = e;
        this.s = s;
    }

    public String accept(Visitor<String> visitor){
        return visitor.visit(this);
    }
}
