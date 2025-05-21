package syntaxtree;

import syntaxtree.visitor.Visitor;

public abstract class Type {
    
    public abstract <T> T accept(Visitor<T> visitor);
}



