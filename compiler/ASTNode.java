package cool.compiler;

import cool.structures.FunctionSymbol;
import cool.structures.IdSymbol;
import cool.structures.LetSymbol;
import cool.structures.TypeSymbol;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import java.util.LinkedList;

public abstract class ASTNode {
    ParserRuleContext ctx;
    Token token;

    ASTNode(ParserRuleContext ctx,
            Token token) {
        this.ctx = ctx;
        this.token = token;
    }
    public <T> T accept(ASTVisitor<T> visitor) {
        return null;
    }
}

abstract class Expression extends ASTNode {
    Expression(ParserRuleContext ctx, Token token) { super(ctx, token); }
}

abstract class Feature extends ASTNode {
    Feature(ParserRuleContext ctx, Token token) { super(ctx, token); }
}

class Program extends ASTNode {
    LinkedList<Class> classes;

    Program(ParserRuleContext ctx,
            LinkedList<Class> classes,
            Token start) {
        super(ctx, start);
        this.classes = classes;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Class extends ASTNode {
    Token childType;
    Token parentType;
    LinkedList<Feature> features;
    TypeSymbol type;

    Class(ParserRuleContext ctx,
          Token childType,
          Token parentType,
          LinkedList<Feature> features,
          Token start) {
        super(ctx, start);
        this.childType = childType;
        this.parentType = parentType;
        this.features = features;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class MethodDefinition extends Feature {
    Token id;
    LinkedList<Formal> formals;
    Token type;
    Expression body;
    FunctionSymbol symbol;

    MethodDefinition(ParserRuleContext ctx,
                     Token id,
                     LinkedList<Formal> formals,
                     Token type,
                     Expression body,
                     Token start) {
        super(ctx, start);
        this.id = id;
        this.formals = formals;
        this.type = type;
        this.body = body;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Formal extends ASTNode {
    Token id;
    Token type;
    IdSymbol symbol;

    Formal(ParserRuleContext ctx,
           Token id,
           Token type,
           Token start) {
        super(ctx, start);
        this.id = id;
        this.type = type;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class AttributeDefinition extends Feature {
    Token id;
    Token type;
    Expression body;
    IdSymbol symbol;

    AttributeDefinition(ParserRuleContext ctx,
                        Token id,
                        Token type,
                        Expression body,
                        Token start) {
        super(ctx, start);
        this.id = id;
        this.type = type;
        this.body = body;

    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Assign extends Expression {
    Token id;
    Expression val;

    Assign(ParserRuleContext ctx,
           Token id,
           Expression val,
           Token start) {
        super(ctx, start);
        this.id = id;
        this.val = val;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ExplicitDispatch extends Expression {
    Expression left;
    Token type;
    Token id;
    LinkedList<Expression> params;
    TypeSymbol symbol;

    ExplicitDispatch(ParserRuleContext ctx,
                     Expression left,
                     Token type,
                     Token id,
                     LinkedList<Expression> params,
                     Token start) {
        super(ctx, start);
        this.left = left;
        this.type = type;
        this.id = id;
        this.params = params;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class ImplicitDispatch extends Expression {
    Token id;
    LinkedList<Expression> params;

    ImplicitDispatch(ParserRuleContext ctx,
                     Token id,
                     LinkedList<Expression> params,
                     Token start) {
        super(ctx, start);
        this.id = id;
        this.params = params;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class If extends Expression {
    Expression cond;
    Expression thenBranch;
    Expression elseBranch;

    If(ParserRuleContext ctx,
       Expression cond,
       Expression thenBranch,
       Expression elseBranch,
       Token start) {
        super(ctx, start);
        this.cond = cond;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class While extends Expression {
    Expression cond;
    Expression body;

    While(ParserRuleContext ctx,
          Expression cond,
          Expression body,
          Token start) {
        super(ctx, start);
        this.cond = cond;
        this.body = body;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Block extends Expression {
    LinkedList<Expression> lines;

    Block(ParserRuleContext ctx,
          LinkedList<Expression> lines,
          Token start) {
        super(ctx, start);
        this.lines = lines;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Let extends Expression {
    Expression body;
    LinkedList<VarDef> vars;

    LetSymbol symbol;

    Let(ParserRuleContext ctx,
        Expression body,
        LinkedList<VarDef> vars,
        Token start) {
        super(ctx, start);
        this.body = body;
        this.vars = vars;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class VarDef extends ASTNode {
    Token id;
    Token type;
    Expression body;
    IdSymbol symbol;

    VarDef(ParserRuleContext ctx,
           Token id,
           Token type,
           Expression body,
           Token start) {
        super(ctx, start);
        this.id = id;
        this.type = type;
        this.body = body;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Case extends Expression {
    Expression cond;
    LinkedList<Branch> branches;

    Case(ParserRuleContext ctx,
         Expression cond,
         LinkedList<Branch> branches,
         Token start) {
        super(ctx, start);
        this.cond = cond;
        this.branches = branches;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Branch extends ASTNode {
    Token id;
    Token type;
    Expression body;

    Branch(ParserRuleContext ctx,
           Token id,
           Token type,
           Expression body,
           Token start) {
        super(ctx, start);
        this.body = body;
        this.id = id;
        this.type = type;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class NewType extends Expression {
    Token type;

    NewType(ParserRuleContext ctx,
            Token type,
            Token start) {
        super(ctx, start);
        this.type = type;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class IsVoid extends Expression {
    Expression body;

    IsVoid(ParserRuleContext ctx,
           Expression body,
           Token start) {
        super(ctx, start);
        this.body = body;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class PlusMinus extends Expression {
    Expression left;
    Expression right;
    Token op;

    PlusMinus(ParserRuleContext ctx,
              Expression left,
              Expression right,
              Token op,
              Token start) {
        super(ctx, start);
        this.op = op;
        this.left = left;
        this.right = right;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class MulDiv extends Expression {
    Expression left;
    Expression right;
    Token op;

    MulDiv(ParserRuleContext ctx,
           Expression left,
           Expression right,
           Token op,
           Token start) {
        super(ctx, start);
        this.op = op;
        this.left = left;
        this.right = right;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Neg extends Expression {
    Expression body;

    Neg(ParserRuleContext ctx,
        Expression body,
        Token start) {
        super(ctx, start);
        this.body = body;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class LessThan extends Expression {
    Expression left;
    Expression right;

    LessThan(ParserRuleContext ctx,
             Expression left,
             Expression right,
             Token start) {
        super(ctx, start);
        this.left = left;
        this.right = right;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class LessThanOrEqual extends Expression {
    Expression left;
    Expression right;

    LessThanOrEqual(ParserRuleContext ctx,
                    Expression left,
                    Expression right,
                    Token start) {
        super(ctx, start);
        this.left = left;
        this.right = right;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Equal extends Expression {
    Expression left;
    Expression right;

    Equal(ParserRuleContext ctx,
          Expression left,
          Expression right,
          Token start) {
        super(ctx, start);
        this.left = left;
        this.right = right;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Not extends Expression {
    Expression body;

    Not(ParserRuleContext ctx,
        Expression body,
        Token start) {
        super(ctx, start);
        this.body = body;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Paren extends Expression {
    Expression body;

    Paren(ParserRuleContext ctx,
          Expression body,
          Token start) {
        super(ctx, start);
        this.body = body;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Id extends Expression {
    Id(ParserRuleContext ctx, Token token) {
        super(ctx, token);
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Int extends Expression {
    Int(ParserRuleContext ctx, Token token) {
        super(ctx, token);
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Str extends Expression {
    Str(ParserRuleContext ctx, Token token) {
        super(ctx, token);
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

class Bool extends Expression {
    Bool(ParserRuleContext ctx, Token token) {
        super(ctx, token);
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}