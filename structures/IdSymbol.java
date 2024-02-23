package cool.structures;

public class IdSymbol extends Symbol {
    protected String typeName;
    public int offset;

    public IdSymbol(String name) {
        super(name);
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }
}