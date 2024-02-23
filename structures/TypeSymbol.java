package cool.structures;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TypeSymbol extends Symbol implements Scope {
    public Map<String, IdSymbol> symbols = new LinkedHashMap<>();
    protected TypeSymbol parent;
    protected String parentName;

    public int tag;
    public TypeSymbol(String name, TypeSymbol parent, String parentName) {
        super(name);
        this.parent = parent;
        this.parentName = parentName;

        var self = new IdSymbol("self");
        self.setTypeName("SELF_TYPE");
        add(self);
    }

    public static final TypeSymbol OBJECT = new TypeSymbol("Object", null, null);
    public static final TypeSymbol INT   = new TypeSymbol("Int", OBJECT, "Object");
    public static final TypeSymbol BOOL  = new TypeSymbol("Bool", OBJECT, "Object");
    public static final TypeSymbol STRING = new TypeSymbol("String", OBJECT, "Object");
    public static final TypeSymbol IO = new TypeSymbol("IO", OBJECT, "Object");
    public static final TypeSymbol SELF_TYPE = new TypeSymbol("SELF_TYPE", OBJECT, "Object");

    @Override
    public boolean add(Symbol sym) {
        if (symbols.containsKey(sym.getName()) && (sym.getClass() == symbols.get(sym.getName()).getClass())) {
            return false;
        }
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
    public TypeSymbol getParent() {
        return parent;
    }

    public void setParent(TypeSymbol parent) {
        this.parent = parent;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
}
