package syntaxtree;

import syntaxtree.visitor.Visitor;

public abstract class Type {
    
    public abstract <T> T accept(Visitor<T> visitor);

    public static Type type(String s) {
        if (s == "int"){
            return new IntegerType();
        } else if (s == "int[]"){
            return new IntArrayType();
        } else if (s == "boolean"){
            return new BooleanType();
        } else {
            return new IdentifierType(s);
        }
    }

    public boolean equals(Type other) {
        return this.toString().equals(other.toString());
    }
}



