package syntaxtree;

import syntaxtree.visitor.Visitor;

public abstract class Statement {
    
    public abstract <T> T accept(Visitor<T> visitor);
}