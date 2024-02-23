package cool.compiler;

import cool.structures.*;

public class DefinitionPassVisitor implements ASTVisitor<Void> {
    Scope currentScope = SymbolTable.globals;

    @Override
    public Void visit(Program program) {
        for (var classs : program.classes) {
            classs.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(Class classs) {
        var name = classs.childType.getText();
        if (name.equals("SELF_TYPE")) {
            SymbolTable.error(classs.ctx, classs.childType, "Class has illegal name SELF_TYPE");
            return null;
        }

        var parent = classs.parentType == null ? TypeSymbol.OBJECT : (TypeSymbol) currentScope.lookup(classs.parentType.getText());
        var symbol = new TypeSymbol(name, parent, classs.parentType == null ? "Object" : classs.parentType.getText());
        if (!currentScope.add(symbol)) {
            SymbolTable.error(classs.ctx, classs.childType, "Class " + name + " is redefined");
            return null;
        }

        if (classs.parentType != null ) {
            if (classs.parentType.getText().equals("Int") || classs.parentType.getText().equals("Bool")
                    || classs.parentType.getText().equals("String") || classs.parentType.getText().equals("SELF_TYPE")) {
                SymbolTable.error(classs.ctx, classs.parentType,
                        "Class " + name + " has illegal parent " + classs.parentType.getText());
                return null;
            }
        }

        currentScope = symbol;

        classs.type = symbol;
        for (var feature : classs.features) {
            feature.accept(this);
        }

        currentScope = SymbolTable.globals;

        return null;
    }

    @Override
    public Void visit(MethodDefinition methodDefinition) {
        var name = methodDefinition.id.getText();
        var symbol = new FunctionSymbol(currentScope, name);

        if (!currentScope.add(symbol)) {
            SymbolTable.error(methodDefinition.ctx, methodDefinition.id,
                    "Class " + ((TypeSymbol)currentScope).getName() + " redefines method " + name);
            return null;
        }

        symbol.setReturnType(methodDefinition.type.getText());

        currentScope = symbol;

        methodDefinition.symbol = symbol;
        for (var formal : methodDefinition.formals) {
            formal.accept(this);
        }
        methodDefinition.body.accept(this);

        currentScope = currentScope.getParent();

        return null;
    }

    @Override
    public Void visit(Formal formal) {
        var name = formal.id.getText();
        var type = formal.type.getText();
        var symbol = new IdSymbol(name);

        if (name.equals("self")) {
            SymbolTable.error(formal.ctx, formal.id,
                    "Method " + ((FunctionSymbol)currentScope).getName() + " of class "
                            + ((TypeSymbol)currentScope.getParent()).getName()
                            + " has formal parameter with illegal name self");
            return null;
        }

        if (type.equals("SELF_TYPE")) {
            SymbolTable.error(formal.ctx, formal.type,
                    "Method " + ((FunctionSymbol)currentScope).getName() + " of class "
                            + ((TypeSymbol)currentScope.getParent()).getName()
                            + " has formal parameter " + name + " with illegal type SELF_TYPE");
            return null;
        }

        if (!currentScope.add(symbol)) {
            SymbolTable.error(formal.ctx, formal.id,
                    "Method " + ((FunctionSymbol)currentScope).getName() + " of class "
                            + ((TypeSymbol)currentScope.getParent()).getName()
                            + " redefines formal parameter " + name);
            return null;
        }

        symbol.setTypeName(type);

        formal.symbol = symbol;

        return null;
    }

    @Override
    public Void visit(AttributeDefinition attributeDefinition) {
        var name = attributeDefinition.id.getText();
        var symbol = new IdSymbol(name);

        if (name.equals("self")) {
            SymbolTable.error(attributeDefinition.ctx, attributeDefinition.id,
                    "Class " + ((TypeSymbol)currentScope).getName() + " has attribute with illegal name self");
            return null;
        }

        if (!currentScope.add(symbol)) {
            SymbolTable.error(attributeDefinition.ctx, attributeDefinition.id,
                    "Class " + ((TypeSymbol)currentScope).getName() + " redefines attribute " + name);
            return null;
        }

        attributeDefinition.symbol = symbol;
        symbol.setTypeName(attributeDefinition.type.getText());
        if (attributeDefinition.body != null)
            attributeDefinition.body.accept(this);

        return null;
    }

    @Override
    public Void visit(Assign assign) {
        assign.val.accept(this);
        return null;
    }

    @Override
    public Void visit(ExplicitDispatch explicitDispatch) {
        return null;
    }

    @Override
    public Void visit(ImplicitDispatch implicitDispatch) {
        return null;
    }

    @Override
    public Void visit(If iff) {
        iff.cond.accept(this);
        iff.thenBranch.accept(this);
        iff.elseBranch.accept(this);
        return null;
    }

    @Override
    public Void visit(While whilee) {
        whilee.cond.accept(this);
        whilee.body.accept(this);
        return null;
    }

    @Override
    public Void visit(Block block) {
        for (var line : block.lines) {
            line.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(Let let) {
        var symbol = new LetSymbol(currentScope);

        let.symbol = symbol;

        currentScope = symbol;

        for (var varDef : let.vars) {
            varDef.accept(this);
        }

        let.body.accept(this);

        currentScope = currentScope.getParent();

        return null;
    }

    @Override
    public Void visit(VarDef varDef) {
        var name = varDef.id.getText();
        var symbol = new IdSymbol(name);

        if (name.equals("self")) {
            SymbolTable.error(varDef.ctx, varDef.id,
                    "Let variable has illegal name self");
            return null;
        }

        varDef.symbol = symbol;

        if (varDef.body != null)
            varDef.body.accept(this);

        return null;
    }

    @Override
    public Void visit(Case casee) {
        casee.cond.accept(this);
        for (var branch : casee.branches) {
            branch.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(Branch branch) {
        var name = branch.id.getText();
        var type = branch.type.getText();

        if (name.equals("self")) {
            SymbolTable.error(branch.ctx, branch.id,
                    "Case variable has illegal name self");
            return null;
        }

        if (type.equals("SELF_TYPE")) {
            SymbolTable.error(branch.ctx, branch.type,
                    "Case variable " + name + " has illegal type SELF_TYPE");
            return null;
        }

        branch.body.accept(this);

        return null;
    }

    @Override
    public Void visit(NewType newType) {
        return null;
    }

    @Override
    public Void visit(IsVoid isVoid) {
        isVoid.body.accept(this);
        return null;
    }

    @Override
    public Void visit(PlusMinus plusMinus) {
        plusMinus.left.accept(this);
        plusMinus.right.accept(this);
        return null;
    }

    @Override
    public Void visit(MulDiv mulDiv) {
        mulDiv.left.accept(this);
        mulDiv.right.accept(this);
        return null;
    }

    @Override
    public Void visit(Neg neg) {
        neg.body.accept(this);
        return null;
    }

    @Override
    public Void visit(LessThan lt) {
        lt.left.accept(this);
        lt.right.accept(this);
        return null;
    }

    @Override
    public Void visit(LessThanOrEqual lte) {
        lte.left.accept(this);
        lte.right.accept(this);
        return null;
    }

    @Override
    public Void visit(Equal equal) {
        equal.left.accept(this);
        equal.right.accept(this);
        return null;
    }

    @Override
    public Void visit(Not not) {
        not.body.accept(this);
        return null;
    }

    @Override
    public Void visit(Paren paren) {
        paren.body.accept(this);
        return null;
    }

    @Override
    public Void visit(Id id) {
        return null;
    }

    @Override
    public Void visit(Int intt) {
        return null;
    }

    @Override
    public Void visit(Str str) {
        return null;
    }

    @Override
    public Void visit(Bool bool) {
        return null;
    }
}
