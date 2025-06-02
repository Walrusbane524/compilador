package syntaxtree;

import syntaxtree.visitor.Visitor;

public class IdentifierExp extends Exp {
    public String s;
    public IdentifierExp(String s) { this.s = s; }
    
    public <T> T accept(Visitor<T> visitor) { return visitor.visit(this); }
}
