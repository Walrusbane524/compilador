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
    public <T> T accept(Visitor<T> visitor) { return visitor.visit(this); }
}