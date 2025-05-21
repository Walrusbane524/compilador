package syntaxtree;
import syntaxtree.visitor.*;

public class True extends Exp {
    public <T> T accept(Visitor<T> visitor) { return visitor.visit(this); }
}
