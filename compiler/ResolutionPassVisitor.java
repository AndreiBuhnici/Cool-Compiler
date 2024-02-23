package cool.compiler;

import cool.parser.CoolParser;
import cool.structures.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResolutionPassVisitor implements ASTVisitor<TypeSymbol> {
    Scope currentScope = SymbolTable.globals;

    private boolean isSubtype(TypeSymbol child, TypeSymbol parent) {
        if (child.getName().equals("SELF_TYPE") && parent.getName().equals("SELF_TYPE"))
            return true;

        if (child.getName().equals("SELF_TYPE")) {
            var scope = currentScope.getParent();
            TypeSymbol typeSymbol;
            while (!(scope instanceof TypeSymbol)) {
                scope = scope.getParent();
                typeSymbol = (TypeSymbol) scope;
                if (typeSymbol == parent)
                    return true;
            }

            typeSymbol = (TypeSymbol) scope;
            if (typeSymbol == parent)
                return true;
        }

        while (child != null) {
            if (child == parent)
                return true;
            child = child.getParent();
        }

        return false;
    }

    private TypeSymbol leastCommonAncestor(TypeSymbol result, TypeSymbol branchType) {
        if (result == branchType)
            return result;

        if (isSubtype(result, branchType))
            return branchType;

        if (isSubtype(branchType, result))
            return result;

        return leastCommonAncestor(result.getParent(), branchType.getParent());
    }

    @Override
    public TypeSymbol visit(Program program) {
        for (var classs : program.classes) {
            classs.accept(this);
        }
        return null;
    }

    @Override
    public TypeSymbol visit(Class classs) {
        var symbol = classs.type;

        if (symbol != null) {
            while (symbol != TypeSymbol.OBJECT) {
                var parentType = (TypeSymbol) currentScope.lookup(symbol.getParentName());
                if (parentType == null) {
                    SymbolTable.error(classs.ctx, classs.parentType, "Class " + classs.childType.getText() +
                            " has undefined parent " + symbol.getParentName());
                    return null;
                }

                symbol.setParent(parentType);
                symbol = parentType;

                if (symbol == classs.type) {
                    SymbolTable.error(classs.ctx, classs.childType, "Inheritance cycle for class "
                            + classs.childType.getText());
                    return null;
                }
            }

            currentScope = classs.type;
            for (var feature : classs.features) {
                feature.accept(this);
            }
            currentScope = SymbolTable.globals;

            var funcCount = 0;
            var parent = classs.type.getParent();

            List<String> funcs = new ArrayList<>();

            while (parent != null) {
                for (var func : parent.symbols.values()) {
                    if (func instanceof FunctionSymbol && !funcs.contains(func.getName())) {
                        funcCount++;
                        funcs.add(func.getName());
                    }
                }
                parent = parent.getParent();
            }

            funcCount = funcCount * 4;

            var attrCount = 12;
            parent = classs.type.getParent();
            while (parent != null) {
                for (var attr : parent.symbols.values()) {
                    if (!(attr instanceof FunctionSymbol) && !attr.getName().equals("self"))
                        attrCount += 4;
                }
                parent = parent.getParent();
            }

            for (var feature : classs.features) {
                if (feature instanceof MethodDefinition) {
                    var symbolFunc = ((MethodDefinition) feature).symbol;
                    if (classs.type.getParent().lookup(symbolFunc.getName()) == null) {
                        symbolFunc.offset = funcCount;
                        funcCount += 4;
                    } else {
                        symbolFunc.offset = ((FunctionSymbol) classs.type.getParent().lookup(symbolFunc.getName())).offset;
                    }
                } else {
                    var symbolAttr = ((AttributeDefinition) feature).symbol;
                    symbolAttr.offset = attrCount;
                    attrCount += 4;
                }
            }
        }

        return null;
    }

    @Override
    public TypeSymbol visit(MethodDefinition methodDefinition) {
        var symbol = methodDefinition.symbol;

        if (symbol != null) {
            var name = methodDefinition.id.getText();
            var returnType = methodDefinition.type.getText();
            var returnSymbol = (TypeSymbol) SymbolTable.globals.lookup(returnType);

            if (returnSymbol == null) {
                SymbolTable.error(methodDefinition.ctx, methodDefinition.type, "Class "
                        + ((TypeSymbol)currentScope).getName() + " has method "
                        + name + " with undefined return type " + returnType);
                return null;
            }

            var inheritedMethod = currentScope.getParent().lookup(name) instanceof FunctionSymbol ?
                    (FunctionSymbol) currentScope.getParent().lookup(name) : null;
            if (inheritedMethod != null) {
                var formals = new ArrayList<>(symbol.getFormals().values());
                var formalsInherited = new ArrayList<>(inheritedMethod.getFormals().values());
                if (formals.size() != formalsInherited.size()) {
                    SymbolTable.error(methodDefinition.ctx, methodDefinition.id, "Class "
                            + ((TypeSymbol)currentScope).getName() + " overrides method "
                            + name + " with different number of formal parameters");
                    return null;
                }

                for (int i = 0; i < formals.size(); i++) {
                    var formalType = formals.get(i).getTypeName();
                    var formalInheritedType = formalsInherited.get(i).getTypeName();
                    var formalSymbol = (TypeSymbol) SymbolTable.globals.lookup(formalType);
                    var formalSymbolInherited = (TypeSymbol) SymbolTable.globals.lookup(formalInheritedType);

                    if (!isSubtype(formalSymbol, formalSymbolInherited)) {
                        SymbolTable.error(methodDefinition.ctx,
                                ((CoolParser.MethodDefinionContext)methodDefinition.ctx).formals.get(i).stop,
                                "Class "
                                + ((TypeSymbol)currentScope).getName() + " overrides method "
                                + name + " but changes type of formal parameter "
                                + formals.get(i).getName() + " from "
                                + formalInheritedType + " to " + formalType);
                        return null;
                    }
                }

                var returnTypeInherited = inheritedMethod.getReturnType();
                var returnSymbolInherited = (TypeSymbol) SymbolTable.globals.lookup(returnTypeInherited);

                if (returnSymbolInherited != returnSymbol) {
                    SymbolTable.error(methodDefinition.ctx, methodDefinition.type, "Class "
                            + ((TypeSymbol)currentScope).getName() + " overrides method "
                            + name + " but changes return type from "
                            + returnTypeInherited + " to " + returnType);
                    return null;
                }
            }

            currentScope = symbol;

            methodDefinition.symbol = symbol;
            for (var formal : methodDefinition.formals) {
                formal.accept(this);
            }

            var body = methodDefinition.body;
            var bodyType = methodDefinition.body.accept(this);

            int formalsSize = 12;
            for (var formal : methodDefinition.formals) {
                formal.symbol.offset = formalsSize;
                formalsSize += 4;
            }

            if (bodyType != null && !isSubtype(bodyType, returnSymbol)) {
                SymbolTable.error(methodDefinition.ctx, body.ctx.start, "Type " + bodyType.getName()
                        + " of the body of method " + name + " is incompatible with declared return type " + returnType);
                return null;
            }

            currentScope = currentScope.getParent();
        }

        return null;
    }

    @Override
    public TypeSymbol visit(Formal formal) {
        var symbol = formal.symbol;

        if (symbol != null) {
            var name = formal.id.getText();
            var type = formal.type.getText();
            var typeSymbol = (TypeSymbol) SymbolTable.globals.lookup(type);

            if (typeSymbol == null) {
                SymbolTable.error(formal.ctx, formal.type, "Method "
                        + ((FunctionSymbol)currentScope).getName() + " of class "
                        + ((TypeSymbol)currentScope.getParent()).getName()
                        + " has formal parameter " + name + " with undefined type " + type);
                return null;
            }

            symbol.setTypeName(type);
        }

        return null;
    }

    @Override
    public TypeSymbol visit(AttributeDefinition attributeDefinition) {
        var symbol = attributeDefinition.symbol;

        if (symbol != null) {
            var name = attributeDefinition.id.getText();
            var type = attributeDefinition.type.getText();
            var typeSymbol = (TypeSymbol) SymbolTable.globals.lookup(type);
            var parent = currentScope.getParent();

            if (parent.lookup(name) != null) {
                SymbolTable.error(attributeDefinition.ctx, attributeDefinition.id,
                        "Class " + ((TypeSymbol)currentScope).getName() + " redefines inherited attribute " + name);
                return null;
            }

            if (typeSymbol == null) {
                SymbolTable.error(attributeDefinition.ctx, attributeDefinition.type, "Class "
                        + ((TypeSymbol)currentScope).getName() + " has attribute "
                        + name + " with undefined type " + type);
                return null;
            }

            var body = attributeDefinition.body;
            if (body != null) {
                var bodyType = body.accept(this);
                if (bodyType != null && !isSubtype(bodyType, typeSymbol)) {
                    SymbolTable.error(attributeDefinition.ctx, body.ctx.start, "Type " + bodyType.getName()
                            + " of initialization expression of attribute "
                            + name + " is incompatible with declared type " + type);
                    return null;
                }
            }

            symbol.setTypeName(type);
        }

        return null;
    }

    @Override
    public TypeSymbol visit(Assign assign) {
        var name = assign.id.getText();
        if (name.equals("self")) {
            SymbolTable.error(assign.ctx, assign.id, "Cannot assign to self");
            return null;
        }

        var symbol = (IdSymbol) currentScope.lookup(name);
        if (symbol == null) {
            return null;
        }

        var typeName = symbol.getTypeName();
        var typeSymbol = (TypeSymbol) SymbolTable.globals.lookup(typeName);

        var body = assign.val;
        var bodyType = body.accept(this);

        if (typeSymbol == null || bodyType == null)
            return null;

        var exprType = (TypeSymbol) SymbolTable.globals.lookup(bodyType.getName());
        if (!isSubtype(exprType, typeSymbol)) {
            SymbolTable.error(assign.ctx, body.ctx.start, "Type " + bodyType.getName()
                    + " of assigned expression is incompatible with declared type " + typeName
                    + " of identifier " + name);
        }

        return bodyType;
    }

    @Override
    public TypeSymbol visit(ExplicitDispatch explicitDispatch) {
        var caller = explicitDispatch.left;
        var callerType = caller.accept(this);
        var typeSymbol = (TypeSymbol) SymbolTable.globals.lookup(callerType.getName());

        if (typeSymbol.getName().equals("SELF_TYPE")) {
            var scope = currentScope.getParent();
            while (!(scope instanceof TypeSymbol))
                scope = scope.getParent();

            typeSymbol = (TypeSymbol) scope;
        }

        var actualCaller = explicitDispatch.type;
        if (actualCaller != null) {
            var actualCallerType = actualCaller.getText();
            var actualCallerSymbol = (TypeSymbol) SymbolTable.globals.lookup(actualCallerType);

            if (actualCallerType.equals("SELF_TYPE")) {
                SymbolTable.error(explicitDispatch.ctx, explicitDispatch.type, "Type of static dispatch " +
                        "cannot be SELF_TYPE");
                return TypeSymbol.OBJECT;
            }

            if (actualCallerSymbol == null) {
                SymbolTable.error(explicitDispatch.ctx, explicitDispatch.type, "Type " + actualCallerType
                        + " of static dispatch is undefined");
                return TypeSymbol.OBJECT;
            }

            if (!isSubtype(typeSymbol, actualCallerSymbol)) {
                SymbolTable.error(explicitDispatch.ctx, explicitDispatch.type, "Type " + actualCallerSymbol.getName()
                        + " of static dispatch is not a superclass of type " + typeSymbol.getName());
                return TypeSymbol.OBJECT;
            }

            typeSymbol = actualCallerSymbol;
        }

        explicitDispatch.symbol = typeSymbol;

        var name = explicitDispatch.id.getText();
        var symbol = (FunctionSymbol) typeSymbol.lookup(name);

        if (symbol == null) {
            SymbolTable.error(explicitDispatch.ctx, explicitDispatch.id, "Undefined method " + name + " in class "
                    + typeSymbol.getName());
            return TypeSymbol.OBJECT;
        }

        List<TypeSymbol> actualTypes = new ArrayList<>();
        for (var expr : explicitDispatch.params) {
            var exprType = expr.accept(this);

            if (exprType != null)
                actualTypes.add(exprType);
        }

        var formals = new ArrayList<>(symbol.getFormals().values());

        if (actualTypes.size() != formals.size()) {
            SymbolTable.error(explicitDispatch.ctx, explicitDispatch.id, "Method " + name + " of class "
                    + typeSymbol.getName() + " is applied to wrong number of arguments");
            return TypeSymbol.OBJECT;
        }

        for (int i = 0; i < actualTypes.size(); i++) {
            var actualType = actualTypes.get(i);
            var formalType = formals.get(i).getTypeName();
            var formalSymbol = (TypeSymbol) SymbolTable.globals.lookup(formalType);

            if (!isSubtype(actualType, formalSymbol)) {
                SymbolTable.error(explicitDispatch.ctx, explicitDispatch.params.get(i).ctx.start, "In call to method "
                        + name + " of class " + typeSymbol.getName() + ", actual type " + actualType.getName()
                        + " of formal parameter " + formals.get(i).getName() + " is incompatible with declared type "
                        + formalType);
            }
        }

        if (Objects.equals(symbol.getReturnType(), TypeSymbol.SELF_TYPE.getName())) {
            return (TypeSymbol) SymbolTable.globals.lookup(callerType.getName());
        }

        return symbol.getReturnType() == null ? TypeSymbol.OBJECT :
                (TypeSymbol) SymbolTable.globals.lookup(symbol.getReturnType());
    }

    @Override
    public TypeSymbol visit(ImplicitDispatch implicitDispatch) {
        var name = implicitDispatch.id.getText();
        Scope scope = currentScope;
        while (!(scope instanceof TypeSymbol)) {
            scope = scope.getParent();
        }

        var typeSymbol = (TypeSymbol) scope;
        var symbol = (FunctionSymbol) typeSymbol.lookup(name);

        if (symbol == null) {
            SymbolTable.error(implicitDispatch.ctx, implicitDispatch.id, "Undefined method " + name + " in class "
                    + typeSymbol.getName());
            return TypeSymbol.OBJECT;
        }

        List<TypeSymbol> actualTypes = new ArrayList<>();
        for (var expr : implicitDispatch.params) {
            var exprType = expr.accept(this);
            if (exprType != null)
                actualTypes.add(exprType);
        }

        var formals = new ArrayList<>(symbol.getFormals().values());

        if (actualTypes.size() != formals.size()) {
            SymbolTable.error(implicitDispatch.ctx, implicitDispatch.id, "Method " + name + " of class "
                    + typeSymbol.getName() + " is applied to wrong number of arguments");
            return TypeSymbol.OBJECT;
        }

        for (int i = 0; i < actualTypes.size(); i++) {
            var actualType = actualTypes.get(i);
            var formalType = formals.get(i).getTypeName();
            var formalSymbol = (TypeSymbol) SymbolTable.globals.lookup(formalType);

            if (!isSubtype(actualType, formalSymbol)) {
                SymbolTable.error(implicitDispatch.ctx, implicitDispatch.params.get(i).ctx.start, "In call to method "
                        + name + " of class " + typeSymbol.getName() + ", actual type " + actualType.getName()
                        + " of formal parameter " + formals.get(i).getName() + " is incompatible with declared type "
                        + formalType);
            }
        }

        if (Objects.equals(symbol.getReturnType(), TypeSymbol.SELF_TYPE.getName()))
            return TypeSymbol.SELF_TYPE;

        return symbol.getReturnType() == null ? TypeSymbol.OBJECT :
                (TypeSymbol) SymbolTable.globals.lookup(symbol.getReturnType());
    }

    @Override
    public TypeSymbol visit(If iff) {
        var condType = iff.cond.accept(this);
        if (condType != TypeSymbol.BOOL) {
            SymbolTable.error(iff.ctx, iff.cond.ctx.start, "If condition has type " + condType.getName()
                    + " instead of Bool");
        }

        return leastCommonAncestor(iff.thenBranch.accept(this), iff.elseBranch.accept(this));
    }

    @Override
    public TypeSymbol visit(While whilee) {
        var condType = whilee.cond.accept(this);
        if (condType != TypeSymbol.BOOL) {
            SymbolTable.error(whilee.ctx, whilee.cond.ctx.start, "While condition has type " + condType.getName()
                    + " instead of Bool");
        }

        return TypeSymbol.OBJECT;
    }

    @Override
    public TypeSymbol visit(Block block) {
        TypeSymbol result = null;
        for (var expr : block.lines) {
            var type = expr.accept(this);
            if (type != null)
                result = type;
        }

        return result;
    }

    @Override
    public TypeSymbol visit(Let let) {
        currentScope = let.symbol;

        for (var def : let.vars) {
            def.accept(this);

            IdSymbol symbol = def.symbol;
            if (symbol != null)
                currentScope.add(symbol);
        }

        var body = let.body;
        var bodyType = body.accept(this);
        currentScope = currentScope.getParent();

        return bodyType;
    }

    @Override
    public TypeSymbol visit(VarDef varDef) {
        var symbol = varDef.symbol;
        if (symbol != null) {
            var name = varDef.id.getText();
            var type = varDef.type.getText();
            var typeSymbol = (TypeSymbol) SymbolTable.globals.lookup(type);

            if (typeSymbol == null) {
                SymbolTable.error(varDef.ctx, varDef.type, "Let variable "
                        + name + " has undefined type " + type);
                varDef.symbol = null;
                return null;
            }

            var body = varDef.body;
            if (body != null) {
                var bodyType = body.accept(this);
                if (bodyType != null && !isSubtype(bodyType, typeSymbol)) {
                    SymbolTable.error(varDef.ctx, body.ctx.start, "Type " + bodyType.getName()
                            + " of initialization expression of identifier "
                            + name + " is incompatible with declared type " + type);
                }
            }

            symbol.setTypeName(type);
        }
        return null;
    }

    @Override
    public TypeSymbol visit(Case casee) {
        TypeSymbol result = null;
        for (var branch : casee.branches) {
            var branchType = branch.accept(this);
            if (branchType != null) {
                if (result == null)
                    result = branchType;
                else
                    result = leastCommonAncestor(result, branchType);
            }
        }
        return result;
    }

    @Override
    public TypeSymbol visit(Branch branch) {
        var typeName = branch.type.getText();
        if (typeName.equals("SELF_TYPE")) {
            return null;
        }

        var typeSymbol = (TypeSymbol) SymbolTable.globals.lookup(typeName);
        if (typeSymbol == null) {
            SymbolTable.error(branch.ctx, branch.type, "Case variable " + branch.id.getText()
                    + " has undefined type " + typeName);
            return null;
        }

        var body = branch.body;
        var bodyType = body.accept(this);

        return bodyType;
    }

    @Override
    public TypeSymbol visit(NewType newType) {
        var name = newType.type.getText();
        var symbol = (TypeSymbol) SymbolTable.globals.lookup(name);
        if (symbol == null) {
            SymbolTable.error(newType.ctx, newType.type, "new is used with undefined type " + name);
            return null;
        }
        return symbol;
    }

    @Override
    public TypeSymbol visit(IsVoid isVoid) {
        return TypeSymbol.BOOL;
    }

    @Override
    public TypeSymbol visit(PlusMinus plusMinus) {
        var leftType = plusMinus.left.accept(this);
        var rightType = plusMinus.right.accept(this);

        if (leftType == null || rightType == null)
            return TypeSymbol.INT;

        if (leftType != TypeSymbol.INT && rightType == TypeSymbol.INT) {
            SymbolTable.error(plusMinus.ctx, plusMinus.ctx.start, "Operand of " + plusMinus.op.getText()
                    +  " has type " + leftType.getName()
                    + " instead of Int");
        }

        if (leftType == TypeSymbol.INT && rightType != TypeSymbol.INT) {
            SymbolTable.error(plusMinus.ctx, plusMinus.ctx.stop, "Operand of " + plusMinus.op.getText()
                    + " has type " + rightType.getName()
                    + " instead of Int");
        }

        return TypeSymbol.INT;
    }

    @Override
    public TypeSymbol visit(MulDiv mulDiv) {
        var leftType = mulDiv.left.accept(this);
        var rightType = mulDiv.right.accept(this);

        if (leftType == null || rightType == null)
            return TypeSymbol.INT;

        if (leftType != TypeSymbol.INT && rightType == TypeSymbol.INT) {
            SymbolTable.error(mulDiv.ctx, mulDiv.ctx.start, "Operand of " + mulDiv.op.getText()
                    +  " has type " + leftType.getName()
                    + " instead of Int");
        }

        if (leftType == TypeSymbol.INT && rightType != TypeSymbol.INT) {
            SymbolTable.error(mulDiv.ctx, mulDiv.ctx.stop, "Operand of " + mulDiv.op.getText()
                    + " has type " + rightType.getName()
                    + " instead of Int");
        }

        return TypeSymbol.INT;
    }

    @Override
    public TypeSymbol visit(Neg neg) {
        var bodyType = neg.body.accept(this);

        if (bodyType == null)
            return null;

        if (bodyType != TypeSymbol.INT) {
            SymbolTable.error(neg.ctx, neg.body.ctx.stop, "Operand of ~ has type " + bodyType.getName()
                    + " instead of Int");
            return TypeSymbol.INT;
        }

        return bodyType;
    }

    @Override
    public TypeSymbol visit(LessThan lt) {
        var leftType = lt.left.accept(this);
        var rightType = lt.right.accept(this);

        if (leftType == null || rightType == null)
            return TypeSymbol.BOOL;

        if (leftType != TypeSymbol.INT && rightType == TypeSymbol.INT) {
            SymbolTable.error(lt.ctx, lt.ctx.start, "Operand of < has type " + leftType.getName()
                    + " instead of Int");
        }

        if (leftType == TypeSymbol.INT && rightType != TypeSymbol.INT) {
            SymbolTable.error(lt.ctx, lt.ctx.stop, "Operand of < has type " + rightType.getName()
                    + " instead of Int");
        }

        return TypeSymbol.BOOL;
    }

    @Override
    public TypeSymbol visit(LessThanOrEqual lte) {
        var leftType = lte.left.accept(this);
        var rightType = lte.right.accept(this);

        if (leftType == null || rightType == null)
            return TypeSymbol.BOOL;

        if (leftType != TypeSymbol.INT && rightType == TypeSymbol.INT) {
            SymbolTable.error(lte.ctx, lte.ctx.start, "Operand of <= has type " + leftType.getName()
                    + " instead of Int");
        }

        if (leftType == TypeSymbol.INT && rightType != TypeSymbol.INT) {
            SymbolTable.error(lte.ctx, lte.ctx.stop, "Operand of <= has type " + rightType.getName()
                    + " instead of Int");
        }

        return TypeSymbol.BOOL;
    }

    @Override
    public TypeSymbol visit(Equal equal) {
        var leftType = equal.left.accept(this);
        var rightType = equal.right.accept(this);

        if (leftType == null || rightType == null)
            return TypeSymbol.BOOL;

        if (leftType == TypeSymbol.INT || leftType == TypeSymbol.BOOL || leftType == TypeSymbol.STRING ||
                rightType == TypeSymbol.INT || rightType == TypeSymbol.BOOL || rightType == TypeSymbol.STRING)
            if (leftType != rightType) {
                SymbolTable.error(equal.ctx, ((CoolParser.EqContext) equal.ctx).EQ().getSymbol(), "Cannot compare "
                        + leftType.getName() + " with " + rightType.getName());
            }

        return TypeSymbol.BOOL;
    }

    @Override
    public TypeSymbol visit(Not not) {
        var bodyType = not.body.accept(this);

        if (bodyType == null)
            return null;

        if (bodyType != TypeSymbol.BOOL) {
            SymbolTable.error(not.ctx, not.body.ctx.stop, "Operand of not has type " + bodyType.getName()
                    + " instead of Bool");
            return TypeSymbol.BOOL;
        }

        return bodyType;
    }

    @Override
    public TypeSymbol visit(Paren paren) {
        return paren.body.accept(this);
    }

    @Override
    public TypeSymbol visit(Id id) {
        var name = id.token.getText();
        var symbol = (IdSymbol)currentScope.lookup(name);

        if (symbol == null) {
            SymbolTable.error(id.ctx, id.ctx.stop, "Undefined identifier " + name);
            return null;
        }

        return (TypeSymbol) SymbolTable.globals.lookup(symbol.getTypeName());
    }

    @Override
    public TypeSymbol visit(Int intt) {
        return TypeSymbol.INT;
    }

    @Override
    public TypeSymbol visit(Str str) {
        return TypeSymbol.STRING;
    }

    @Override
    public TypeSymbol visit(Bool bool) {
        return TypeSymbol.BOOL;
    }
};