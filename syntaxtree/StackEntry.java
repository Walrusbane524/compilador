package syntaxtree;

import Symbol.Symbol;

class StackEntry<V> {
    Symbol symbol;
    V oldVal;
    boolean marker = false;

    StackEntry(Symbol s, V o) { symbol = s; oldVal = o; }

    StackEntry() { marker = true; }
}
