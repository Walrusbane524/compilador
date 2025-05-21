package syntaxtree;

class TypeTree{
    public SymbolTable<ClassType> classes = new SymbolTable<ClassType>();

    public boolean addClass(String id, ClassType t) {
        Symbol s = Symbol.symbol(id);
        if (classes.get(s) != null) return false;

        classes.put(s, t);
        return true;
    }
}
