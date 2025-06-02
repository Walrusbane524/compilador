package syntaxtree;

public class ErrorMsg {
    public boolean anyErrors;
    public Void complain(String msg) {
        anyErrors = true;
        System.out.println(msg);
        return null;
    }
}
