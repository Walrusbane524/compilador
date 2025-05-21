package syntaxtree;

import syntaxtree.visitor.Visitor;

public class Block extends Statement {
    public StatementList sl;
    public Block(StatementList sl) { this.sl = sl; }
    public String accept(Visitor<String> visitor){
        return visitor.visit(this);
    }
}