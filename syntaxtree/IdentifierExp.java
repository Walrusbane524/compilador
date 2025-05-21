package syntaxtree;

import syntaxtree.visitor.Visitor;

public class IdentifierExp extends Exp {
    public String s;
    public IdentifierExp(String s) { this.s = s; }
    
    public String accept(Visitor<String> visitor){
        return visitor.visit(this);
    }
}
