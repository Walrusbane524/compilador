package syntaxtree.visitor;

import syntaxtree.And;
import syntaxtree.ArrayAssign;
import syntaxtree.ArrayLength;
import syntaxtree.ArrayLookup;
import syntaxtree.Assign;
import syntaxtree.Block;
import syntaxtree.BooleanType;
import syntaxtree.Call;
import syntaxtree.ClassDeclExtends;
import syntaxtree.ClassDeclSimple;
import syntaxtree.ClassType;
import syntaxtree.ErrorMsg;
import syntaxtree.False;
import syntaxtree.Formal;
import syntaxtree.Identifier;
import syntaxtree.IdentifierExp;
import syntaxtree.IdentifierType;
import syntaxtree.If;
import syntaxtree.IntArrayType;
import syntaxtree.IntegerLiteral;
import syntaxtree.IntegerType;
import syntaxtree.LessThan;
import syntaxtree.MainClass;
import syntaxtree.MethodDecl;
import syntaxtree.MethodType;
import syntaxtree.Minus;
import syntaxtree.NewArray;
import syntaxtree.NewObject;
import syntaxtree.Not;
import syntaxtree.Plus;
import syntaxtree.Print;
import syntaxtree.Program;
import syntaxtree.This;
import syntaxtree.Times;
import syntaxtree.True;
import syntaxtree.Type;
import syntaxtree.TypeTree;
import syntaxtree.VarDecl;
import syntaxtree.While;

public class TypeChecker implements Visitor<Type>{
    public ErrorMsg error = new ErrorMsg();
    public TypeTree tree;
    public ClassType currClass = null;
    public MethodType currMethod = null;

    public TypeChecker(TypeTree t) { tree = t; }

    private ClassType getClassType(Type idType) {
        if (isPrimitive(idType)) {
            error.complain(idType.toString() + " is primitive");
            return null;
        }

        String className = ((IdentifierType)idType).s;
        return tree.classes.get(className);
    }

    private MethodType getMethod(ClassType c, String methodName) {
        MethodType methodType = c.methods.get(methodName);
        if (methodType == null && c.getSuperClass() != null) {
            ClassType sc = tree.classes.get(c.getSuperClass());
            if (sc != null)
                return getMethod(sc, methodName);
        }
        return methodType;
    }

    private Type getIdentifierType(ClassType c, String idName) {
        String typeName = currMethod.vars.get(idName);
        if (typeName == null) typeName = c.vars.get(idName);
        if (typeName != null) {
            return Type.type(typeName);
        }

        if (c.getSuperClass() != null) {
            ClassType sc = tree.classes.get(c.getSuperClass());
            if (sc != null)
                return getIdentifierType(sc, idName);
        }

        return null;
    }

    private boolean isPrimitive(Type t) {
        return !(t instanceof IdentifierType);
    }

    private boolean isDerived(Type t1, Type t2) {
        if (t1 == null || t2 == null || t1.equals(t2)) return true;

        ClassType cSub = getClassType(t1);
        ClassType cSup = getClassType(t2);
        if (cSub == null || cSup == null) return false;

        while (cSub.getSuperClass() != null) {
            cSub = tree.classes.get(cSub.getSuperClass());
            if (cSub != null && cSup.getId().equals(cSub.getId())) return true;
        }
        return false;
    }

    private boolean checkIfTypeExists(Type t) {
        if (t == null || !(t instanceof IdentifierType))
            return true;

        String className = ((IdentifierType)t).s;
        if (tree.classes.get(className) == null) {
            error.complain(t.toString() + " not defined");
            return false;
        }
        return true;
    }

    public Type visit(And n) {
        Type e1Type = n.e1.accept(this);
        Type e2Type = n.e2.accept(this);

        if (e1Type == null) e1Type = new BooleanType();
        if (e2Type == null) e2Type = new BooleanType();

        if (!(e1Type instanceof BooleanType))
            error.complain("Left side of And must be of type boolean");
        if (!(e2Type instanceof BooleanType))
            error.complain("Right side of And must be of type boolean");
        return new BooleanType();
    }

    public Type visit(LessThan n) {
        Type e1Type = n.e1.accept(this);
        Type e2Type = n.e2.accept(this);

        if (e1Type == null) e1Type = new IntegerType();
        if (e2Type == null) e2Type = new IntegerType();

        if (!(e1Type instanceof IntegerType))
            error.complain("Left side of LessThan must be of type int");
        if (!(e2Type instanceof IntegerType))
            error.complain("Right side of LessThan must be of type int");

        return new BooleanType();
    }

    public Type visit(Plus n) {
        Type e1Type = n.e1.accept(this);
        Type e2Type = n.e2.accept(this);

        if (e1Type == null) e1Type = new IntegerType();
        if (e2Type == null) e2Type = new IntegerType();

        if (!(e1Type instanceof IntegerType))
            error.complain("Left side of Plus must be of type int");
        if (!(e2Type instanceof IntegerType))
            error.complain("Right side of Plus must be of type int");

        return new IntegerType();
    }


    public Type visit(Minus n) {
        Type e1Type = n.e1.accept(this);
        Type e2Type = n.e2.accept(this);

        if (e1Type == null) e1Type = new IntegerType();
        if (e2Type == null) e2Type = new IntegerType();

        if (!(e1Type instanceof IntegerType))
            error.complain("Left side of Minus must be of type int");
        if (!(e2Type instanceof IntegerType))
            error.complain("Right side of Minus must be of type int");

        return new IntegerType();
    }


    public Type visit(Times n) {
        Type e1Type = n.e1.accept(this);
        Type e2Type = n.e2.accept(this);

        if (e1Type == null) e1Type = new IntegerType();
        if (e2Type == null) e2Type = new IntegerType();

        if (!(e1Type instanceof IntegerType))
            error.complain("Left side of Times must be of type int");
        if (!(e2Type instanceof IntegerType))
            error.complain("Right side of Times must be of type int");

        return new IntegerType();
    }


    public Type visit(ArrayLookup n) {
        Type arrayType = n.e1.accept(this);
        Type indexType = n.e2.accept(this);

        if (arrayType == null) arrayType = new IntArrayType();
        if (indexType == null) indexType = new IntegerType();

        if (!(arrayType instanceof IntArrayType))
            error.complain("Left side of ArrayLookup must be of type int[]");
        if (!(indexType instanceof IntegerType))
            error.complain("Right side of ArrayLookup must be of type int");

        return new IntegerType();
    }


    public Type visit(Call n) {
        Type objType = n.e.accept(this);
        if (isPrimitive(objType)) {
            error.complain("Object of method call must be a reference type, cannot call " + n.i.s + "()");
            return null;
        }

        ClassType classType = getClassType(objType);
        if (classType == null) {
            error.complain("Object of method call must have a defined class type");
            return null;
        }

        MethodType methodType = getMethod(classType, n.i.s);
        if (methodType == null)
            error.complain("Method " + n.i.s + " not found in class " + classType.id);

        if (methodType != null && methodType.args.size() != n.el.size())
            error.complain("Method " + n.i.s + " called with wrong number of arguments");

        if (methodType != null) {
            for (int i = 0; i < methodType.args.size(); i++) {
                Type paramType = n.el.get(i).accept(this);
                Type expectedType = Type.type(methodType.args.get(i));

                if (paramType == null) continue;
                if (!isDerived(paramType, expectedType)) {
                    error.complain("Incorrect parameter type at position " + (i + 1) + " in call to method " + n.i.s);
                }
            }
            return methodType.getReturnType();
        }

        return null;
    }


    public Type visit(ArrayLength n) {
        if (!(n.e.accept(this) instanceof IntArrayType))
            error.complain("Expression in ArrayLength must be of type int[]");
        return new IntegerType();
    }


    public Type visit(Not n) {
        Type t = n.e.accept(this);
        if (t != null && !(t instanceof BooleanType))
            error.complain("Expression in Not must be of type boolean");
        return new BooleanType();
    }


    public Type visit(IdentifierExp n) {
        String idName = n.s;
        return getIdentifierType(currClass, idName);
    }

    public Type visit(IntegerLiteral n) {
        return new IntegerType();
    }

    public Type visit(NewArray n) {
        return new IntArrayType();
    }

    public Type visit(NewObject n) {
        return Type.type(n.i.s);
    }

    public Type visit(True n) {
        return new BooleanType();
    }

    public Type visit(False n) {
        return new BooleanType();
    }

    public Type visit(This n) {
        return Type.type(currClass.getId());
    }

    public Type visit(Block s) {
        for (int i = 0; i < s.sl.size(); i++) {
            s.sl.get(i).accept(this);
        }
        return null;
    }

    public Type visit(If s) {
        Type expType = s.e.accept(this);
        if (expType != null && !(expType instanceof BooleanType))
            error.complain("Condition in If statement must be of type boolean");

        s.s1.accept(this);
        s.s2.accept(this);
        return null;
    }


    public Type visit(While s) {
        Type expType = s.e.accept(this);
        if (expType != null && !(expType instanceof BooleanType))
            error.complain("Condition in While statement must be of type boolean");

        s.s.accept(this);
        return null;
    }


    public Type visit(Print s) {
        Type type = s.e.accept(this);
        if (type != null && !(type instanceof IntegerType))
            error.complain("Expression in Print statement must be of type int");
        return null;
    }


    public Type visit(Assign s) {
        String idName = s.i.s;
        Type idType = getIdentifierType(currClass, idName);
        Type expType = s.e.accept(this);
        if (idType == null || expType == null || idType.toString().equals(expType.toString()))
            return null;

        if (!isDerived(expType, idType))
            error.complain("Assigned expression type does not match variable '" + idName + "' of type " + idType.toString());
        return null;
    }


    public Type visit(ArrayAssign s) {
        String idName = s.i.s;
        Type idType = getIdentifierType(currClass, idName);
        if (idType != null && !(idType instanceof IntArrayType))
            error.complain("Variable '" + idName + "' in ArrayAssign must be of type int[]");

        Type idxType = s.e1.accept(this);
        if (idxType != null && !(idxType instanceof IntegerType))
            error.complain("Index expression in ArrayAssign must be of type int");

        Type expType = s.e2.accept(this);
        if (expType != null && !(expType instanceof IntegerType))
            error.complain("Assigned expression in ArrayAssign must be of type int");

        return null;
    }


    public Type visit(ClassDeclSimple n) {
        currClass = getClassType(Type.type(n.i.s));
        for (int i = 0; i < n.vl.size(); i++) {
            n.vl.get(i).accept(this);
        }
        for (int i = 0; i < n.vl.size(); i++) {
            n.vl.get(i).accept(this);
        }
        currClass = null;
        return null;
    }

    public Type visit(ClassDeclExtends n) {
        currClass = getClassType(Type.type(n.i.s));
        for (int i = 0; i < n.vl.size(); i++) {
            n.vl.get(i).accept(this);
        }
        for (int i = 0; i < n.vl.size(); i++) {
            n.vl.get(i).accept(this);
        }
        currClass = null;
        return null;
    }

    public Type visit(MainClass n) {
        n.s.accept(this);
        return null;
    }

    public Type visit(VarDecl n) {
        checkIfTypeExists(n.t);
        return null;
    }

    public Type visit(MethodDecl n) {

        currMethod = currClass.methods.get(n.i.s);

        for (int i = 0; i < n.fl.size(); i++) {
            n.fl.get(i).accept(this);
        }
        for (int i = 0; i < n.vl.size(); i++) {
            n.vl.get(i).accept(this);
        }
        for (int i = 0; i < n.sl.size(); i++) {
            n.sl.get(i).accept(this);
        }

        Type returnType = n.e.accept(this);
        if (checkIfTypeExists(n.t) && returnType != null &&
                !returnType.toString().equals(n.t.toString()))
            error.complain("Return expression type does not match method return type: expected " 
                            + n.t.toString() + ", but found " + returnType.toString());

        currMethod = null;
        return null;
    }


    public Type visit(Formal n) {
        checkIfTypeExists(n.t);
        return null;
    }

    public Type visit(IntArrayType n) { return null; }
    public Type visit(BooleanType n) { return null; }
    public Type visit(IntegerType n) { return null; }
    public Type visit(IdentifierType n) { return null; }

    public Type visit(Program p) {
        p.m.accept(this);
        for (int i = 0; i < p.cl.size(); i++) {
            p.cl.get(i).accept(this);
        }
        return null;
    }
    public Type visit(Identifier id) {
        return null;
    }
}
