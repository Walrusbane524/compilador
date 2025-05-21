package syntaxtree;

import syntaxtree.visitor.Visitor;

public abstract class Statement {
    
    public abstract String accept(Visitor<String> visitor);
}