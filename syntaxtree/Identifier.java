package syntaxtree;

import syntaxtree.visitor.Visitor;

public class Identifier {
    public String s;

    public Identifier(String s) {
        this.s = s;
    }

    public String accept(Visitor<String> visitor){
        return visitor.visit(this);
    }
}
