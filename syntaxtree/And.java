package syntaxtree;
import syntaxtree.visitor.*;

public class And extends Exp {
    public Exp e1, e2;
    public And(Exp e1, Exp e2) { this.e1 = e1; this.e2 = e2; }
    public String accept(Visitor<String> visitor){
        return visitor.visit(this);
    }
}
