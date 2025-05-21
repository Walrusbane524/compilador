package syntaxtree.visitor;

import syntaxtree.*;
import java.util.Arrays;
public class PrettyPrintVisitor implements Visitor<String> {
    private int indent = 0;

    private String indent() {
        char[] chars = new char[indent * 2];
        Arrays.fill(chars, ' ');
        return new String(chars);
    }

    @Override
    public String visit(Program n) {
        StringBuilder sb = new StringBuilder();
        sb.append(n.m.accept(this));
        for (ClassDecl cd : n.cl) {
            sb.append(cd.accept(this));
        }
        return sb.toString();
    }

    @Override
    public String visit(MainClass n) {
        StringBuilder sb = new StringBuilder();
        sb.append("class ").append(n.i1.s).append(" {\n");
        indent++;
        sb.append(indent()).append("public static void main(String[] ").append(n.i2.s).append(") {\n");
        indent++;
        sb.append(indent()).append(n.s.accept(this)).append("\n");
        indent--;
        sb.append(indent()).append("}\n");
        indent--;
        sb.append("}\n");
        return sb.toString();
    }

    @Override
    public String visit(ClassDeclSimple n) {
        StringBuilder sb = new StringBuilder();
        sb.append("class ").append(n.i.s).append(" {\n");
        indent++;
        for (VarDecl v : n.vl) sb.append(indent()).append(v.accept(this)).append("\n");
        for (MethodDecl m : n.ml) sb.append(indent()).append(m.accept(this)).append("\n");
        indent--;
        sb.append("}\n");
        return sb.toString();
    }

    @Override
    public String visit(ClassDeclExtends n) {
        StringBuilder sb = new StringBuilder();
        sb.append("class ").append(n.i.s)
          .append(" extends ").append(n.j.s).append(" {\n");
        indent++;
        for (VarDecl v : n.vl) sb.append(indent()).append(v.accept(this)).append("\n");
        for (MethodDecl m : n.ml) sb.append(indent()).append(m.accept(this)).append("\n");
        indent--;
        sb.append("}\n");
        return sb.toString();
    }

    @Override
    public String visit(VarDecl n) {
        return n.t.accept(this) + " " + n.i.s + ";";
    }

    @Override
    public String visit(MethodDecl n) {
        StringBuilder sb = new StringBuilder();
        sb.append("public ").append(n.t.accept(this)).append(" ")
          .append(n.i.s).append("(");
        for (int i = 0; i < n.fl.size(); i++) {
            sb.append(n.fl.get(i).accept(this));
            if (i < n.fl.size()-1) sb.append(", ");
        }
        sb.append(") {\n");
        indent++;
        for (VarDecl v : n.vl) sb.append(indent()).append(v.accept(this)).append("\n");
        for (Statement s : n.sl) sb.append(indent()).append(s.accept(this)).append("\n");
        sb.append(indent()).append("return ").append(n.e.accept(this)).append(";");
        sb.append("\n");
        indent--;
        sb.append(indent()).append("}");
        return sb.toString();
    }

    @Override
    public String visit(Formal n) {
        return n.t.accept(this) + " " + n.i.s;
    }

    @Override
    public String visit(IntArrayType n) {
        return "int[]";
    }

    @Override
    public String visit(BooleanType n) {
        return "boolean";
    }

    @Override
    public String visit(IntegerType n) {
        return "int";
    }

    @Override
    public String visit(IdentifierType n) {
        return n.s;
    }

    @Override
    public String visit(Block n) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        indent++;
        for (Statement s : n.sl) sb.append(indent()).append(s.accept(this)).append("\n");
        indent--;
        sb.append(indent()).append("}");
        return sb.toString();
    }

    @Override
    public String visit(If n) {
        StringBuilder sb = new StringBuilder();
        sb.append("if (").append(n.e.accept(this)).append(") ")
          .append(n.s1.accept(this))
          .append(" else ")
          .append(n.s2.accept(this));
        return sb.toString();
    }

    @Override
    public String visit(While n) {
        return "while (" + n.e.accept(this) + ") " + n.s.accept(this);
    }

    @Override
    public String visit(Print n) {
        return "System.out.println(" + n.e.accept(this) + ");";
    }

    @Override
    public String visit(Assign n) {
        return n.i.s + " = " + n.e.accept(this) + ";";
    }

    @Override
    public String visit(ArrayAssign n) {
        return n.i.s + "[" + n.e1.accept(this) + "] = " + n.e2.accept(this) + ";";
    }

    @Override
    public String visit(And n) {
        return "(" + n.e1.accept(this) + " && " + n.e2.accept(this) + ")";
    }

    @Override
    public String visit(LessThan n) {
        return "(" + n.e1.accept(this) + " < " + n.e2.accept(this) + ")";
    }

    @Override
    public String visit(Plus n) {
        return "(" + n.e1.accept(this) + " + " + n.e2.accept(this) + ")";
    }

    @Override
    public String visit(Minus n) {
        return "(" + n.e1.accept(this) + " - " + n.e2.accept(this) + ")";
    }

    @Override
    public String visit(Times n) {
        return "(" + n.e1.accept(this) + " * " + n.e2.accept(this) + ")";
    }

    @Override
    public String visit(ArrayLookup n) {
        return n.e1.accept(this) + "[" + n.e2.accept(this) + "]";
    }

    @Override
    public String visit(ArrayLength n) {
        return n.e.accept(this) + ".length";
    }

    @Override
    public String visit(Call n) {
        StringBuilder sb = new StringBuilder();
        sb.append(n.e.accept(this)).append(".").append(n.i.s).append("(");
        for (int i = 0; i < n.el.size(); i++) {
            sb.append(n.el.get(i).accept(this));
            if (i < n.el.size()-1) sb.append(", ");
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String visit(IntegerLiteral n) {
        return Integer.toString(n.i);
    }

    @Override
    public String visit(True n) {
        return "true";
    }

    @Override
    public String visit(False n) {
        return "false";
    }

    @Override
    public String visit(IdentifierExp n) {
        return n.s;
    }

    @Override
    public String visit(This n) {
        return "this";
    }

    @Override
    public String visit(NewArray n) {
        return "new int[" + n.size.accept(this) + "]";
    }

    @Override
    public String visit(NewObject n) {
        return "new " + n.i.s + "()";
    }

    @Override
    public String visit(Not n) {
        return "!" + n.e.accept(this);
    }

    @Override
    public String visit(Identifier n) {
        return n.s;
    }
}
