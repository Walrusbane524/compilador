package syntaxtree;

class ClassType{
    private String id;
    private String superClass;
    public SymbolTable<String> vars = new SymbolTable<String>();
    public SymbolTable<MethodType> methods = new SymbolTable<MethodType>();

    ClassType(String i) { id = i; superClass = null; }

    ClassType(String i, String sc) { id = i; superClass = sc; }

    public String getId() { return id; }
    public String getSuperClass() { return superClass; }

    public boolean addVar(String id, Type t) {
        Symbol s = Symbol.symbol(id);
        if (vars.get(s) != null) return false;

        vars.put(s, t.toString());
        return true;
    }

    public boolean addMethod(String id, MethodType t) {
        Symbol s = Symbol.symbol(id);
        if (methods.get(s) != null) return false;

        methods.put(s, t);
        return true;
    }

    public String toString() {
        String s = "(";
        s += "Fields" + vars.toString();
        s += ", Methods" + methods.toString();
        return s + ")";
    }
}