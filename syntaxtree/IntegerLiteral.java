package syntaxtree;

import syntaxtree.visitor.Visitor;

public class IntegerLiteral extends Exp {
    public int i;
    public IntegerLiteral(int i) { this.i = i; }
    public String accept(Visitor<String> visitor){
        return visitor.visit(this);
    }
}
