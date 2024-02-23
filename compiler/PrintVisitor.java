package cool.compiler;

public class PrintVisitor implements ASTVisitor<Void>{
    int indent = 0;

    @Override
    public Void visit(Program program) {
        printIndent("program");
        indent++;
        for (var cls : program.classes) {
            cls.accept(this);
        }
        indent--;
        return null;
    }

    @Override
    public Void visit(Class classs) {
        printIndent("class");
        indent++;
        printIndent(classs.childType.getText());
        if (classs.parentType != null)
            printIndent(classs.parentType.getText());
        for (var feat : classs.features) {
            feat.accept(this);
        }
        indent--;
        return null;
    }

    @Override
    public Void visit(MethodDefinition methodDefinition) {
        printIndent("method");
        indent++;
        printIndent(methodDefinition.id.getText());
        for (var formal : methodDefinition.formals) {
            formal.accept(this);
        }
        printIndent(methodDefinition.type.getText());
        methodDefinition.body.accept(this);
        indent--;
        return null;
    }

    @Override
    public Void visit(Formal formal) {
        printIndent("formal");
        indent++;
        printIndent(formal.id.getText());
        printIndent(formal.type.getText());
        indent--;
        return null;
    }

    @Override
    public Void visit(AttributeDefinition attributeDefinition) {
        printIndent("attribute");
        indent++;
        printIndent(attributeDefinition.id.getText());
        printIndent(attributeDefinition.type.getText());
        if (attributeDefinition.body != null)
            attributeDefinition.body.accept(this);
        indent--;
        return null;
    }

    @Override
    public Void visit(Assign assign) {
        printIndent("<-");
        indent++;
        printIndent(assign.id.getText());
        assign.val.accept(this);
        indent--;
        return null;
    }

    @Override
    public Void visit(ExplicitDispatch explicitDispatch) {
        printIndent(".");
        indent++;
        explicitDispatch.left.accept(this);
        if (explicitDispatch.type != null)
            printIndent(explicitDispatch.type.getText());
        printIndent(explicitDispatch.id.getText());
        for (var param : explicitDispatch.params) {
            param.accept(this);
        }
        indent--;
        return null;
    }

    @Override
    public Void visit(ImplicitDispatch implicitDispatch) {
        printIndent("implicit dispatch");
        indent++;
        printIndent(implicitDispatch.id.getText());
        for (var param : implicitDispatch.params) {
            param.accept(this);
        }
        indent--;
        return null;
    }

    @Override
    public Void visit(If iff) {
        printIndent("if");
        indent++;
        iff.cond.accept(this);
        iff.thenBranch.accept(this);
        iff.elseBranch.accept(this);
        indent--;
        return null;
    }

    @Override
    public Void visit(While whilee) {
        printIndent("while");
        indent++;
        whilee.cond.accept(this);
        whilee.body.accept(this);
        indent--;
        return null;
    }

    @Override
    public Void visit(Block block) {
        printIndent("block");
        indent++;
        for (var line : block.lines) {
            line.accept(this);
        }
        indent--;
        return null;
    }

    @Override
    public Void visit(Let let) {
        printIndent("let");
        indent++;
        for (var var : let.vars) {
            var.accept(this);
        }
        let.body.accept(this);
        indent--;
        return null;
    }

    @Override
    public Void visit(VarDef varDef) {
        printIndent("local");
        indent++;
        printIndent(varDef.id.getText());
        printIndent(varDef.type.getText());
        if (varDef.body != null)
            varDef.body.accept(this);
        indent--;
        return null;
    }

    @Override
    public Void visit(Case casee) {
        printIndent("case");
        indent++;
        casee.cond.accept(this);
        for (var branch : casee.branches) {
            branch.accept(this);
        }
        indent--;
        return null;
    }

    @Override
    public Void visit(Branch branch) {
        printIndent("case branch");
        indent++;
        printIndent(branch.id.getText());
        printIndent(branch.type.getText());
        branch.body.accept(this);
        indent--;
        return null;
    }

    @Override
    public Void visit(NewType newType) {
        printIndent("new");
        indent++;
        printIndent(newType.type.getText());
        indent--;
        return null;
    }

    @Override
    public Void visit(IsVoid isVoid) {
        printIndent("isvoid");
        indent++;
        isVoid.body.accept(this);
        indent--;
        return null;
    }

    @Override
    public Void visit(PlusMinus plusMinus) {
        printIndent(plusMinus.op.getText());
        indent++;
        plusMinus.left.accept(this);
        plusMinus.right.accept(this);
        indent--;
        return null;
    }

    @Override
    public Void visit(MulDiv mulDiv) {
        printIndent(mulDiv.op.getText());
        indent++;
        mulDiv.left.accept(this);
        mulDiv.right.accept(this);
        indent--;
        return null;
    }

    @Override
    public Void visit(Neg neg) {
        printIndent("~");
        indent++;
        neg.body.accept(this);
        indent--;
        return null;
    }

    @Override
    public Void visit(LessThan lt) {
        printIndent("<");
        indent++;
        lt.left.accept(this);
        lt.right.accept(this);
        indent--;
        return null;
    }

    @Override
    public Void visit(LessThanOrEqual lte) {
        printIndent("<=");
        indent++;
        lte.left.accept(this);
        lte.right.accept(this);
        indent--;
        return null;
    }

    @Override
    public Void visit(Equal equal) {
        printIndent("=");
        indent++;
        equal.left.accept(this);
        equal.right.accept(this);
        indent--;
        return null;
    }

    @Override
    public Void visit(Not not) {
        printIndent("not");
        indent++;
        not.body.accept(this);
        indent--;
        return null;
    }

    @Override
    public Void visit(Paren paren) {
        paren.body.accept(this);
        return null;
    }

    @Override
    public Void visit(Id id) {
        printIndent(id.token.getText());
        return null;
    }

    @Override
    public Void visit(Int intt) {
        printIndent(intt.token.getText());
        return null;
    }

    @Override
    public Void visit(Str str) {
        printIndent(str.token.getText());
        return null;
    }

    @Override
    public Void visit(Bool bool) {
        printIndent(bool.token.getText());
        return null;
    }

    void printIndent(String str) {
        for (int i = 0; i < indent; i++)
            for (int j = 0; j < 2; j++)
                System.out.print(" ");
        System.out.println(str);
    }
}
