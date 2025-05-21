package syntaxtree;

import syntaxtree.visitor.Visitor;

public abstract class Exp {
    
    public abstract <T> T accept(Visitor<T> visitor);
}



