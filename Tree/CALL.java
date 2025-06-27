package Tree;
import Temp.Temp;
import java.util.List;
import Temp.Label;
public class CALL extends Exp {
  public Exp func;
  public ExpList args;
  public CALL(Exp f, ExpList a) {func=f; args=a;}
  public ExpList kids() {return new ExpList(func,args);}
  public Exp build(ExpList kids) {
    return new CALL(kids.head,kids.tail);
  }
  public CALL(Tree.Exp f, List<Tree.Exp> a) {
    if (a == null || a.isEmpty()) return;
    func = f;
    args = new ExpList(a);
  }
  
}

