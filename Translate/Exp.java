package Translate;

public class Exp {
    Tree.Exp exp;
    public Exp(Tree.Exp exp) { this.exp = exp; }
    Tree.Exp unEx() { return exp; }

    public Tree.Stm unNx() { return ((Tree.ESEQ) exp).stm; }
}