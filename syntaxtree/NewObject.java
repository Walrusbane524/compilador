package syntaxtree;

import syntaxtree.visitor.Visitor;

public class NewObject extends Exp {
    public Identifier i;
    public NewObject(Identifier i) { this.i = i; }

    public String accept(Visitor<String> visitor){
        return visitor.visit(this);
    }
}
