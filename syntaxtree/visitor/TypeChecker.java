package syntaxtree.visitor;

import syntaxtree.*;

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
        System.out.println("is derived call");
        System.out.println("t1: " + t1);
        System.out.println("t2: " + t2);
        System.out.println("equals: " + t1.equals(t2));
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
        System.out.println("And typecheck");
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
        System.out.println("LessThan typecheck");
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
        System.out.println("Plus typecheck");
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
        System.out.println("Minus typecheck");
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
        System.out.println("Times typecheck");
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
        System.out.println("ArrayLookup typecheck");
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


    public Type visit(Call call) {
        System.out.println("Method Call typecheck");
        Type objType = call.e.accept(this);
        if (isPrimitive(objType)) {
            error.complain("Object of method call must be a reference type, cannot call " + call.i.s + "()");
            return null;
        }

        ClassType classType = getClassType(objType);
        if (classType == null) {
            error.complain("Object of method call must have a defined class type");
            return null;
        }

        MethodType methodType = getMethod(classType, call.i.s);
        if (methodType == null)
            error.complain("Method " + call.i.s + " not found in class " + classType.id);

        if (methodType != null && methodType.args.size() != call.el.size())
            error.complain("Method " + call.i.s + " called with wrong number of arguments");

        if (methodType != null) {
            for (int i = 0; i < methodType.args.size(); i++) {
                Type paramType = call.el.get(i).accept(this);
                System.out.println("paramtype: " + paramType);
                Type expectedType = Type.type(methodType.args.get(i));
                System.out.println("expectedtype: " + expectedType);

                if (paramType == null) continue;
                if (!isDerived(paramType, expectedType)) {
                    error.complain("Incorrect parameter type at position " + (i + 1) + " in call to method " + call.i.s);
                }
            }
            return methodType.getReturnType();
        }

        return null;
    }


    public Type visit(ArrayLength n) {
        System.out.println("ArrayLength typecheck");
        if (!(n.e.accept(this) instanceof IntArrayType))
            error.complain("Expression in ArrayLength must be of type int[]");
        return new IntegerType();
    }


    public Type visit(Not n) {
        System.out.println("Not typecheck");
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
        System.out.println("If typecheck");
        Type expType = s.e.accept(this);
        if (expType != null && !(expType instanceof BooleanType))
            error.complain("Condition in If statement must be of type boolean");

        s.s1.accept(this);
        s.s2.accept(this);
        return null;
    }


    public Type visit(While s) {
        System.out.println("While typecheck");
        Type expType = s.e.accept(this);
        if (expType != null && !(expType instanceof BooleanType))
            error.complain("Condition in While statement must be of type boolean");

        s.s.accept(this);
        return null;
    }


    public Type visit(Print s) {
        System.out.println("Print typecheck");
        Type type = s.e.accept(this);
        if (type != null && !(type instanceof IntegerType))
            error.complain("Expression in Print statement must be of type int");
        return null;
    }


    public Type visit(Assign s) {
        System.out.println("Assign typecheck");
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
        System.out.println("ArrayAssign typecheck");
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
        System.out.println("ClassDeclSimple typecheck");
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
        System.out.println("ClassDeclExtends typecheck");
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
        System.out.println("Method typecheck");
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
        System.out.println("Formal typecheck");
        checkIfTypeExists(n.t);
        return null;
    }

    public Type visit(IntArrayType n) { return null; }
    public Type visit(BooleanType n) { return null; }
    public Type visit(IntegerType n) { return null; }
    public Type visit(IdentifierType n) { return null; }

    public Type visit(Program p) {
        System.out.println("Program typecheck");
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
