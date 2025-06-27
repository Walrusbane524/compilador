package Translate;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

import Frame.*;
import Temp.*;
import Tree.*;
import syntaxtree.*;
import syntaxtree.visitor.TypeChecker;
import syntaxtree.visitor.Visitor;
import Symbol.Symbol;

public class IRTreeVisitor implements Visitor<Exp> {
    TypeChecker typeCheck;
    SymbolTable<ClassType> symbolTable;
    ClassType currentClass;
    MethodType currentMethod;
    Frame currentFrame;
    LinkedList<ProcFrag> frags;

    public IRTreeVisitor(Frame frame, TypeChecker typeCheck) {
        this.typeCheck = typeCheck;
        this.symbolTable = typeCheck.tree.classes;
        this.currentFrame = frame;
        this.frags = new LinkedList<>();
    }

    public LinkedList<ProcFrag> getResult() { return frags; }

    @Override
    public Exp visit(Program program) {

        program.m.accept(this);
        program.cl.forEach(classDecl -> classDecl.accept(this));

        return null;

    }

    @Override
    public Exp visit(Identifier identifier) {

        Access a = currentFrame.allocLocal(false);

        return new Exp(a.exp(new TEMP(currentFrame.FP())));
        
    }

    @Override
    public Exp visit(MainClass mainClass) {

        Symbol frameName = Symbol.symbol(mainClass.i1.s + "$main");
        List<Boolean> escapes = List.of(false);
        currentFrame = currentFrame.newFrame(frameName, escapes);

        Stm mainStm = mainClass.s.accept(this).unNx();
        List<Stm> bodyList = new ArrayList<>(List.of(mainStm));
        procEntryExit(bodyList);

        return null;

    }

    @Override
    public Exp visit(ClassDeclSimple classDeclSimple) {

        currentClass = symbolTable.get(Symbol.symbol(classDeclSimple.i.s));

        classDeclSimple.i.accept(this);

        classDeclSimple.vl.forEach(field -> field.accept(this));
        classDeclSimple.ml.forEach(method -> method.accept(this));

        return null;

    }

    @Override
    public Exp visit(ClassDeclExtends classDeclExtends) {

        currentClass = symbolTable.get(Symbol.symbol(classDeclExtends.i.s));

        classDeclExtends.i.accept(this);

        classDeclExtends.vl.forEach(field -> field.accept(this));
        classDeclExtends.ml.forEach(method -> method.accept(this));

        return null;
        
    }

    @Override
    public Exp visit(VarDecl varDecl) {

        varDecl.t.accept(this);
        varDecl.i.accept(this);

        return null;

    }

    @Override
    public Exp visit(MethodDecl methodDecl) {

        currentMethod = currentClass.methods.get(Symbol.symbol(methodDecl.i.s));

        Symbol frameName = Symbol.symbol(currentClass.getId() + "$" + currentMethod.getId());
        List<Boolean> escapeList = Collections.nCopies(currentMethod.args.size() + 1, false);
        currentFrame = currentFrame.newFrame(frameName, escapeList);

        methodDecl.fl.forEach(f -> f.accept(this));
        methodDecl.vl.forEach(v -> v.accept(this));

        Stm firstStm = methodDecl.sl.isEmpty() ? new EXP(new CONST(0)) : methodDecl.sl.getFirst().accept(this).unNx();

        Stm body = methodDecl.sl.stream().map(s -> s.accept(this).unNx()).skip(1).reduce(firstStm, SEQ::new);

        Tree.Exp returnExp = new ESEQ(body, methodDecl.e.accept(this).unEx());
        body = new MOVE(new TEMP(currentFrame.RV()), returnExp);

        ArrayList<Stm> bodyList = new ArrayList<>(List.of(body));
        procEntryExit(bodyList);

        return null;

    }

    public void procEntryExit(List<Stm> body) { frags.add(new ProcFrag(body, currentFrame)); }

    @Override
    public Exp visit(Formal formal) {

        formal.i.accept(this);
        formal.t.accept(this);

        return null;

    }

    @Override
    public Exp visit(And and) {

        return new Exp(new BINOP(BINOP.AND, and.e1.accept(this).unEx(), and.e2.accept(this).unEx()));

    }

    @Override
    public Exp visit(LessThan lessThan) {

        Tree.Exp left = lessThan.e1.accept(this).unEx();
        Tree.Exp right = lessThan.e2.accept(this).unEx();

        Label trueLabel = new Label();
        Label falseLabel = new Label();
        Label end = new Label();

        Temp result = new Temp();

        MEM resultMem = new MEM(new TEMP(result));

        MOVE ifTrue = new MOVE(new TEMP(result), new CONST(1));
        MOVE ifFalse = new MOVE(new TEMP(result), new CONST(0));

        CJUMP cjump = new CJUMP(CJUMP.LT, left, right, trueLabel, falseLabel);

        Tree.Exp lessThanExp = new ESEQ( new SEQ(cjump, new SEQ(new LABEL(trueLabel), new SEQ(ifTrue, new SEQ(new JUMP(end), new SEQ(new LABEL(falseLabel), new SEQ(ifFalse, new LABEL(end))))))), resultMem);

        return new Exp(lessThanExp);

    }

    @Override
    public Exp visit(Plus plus) { return new Exp(new BINOP(BINOP.PLUS, plus.e1.accept(this).unEx(), plus.e2.accept(this).unEx())); }

    @Override
    public Exp visit(Minus minus) { return new Exp(new BINOP(BINOP.MINUS, minus.e1.accept(this).unEx(), minus.e2.accept(this).unEx())); }

    @Override
    public Exp visit(Times times) { return new Exp(new BINOP(BINOP.MUL, times.e1.accept(this).unEx(), times.e2.accept(this).unEx())); }

    @Override
    public Exp visit(ArrayLookup arrayLookup) {

        Exp array = arrayLookup.e1.accept(this);
        Exp i = arrayLookup.e2.accept(this);

        BINOP offset = new BINOP(BINOP.MUL, i.unEx(), new CONST(currentFrame.wordSize()));
        BINOP pointer = new BINOP(BINOP.PLUS, array.unEx(), offset);

        return new Exp(new MEM(pointer));

    }

    @Override
    public Exp visit(ArrayLength arrayLength) { return new Exp(new MEM(arrayLength.e.accept(this).unEx())); }

    @Override
    public Exp visit(Call call) {

        ArrayList<Tree.Exp> argsList = call.el.stream().map(a -> a.accept(this).unEx()).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        argsList.addFirst(call.e.accept(this).unEx());

        typeCheck.currClass = currentClass;
        typeCheck.currMethod = currentMethod;
        Type type = call.e.accept(typeCheck);

        Label label = new Label(type.toString() + "$" + call.i);
        CALL callExp = new CALL(new NAME(label), argsList);

        return new Exp(callExp);

    }

    @Override
    public Exp visit(IntegerLiteral integerLiteral) { return new Exp(new CONST(integerLiteral.i)); }

    @Override
    public Exp visit(True aTrue) { return new Exp(new CONST(1)); }

    @Override
    public Exp visit(False aFalse) { return new Exp(new CONST(0)); }

    @Override
    public Exp visit(IdentifierExp identifierExp) {

        Access a = currentFrame.allocLocal(false);
        return new Exp(a.exp(new TEMP(currentFrame.FP())));

    }

    @Override
    public Exp visit(This aThis) { return new Exp(new MEM(new TEMP(currentFrame.FP()))); }

    @Override
    public Exp visit(NewArray newArray) {

        Exp sizeExp = newArray.size.accept(this);

        BINOP size = new BINOP(BINOP.PLUS, sizeExp.unEx(), new CONST(1));
        BINOP sizeInBytes = new BINOP(BINOP.MUL, size, new CONST(currentFrame.wordSize()));

        List<Tree.Exp> argsList = new ArrayList<>(List.of(sizeInBytes));
        Tree.Exp alloc = currentFrame.externalCall("initArray", argsList);

        return new Exp(alloc);
        
    }

    @Override
    public Exp visit(NewObject newObject) {

        ClassType objInfo = symbolTable.get(Symbol.symbol(newObject.i.toString()));

        int objSize = objInfo.vars.size();
        BINOP objSizeInBytes = new BINOP(BINOP.MUL, new CONST(objSize), new CONST(currentFrame.wordSize()));

        List<Tree.Exp> argsList = new ArrayList<>(List.of(objSizeInBytes));

        Tree.Exp alloc = currentFrame.externalCall("malloc", argsList);

        return new Exp(alloc);

    }

    @Override
    public Exp visit(Not not) {

        return new Exp(new BINOP(BINOP.XOR, new CONST(1), not.e.accept(this).unEx()));

    }

    @Override
    public Exp visit(Block block) {

        Stm stm1 = block.sl.getFirst().accept(this).unNx();
        Stm stmB = block.sl.stream().map(stm -> stm.accept(this).unNx()).skip(1).reduce(stm1, SEQ::new);

        return new Exp(new ESEQ(stmB, new CONST(0)));

    }

    @Override
    public Exp visit(If ifStm) {

        Exp condition = ifStm.e.accept(this);
        Exp s1Exp = ifStm.s1.accept(this);
        Exp s2Exp = ifStm.s2.accept(this);

        Label s1Label = new Label();
        Label s2Label = new Label();
        Label end = new Label();

        CJUMP cjump = new CJUMP(CJUMP.EQ, condition.unEx(), new CONST(1), s1Label, s2Label);
        return new Exp(new ESEQ(new SEQ(cjump, new SEQ(new LABEL(s1Label), new SEQ(s1Exp.unNx(), new SEQ(new JUMP(end), new SEQ(new LABEL(s2Label), new SEQ(s2Exp.unNx(), new LABEL(end))))))), new CONST(0)));

    }

    @Override
    public Exp visit(While whileStm) {

        Label test = new Label();
        Label begin = new Label();
        Label end = new Label();
        Exp condition = whileStm.e.accept(this);
        Exp body = whileStm.s.accept(this);
        CJUMP cjump = new CJUMP(CJUMP.EQ, condition.unEx(), new CONST(1), begin, end);
        SEQ loop = new SEQ(new LABEL(test), new SEQ(cjump, new SEQ(new LABEL(begin), new SEQ(body.unNx(), new SEQ(new JUMP(test), new LABEL(end))))));
        return new Exp(new ESEQ(loop, new CONST(0)));

    }

    @Override
    public Exp visit(syntaxtree.Print print) {

        Exp exp = print.e.accept(this);
        ArrayList<Tree.Exp> argsList = new ArrayList<>(List.of(exp.unEx()));
        Tree.Exp syscall = currentFrame.externalCall("print", argsList);
        Stm printStm = new EXP(syscall);
        return new Exp(new ESEQ(printStm, new CONST(0)));

    }

    @Override
    public Exp visit(Assign assign) {

        Tree.Exp varName = assign.i.accept(this).unEx();
        Tree.Exp varValue = assign.e.accept(this).unEx();
        MOVE assignStm = new MOVE(new MEM(varName), varValue);
        return new Exp(new ESEQ(assignStm, new CONST(0)));

    }

    @Override
    public Exp visit(ArrayAssign arrayAssign) {

        Exp arrayExp = arrayAssign.i.accept(this);
        Exp indexExp = arrayAssign.e1.accept(this);
        Exp valueExp = arrayAssign.e2.accept(this);
        BINOP offset = new BINOP(BINOP.MUL, indexExp.unEx(), new CONST(currentFrame.wordSize()));
        BINOP pointerToElement = new BINOP(BINOP.PLUS, arrayExp.unEx(), offset);
        MOVE arrayAssignStm = new MOVE(new MEM(pointerToElement), valueExp.unEx());
        return new Exp(new ESEQ(arrayAssignStm, new CONST(0)));

    }

    @Override
    public Exp visit(BooleanType booleanType) { return null; }

    @Override
    public Exp visit(IntegerType integerType) { return null; }

    @Override
    public Exp visit(IdentifierType identifierType) { return null; }

    @Override
    public Exp visit(IntArrayType intArrayType) { return null; }
}
