package syntaxtree;
import syntaxtree.visitor.*;

public class True extends Exp {
    public String accept(Visitor<String> visitor){
        return visitor.visit(this);
    }
}
