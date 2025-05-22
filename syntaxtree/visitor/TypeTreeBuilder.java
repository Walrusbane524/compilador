package syntaxtree.visitor;

import syntaxtree.*;

public class TypeTreeBuilder implements Visitor<Void> {
    public ClassType currClass = null;
    public MethodType currMethod = null;
    public ErrorMsg error = new ErrorMsg();

    public TypeTree tree = new TypeTree();

    public Void visit(And n) { return null; }
    public Void visit(LessThan n) { return null; }
    public Void visit(Plus n) { return null; }
    public Void visit(Minus n) { return null; }
    public Void visit(Times n) { return null; }
    public Void visit(ArrayLookup n) { return null; }
    public Void visit(Call n) { return null; }
    public Void visit(ArrayLength n) { return null; }
    public Void visit(Not n) { return null; }
    public Void visit(IdentifierExp n) { return null; }
    public Void visit(IntegerLiteral n) { return null; }
    public Void visit(NewArray n) { return null; }
    public Void visit(NewObject n) { return null; }
    public Void visit(True n) { return null; }
    public Void visit(False n) { return null; }
    public Void visit(This n) { return null; }
    public Void visit(Block s) { return null; }
    public Void visit(If s) { return null; }
    public Void visit(While s) { return null; }
    public Void visit(Print s) { return null; }
    public Void visit(Assign s) { return null; }
    public Void visit(ArrayAssign s) { return null; }
    public Void visit(Identifier i) { return null; }

    public Void visit(ClassDeclSimple n) {
        String id = n.i.toString();
        System.out.println("Defining class: " + id);
        currClass = new ClassType(id);

        for (int i = 0; i < n.vl.size(); i++)
            n.vl.get(i).accept(this);
        for (int i = 0; i < n.ml.size(); i++)
            n.ml.get(i).accept(this);

        if (!tree.addClass(id, currClass))
            error.complain("(classdeclsimple)" + id + " is already defined");

        currClass = null;
        return null;
    }

    public Void visit(ClassDeclExtends n) {
        String id = n.i.toString();
        System.out.println("Defining class: " + id);
        String sc = n.j.toString();
        currClass = new ClassType(id, sc);

        for (int i = 0; i < n.vl.size(); i++)
            n.vl.get(i).accept(this);
        for (int i = 0; i < n.ml.size(); i++)
            n.ml.get(i).accept(this);

        if (!tree.addClass(id, currClass))
            error.complain("(classdeclextends)" + id + " is already defined");

        currClass = null;
        return null;
    }

    public Void visit(MainClass n) {
        return null;
    }

    public Void visit(VarDecl n) {
        Type t = n.t;
        String id = n.i.toString();
        System.out.println("Defining var: " + id);

        if (currMethod == null) {
            if (!currClass.addVar(id, t))
                error.complain("(methodnull) " + id + " is already defined in " + currClass.getId());
        }
        else if (!currMethod.addVar(id,t))
            error.complain("(var)" + id + " is already defined in "  + currClass.getId() + "." + currMethod.getId());
        return null;
    }

    public Void visit(MethodDecl n) {
        String id = n.i.toString();
        Type rt = n.t;
        System.out.println("Defining method: " + id);

        currMethod = new MethodType(id, rt);

        if (!currClass.addMethod(id, currMethod))
            error.complain("(methoddecl)" + id + " is already defined in "  + currClass.getId() + "." + currMethod.getId());

        for (int i = 0; i < n.fl.size(); i++)
            n.fl.get(i).accept(this);
        for (int i = 0; i < n.vl.size(); i++)
            n.vl.get(i).accept(this);

        currMethod = null;
        return null;
    }

    public Void visit(Formal n) {
        Type t = n.t;
        String id = n.i.toString();
        System.out.println("Defining formal: " + id);

        if (!currMethod.addArg(id,t))
            error.complain("(formal)" + id + " is already defined in "  + currClass.getId() + "." + currMethod.getId());
        return null;
    }

    public Void visit(IntArrayType n) { return null; }
    public Void visit(BooleanType n) { return null; }
    public Void visit(IntegerType n) { return null; }
    public Void visit(IdentifierType n) { return null; }

    public Void visit(Program p) {
        System.out.println("Defining program");
        p.m.accept(this);
        for (int i = 0; i < p.cl.size(); i++)
            p.cl.get(i).accept(this);

        if (error.anyErrors)
            tree = new TypeTree();
        return null;
    }

}
