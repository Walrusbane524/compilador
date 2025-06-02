package syntaxtree;

import syntaxtree.visitor.Visitor;

public class ClassDeclExtends extends ClassDecl {
    public Identifier i, j;
    public VarDeclList vl;
    public MethodDeclList ml;

    public ClassDeclExtends(Identifier i, Identifier j, VarDeclList vl, MethodDeclList ml) {
        this.i = i;
        this.j = j;
        this.vl = vl;
        this.ml = ml;
    }
    public <T> T accept(Visitor<T> visitor) { return visitor.visit(this); }
}
