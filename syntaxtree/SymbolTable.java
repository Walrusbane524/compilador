package syntaxtree;

import java.util.Hashtable;
import java.util.Stack;
import java.util.Map;

class SymbolTable<V> {
    private Hashtable<Symbol, V> table = new Hashtable<Symbol, V>();
    private Stack<StackEntry<V>> stack = new Stack<StackEntry<V>>();

    public void put(Symbol key, V value) {
        Object oldVal = table.get(key);
        stack.push(new StackEntry(key, oldVal));
        table.put(key, value);
    }

    public V get(Symbol key) {
        return table.get(key);
    }

    public V get(String key) {
        return table.get(Symbol.symbol(key));
    }

    public void beginScope() {
        stack.push(new StackEntry());
    }

    public void endScope() {
        while (!stack.peek().marker) {
            StackEntry<V> e = stack.pop();
            table.remove(e.symbol);
            if (e.oldVal != null)
                table.put(e.symbol, e.oldVal);
        }
        stack.pop();
    }

    public java.util.Enumeration keys() {
        return table.keys();
    }

    public String toString() {
        String s = "(";

        boolean first = true;
        for (Map.Entry<Symbol, V> entry : table.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();

            if (!first) s += ", ";
            s += key + " |-> " + value;
            first = false;
        }
        return s + ")";
    }
}