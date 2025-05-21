package syntaxtree;

import syntaxtree.visitor.Visitor;

public class Block extends Statement {
    public StatementList sl;
    public Block(StatementList sl) { this.sl = sl; }
    public <T> T accept(Visitor<T> visitor) { return visitor.visit(this); }
}