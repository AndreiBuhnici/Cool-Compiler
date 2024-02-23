package cool.structures;

public interface Scope {
    public boolean add(Symbol sym);
    
    public Symbol lookup(String str);

    public Symbol lookup_once(String str);
    
    public Scope getParent();
}
