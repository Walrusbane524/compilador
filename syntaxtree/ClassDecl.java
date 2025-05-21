package syntaxtree;

import syntaxtree.visitor.Visitor;

public abstract class ClassDecl {
    public abstract String accept(Visitor<String> visitor);
}
