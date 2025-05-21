package syntaxtree;

import syntaxtree.visitor.Visitor;

public class ClassDeclSimple extends ClassDecl {
    public Identifier i;
    public VarDeclList vl;
    public MethodDeclList ml;

    public ClassDeclSimple(Identifier i, VarDeclList vl, MethodDeclList ml) {
        this.i = i;
        this.vl = vl;
        this.ml = ml;
    }
    public String accept(Visitor<String> visitor){
        return visitor.visit(this);
    }
}