package syntaxtree;

import syntaxtree.visitor.Visitor;

public class If extends Statement {
    public Exp e;
    public Statement s1, s2;
    public If(Exp e, Statement s1, Statement s2) {
        this.e = e;
        this.s1 = s1;
        this.s2 = s2;
    }

    public String accept(Visitor<String> visitor){
        return visitor.visit(this);
    }
}
