package syntaxtree;

import syntaxtree.visitor.Visitor;

public class MethodDecl {
    public Type t;
    public Identifier i;
    public FormalList fl;
    public VarDeclList vl;
    public StatementList sl;
    public Exp e;

    public MethodDecl(Type t, Identifier i, FormalList fl, VarDeclList vl, StatementList sl, Exp e) {
        this.t = t;
        this.i = i;
        this.fl = fl;
        this.vl = vl;
        this.sl = sl;
        this.e = e;
    }

    

    public String accept(Visitor<String> visitor){
        return visitor.visit(this);
    }
}
