package syntaxtree;

import syntaxtree.visitor.Visitor;

public abstract class Type {
    
    public abstract String accept(Visitor<String> visitor);
}



