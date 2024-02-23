package cool.compiler;

import cool.parser.CoolParser;
import cool.parser.CoolParserBaseVisitor;
import org.antlr.v4.runtime.Token;

import java.util.LinkedList;

public class ASTConstructionVisitor extends CoolParserBaseVisitor<ASTNode> {
    @Override
    public ASTNode visitProgram(CoolParser.ProgramContext ctx) {
        var classes = new LinkedList<Class>();

        for (var cls : ctx.classes) {
            var c = (Class) visit(cls);
            if (c != null)
                classes.add(c);
        }

        return new Program(ctx, classes, ctx.start);
    }

    @Override
    public ASTNode visitClass(CoolParser.ClassContext ctx) {
        var features = new LinkedList<Feature>();

        for (var feat : ctx.features) {
            var f = (Feature) visit(feat);
            if (f != null)
                features.add(f);
        }

        return new Class(ctx, ctx.childType, ctx.parentType, features, ctx.start);
    }

    @Override
    public ASTNode visitMethodDefinion(CoolParser.MethodDefinionContext ctx) {
        var formals = new LinkedList<Formal>();

        for (var formal : ctx.formals) {
            var f = (Formal) visit(formal);
            if (f != null)
                formals.add(f);
        }

        return new MethodDefinition(ctx, ctx.ID().getSymbol(), formals, ctx.TYPE().getSymbol(), (Expression) visit(ctx.body), ctx.start);
    }

    @Override
    public ASTNode visitFormal(CoolParser.FormalContext ctx) {
        return new Formal(ctx, ctx.ID().getSymbol(), ctx.TYPE().getSymbol(), ctx.start);
    }

    @Override
    public ASTNode visitAttributeDefinition(CoolParser.AttributeDefinitionContext ctx) {
        Expression body = null;
        if (ctx.body != null)
            body = (Expression) visit(ctx.body);
        return new AttributeDefinition(ctx, ctx.ID().getSymbol(), ctx.TYPE().getSymbol(), body, ctx.start);
    }

    @Override
    public ASTNode visitAssign(CoolParser.AssignContext ctx) {
        return new Assign(ctx, ctx.ID().getSymbol(), (Expression) visit(ctx.val), ctx.start);
    }

    @Override
    public ASTNode visitExplicitDispatch(CoolParser.ExplicitDispatchContext ctx) {
        var params = new LinkedList<Expression>();

        for (var param : ctx.params) {
            var p = (Expression) visit(param);
            if (p != null)
                params.add(p);
        }

        Token type = null;
        if (ctx.TYPE() != null)
            type = ctx.TYPE().getSymbol();

        return new ExplicitDispatch(ctx, (Expression) visit(ctx.left), type, ctx.ID().getSymbol(), params, ctx.start);
    }

    @Override
    public ASTNode visitImplicitDispatch(CoolParser.ImplicitDispatchContext ctx) {
        var params = new LinkedList<Expression>();

        for (var param : ctx.params) {
            var p = (Expression) visit(param);
            if (p != null)
                params.add(p);
        }

        return new ImplicitDispatch(ctx, ctx.ID().getSymbol(), params, ctx.start);
    }

    @Override
    public ASTNode visitIf(CoolParser.IfContext ctx) {
        return new If(ctx, (Expression) visit(ctx.cond), (Expression) visit(ctx.then), (Expression) visit(ctx.else_), ctx.start);
    }

    @Override
    public ASTNode visitWhile(CoolParser.WhileContext ctx) {
        return new While(ctx, (Expression) visit(ctx.cond), (Expression) visit(ctx.body), ctx.start);
    }

    @Override
    public ASTNode visitBlock(CoolParser.BlockContext ctx) {
        var lines = new LinkedList<Expression>();

        for (var line : ctx.lines) {
            var l = (Expression) visit(line);
            if (l != null)
                lines.add(l);
        }

        return new Block(ctx, lines, ctx.start);
    }

    @Override
    public ASTNode visitLet(CoolParser.LetContext ctx) {
        var varDefs = new LinkedList<VarDef>();

        for (var varDef : ctx.vars) {
            var v = (VarDef) visit(varDef);
            if (v != null)
                varDefs.add(v);
        }

        return new Let(ctx, (Expression) visit(ctx.body), varDefs, ctx.start);
    }

    @Override
    public ASTNode visitVarDef(CoolParser.VarDefContext ctx) {
        Expression body = null;
        if (ctx.body != null)
            body = (Expression) visit(ctx.body);
        return new VarDef(ctx, ctx.ID().getSymbol(), ctx.TYPE().getSymbol(), body, ctx.start);
    }

    @Override
    public ASTNode visitCase(CoolParser.CaseContext ctx) {
        var branches = new LinkedList<Branch>();

        for (var branch : ctx.branches) {
            var b = (Branch) visit(branch);
            if (b != null)
                branches.add(b);
        }

        return new Case(ctx, (Expression) visit(ctx.cond), branches, ctx.start);
    }

    @Override
    public ASTNode visitBranch(CoolParser.BranchContext ctx) {
        return new Branch(ctx, ctx.ID().getSymbol(), ctx.TYPE().getSymbol(), (Expression) visit(ctx.body), ctx.start);
    }

    @Override
    public ASTNode visitNew(CoolParser.NewContext ctx) {
        return new NewType(ctx, ctx.TYPE().getSymbol(), ctx.start);
    }

    @Override
    public ASTNode visitIsvoid(CoolParser.IsvoidContext ctx) {
        return new IsVoid(ctx, (Expression) visit(ctx.body), ctx.start);
    }

    @Override
    public ASTNode visitPlusMinus(CoolParser.PlusMinusContext ctx) {
        return new PlusMinus(ctx, (Expression) visit(ctx.left), (Expression) visit(ctx.right), ctx.op, ctx.start);
    }

    @Override
    public ASTNode visitMulDiv(CoolParser.MulDivContext ctx) {
        return new MulDiv(ctx, (Expression) visit(ctx.left), (Expression) visit(ctx.right), ctx.op, ctx.start);
    }

    @Override
    public ASTNode visitNeg(CoolParser.NegContext ctx) {
        return new Neg(ctx, (Expression) visit(ctx.body), ctx.start);
    }

    @Override
    public ASTNode visitLt(CoolParser.LtContext ctx) {
        return new LessThan(ctx, (Expression) visit(ctx.left), (Expression) visit(ctx.right), ctx.start);
    }

    @Override
    public ASTNode visitLte(CoolParser.LteContext ctx) {
        return new LessThanOrEqual(ctx, (Expression) visit(ctx.left), (Expression) visit(ctx.right), ctx.start);
    }

    @Override
    public ASTNode visitEq(CoolParser.EqContext ctx) {
        return new Equal(ctx, (Expression) visit(ctx.left), (Expression) visit(ctx.right), ctx.start);
    }

    @Override
    public ASTNode visitNot(CoolParser.NotContext ctx) {
        return new Not(ctx, (Expression) visit(ctx.body), ctx.start);
    }

    @Override
    public ASTNode visitParen(CoolParser.ParenContext ctx) {
        return new Paren(ctx, (Expression) visit(ctx.body), ctx.start);
    }

    @Override
    public ASTNode visitId(CoolParser.IdContext ctx) {
        return new Id(ctx, ctx.ID().getSymbol());
    }

    @Override
    public ASTNode visitInt(CoolParser.IntContext ctx) {
        return new Int(ctx, ctx.INT().getSymbol());
    }

    @Override
    public ASTNode visitString(CoolParser.StringContext ctx) {
        return new Str(ctx, ctx.STRING().getSymbol());
    }

    @Override
    public ASTNode visitBool(CoolParser.BoolContext ctx) {
        return new Bool(ctx, ctx.BOOL().getSymbol());
    }
}
