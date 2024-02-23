package cool.structures;

import java.util.LinkedHashMap;
import java.util.Map;

public class FunctionSymbol extends IdSymbol implements Scope {

    protected Map<String, IdSymbol> symbols = new LinkedHashMap<>();
    protected Scope parent;
    protected TypeSymbol type;
    protected String returnType;

    public FunctionSymbol(Scope parent, String name) {
        super(name);
        this.parent = parent;
    }

    @Override
    public boolean add(Symbol sym) {
        if (symbols.containsKey(sym.getName()))
            return false;

        symbols.put(sym.getName(), (IdSymbol) sym);

        return true;
    }

    @Override
    public Symbol lookup(String s) {
        var sym = symbols.get(s);

        if (sym != null)
            return sym;

        if (parent != null)
            return parent.lookup(s);

        return null;
    }

    @Override
    public Symbol lookup_once(String str) {
        return symbols.get(str);
    }

    @Override
    public Scope getParent() {
        return parent;
    }

    public Map<String, IdSymbol> getFormals() {
        return symbols;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }
}