package cool.structures;

import java.util.LinkedHashMap;
import java.util.Map;

public class LetSymbol implements Scope {
    protected Map<String, Symbol> symbols = new LinkedHashMap<>();
    protected Scope parent;

    public LetSymbol(Scope parent) {
        this.parent = parent;
    }

    @Override
    public boolean add(Symbol sym) {
        if (symbols.containsKey(sym.getName()))
            return false;

        symbols.put(sym.getName(), sym);

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
}
