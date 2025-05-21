package syntaxtree;

import syntaxtree.visitor.Visitor;

public class Identifier {
    public String s;

    public Identifier(String s) {
        this.s = s;
    }

    public <T> T accept(Visitor<T> visitor) { return visitor.visit(this); }

    @Override
    public String toString(){
        return s;
    }
}
