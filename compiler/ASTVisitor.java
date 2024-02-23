package cool.compiler;

public interface ASTVisitor<T> {
    T visit(Program program);
    T visit(Class classs);
    T visit(MethodDefinition methodDefinition);
    T visit(Formal formal);
    T visit(AttributeDefinition attributeDefinition);
    T visit(Assign assign);
    T visit(ExplicitDispatch explicitDispatch);
    T visit(ImplicitDispatch implicitDispatch);
    T visit(If iff);
    T visit(While whilee);
    T visit(Block block);
    T visit(Let let);
    T visit(VarDef varDef);
    T visit(Case casee);
    T visit(Branch branch);
    T visit(NewType newType);
    T visit(IsVoid isVoid);
    T visit(PlusMinus plusMinus);
    T visit(MulDiv mulDiv);
    T visit(Neg neg);
    T visit(LessThan lt);
    T visit(LessThanOrEqual lte);
    T visit(Equal equal);
    T visit(Not not);
    T visit(Paren paren);
    T visit(Id id);
    T visit(Int intt);
    T visit(Str str);
    T visit(Bool bool);
}
