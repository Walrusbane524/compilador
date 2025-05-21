package syntaxtree;

import syntaxtree.visitor.Visitor;

public class False extends Exp {
    public String accept(Visitor<String> visitor){
        return visitor.visit(this);
    }
}
