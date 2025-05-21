package syntaxtree;

import syntaxtree.visitor.Visitor;

public abstract class ClassDecl {
    public abstract <T> T accept(Visitor<T> visitor);
} 
