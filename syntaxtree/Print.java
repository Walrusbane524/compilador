package syntaxtree;

import syntaxtree.visitor.Visitor;

public class Print extends Statement {
    public Exp e;
    public Print(Exp e) { this.e = e; }
    public String accept(Visitor<String> visitor){
        return visitor.visit(this);
    }
}
