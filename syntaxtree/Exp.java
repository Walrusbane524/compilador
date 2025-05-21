package syntaxtree;

import syntaxtree.visitor.Visitor;

public abstract class Exp {
    
    public abstract String accept(Visitor<String> visitor);
}



