package syntaxtree;

import java.util.Dictionary;
import java.util.Hashtable;

class Symbol {
    private String name;
    private Symbol(String n) { name=n; }
    private static Dictionary<String, Object> dict = new Hashtable<String, Object>();

    public String toString() { return name; }

    public static Symbol symbol(String n) {
        String u = n.intern();
        Symbol s = (Symbol)dict.get(u);
        if (s==null) { s = new Symbol(u); dict.put(u,s); }
        return s;
    }
}
