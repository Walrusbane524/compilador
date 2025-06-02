package syntaxtree;

import java.util.ArrayList;

public class MethodType {
    private String id;
    private Type returnType;
    public ArrayList<String> args = new ArrayList<String>();
    public SymbolTable<String> vars = new SymbolTable<String>();

    public MethodType(String i, Type rt) { id = i; returnType = rt; }

    public String getId() { return id; }
    public Type getReturnType() { return returnType; }

    public boolean addVar(String id, Type t) {
        Symbol s = Symbol.symbol(id);
        if (vars.get(s) != null) return false;

        vars.put(s, t.toString());
        return true;
    }

    public boolean addArg(String id, Type t) {
        args.add(t.toString());
        return addVar(id, t);
    }

    public String toString() {
        String s = "(";
        s += "Args(";
        for (int i = 0; i < args.size(); i++) {
            s += ((i != 0) ? ", " : "") + args.get(i);
        }
        s += "), ";
        s += "Vars" + vars.toString();
        return s + ")";
    }
}
