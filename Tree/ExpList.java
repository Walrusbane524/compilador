package Tree;
import java.util.List;
public class ExpList {
  public Exp head;
  public ExpList tail;
  public ExpList(Exp h, ExpList t) {head=h; tail=t;}
  public ExpList(List<Exp> exprs) {
    head = exprs.isEmpty() ? null : exprs.getFirst();
    tail = exprs.size() < 2 ? null : new ExpList(exprs.get(1), null);
    if (exprs.size() < 2) return;
    tail = new ExpList(exprs.getLast(), null);
    exprs.stream().skip(2).map(expr -> new ExpList(expr, null)).reduce(tail, (acc, expList) -> { acc.tail = expList; return expList; });
  }
}



