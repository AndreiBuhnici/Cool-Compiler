package cool.compiler;

import cool.parser.CoolParser;
import cool.structures.*;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.io.File;
import java.util.*;

public class CodeGenVisitor implements ASTVisitor<ST> {
    static STGroupFile templates = new STGroupFile("cool/compiler/cgen.stg");

    Scope currentScope = SymbolTable.globals;
    int k = 0;
    int ifCount = 0;
    int whileCount = 0;
    int isVoidCount = 0;
    int notCount = 0;
    int equalCount = 0;
    int lessThanCount = 0;

    ST protObjects;
    ST dispTables;
    ST nameTables;
    ST objTables;
    ST funcSection;
    ST constants;

    List<Integer> constInts = new ArrayList<>();
    List<String> constStrings = new ArrayList<>();

    private String addInt(int i) {
        int label;
        if (!constInts.contains(i)) {
            label = constInts.size();

            constInts.add(i);

            ST constInt = templates.getInstanceOf("constInt").add("label", label).add("val", i).add("tag", TypeSymbol.INT.tag);

            constants.add("e", constInt);
        } else {
            label = constInts.indexOf(i);
        }

        return "int_const" + label;
    }

    private String addString(String s) {
        int label;
        if (!constStrings.contains(s)) {
            label = constStrings.size();

            constStrings.add(s);

            ST constString = templates.getInstanceOf("constString")
                    .add("str", s)
                    .add("label", label)
                    .add("tag", TypeSymbol.STRING.tag)
                    .add("l", addInt(s.length()))
                    .add("wordCount", (s.length() + 1) / 4 + 5);

            constants.add("e", constString);
        } else {
            label = constStrings.indexOf(s);
        }

        return "str_const" + label;
    }

    private void addBool() {
        ST constBoolFalse = templates.getInstanceOf("constBool")
                .add("val", "false")
                .add("tag", TypeSymbol.BOOL.tag)
                .add("valInt", 0);

        ST constBoolTrue = templates.getInstanceOf("constBool")
                .add("val", "true")
                .add("tag", TypeSymbol.BOOL.tag)
                .add("valInt", 1);

        constants.add("e", constBoolFalse).add("e", constBoolTrue);
    }

    private String getClassAttributes(TypeSymbol symbol) {
        if (symbol == TypeSymbol.INT || symbol == TypeSymbol.BOOL)
            return "\t.word\t0\n";
        if (symbol == TypeSymbol.STRING)
            return "\t.word\tint_const0\n\t.asciiz\t\"\"\n\t.align\t2\n";

        StringBuilder attributes = new StringBuilder();

        if (symbol.getParent() != null) {
            attributes.append(getClassAttributes(symbol.getParent()));
        }

        for (var attr : symbol.symbols.values()) {
            if (attr.getName().equals("self"))
                continue;
            if (!(attr instanceof FunctionSymbol)) {
                var type = attr.getTypeName();
                if (type.equals(TypeSymbol.INT.getName()))
                    attributes.append("\t.word\tint_const0\n");
                else if (type.equals(TypeSymbol.STRING.getName()))
                    attributes.append("\t.word\tstr_const0\n");
                else if (type.equals(TypeSymbol.BOOL.getName()))
                    attributes.append("\t.word\tbool_const_false\n");
                else
                    attributes.append("\t.word\t0\n");
            }
        }

        return attributes.toString();
    }

    @Override
    public ST visit(Program program) {
        protObjects = templates.getInstanceOf("sequence");
        dispTables = templates.getInstanceOf("sequence");
        nameTables = templates.getInstanceOf("sequence");
        objTables = templates.getInstanceOf("sequence");
        constants = templates.getInstanceOf("sequence");
        funcSection = templates.getInstanceOf("sequence");

        int tag = 0;
        for (var symb : ((DefaultScope)SymbolTable.globals).symbols.values()) {
            if (symb != TypeSymbol.SELF_TYPE) {
                var type = (TypeSymbol) symb;
                type.tag = tag;
                tag++;
            }
        }

        addInt(0);
        addString("");

        for (var symb : ((DefaultScope)SymbolTable.globals).symbols.values()) {
            if (symb != TypeSymbol.SELF_TYPE) {
                var type = (TypeSymbol) symb;

                var parent = type;
                var size = 3;

                while (parent != null) {
                    for (var attr : parent.symbols.values()) {
                        if (!(attr instanceof FunctionSymbol)) {
                            if (!Objects.equals(attr.getName(), "self"))
                                size++;
                        }
                    }
                    parent = parent.getParent();
                }

                protObjects.add("e", templates.getInstanceOf("protObj")
                        .add("class", type.getName())
                        .add("tag", type.tag)
                        .add("size", size)
                        .add("attr", getClassAttributes(type)));

                ST classDispTable = templates.getInstanceOf("sequence");
                HashMap<String, String> funcNames = new LinkedHashMap<>();

                if (type.getParent() != null) {
                    parent = type.getParent();
                    List<TypeSymbol> parents = new ArrayList<>();
                    while (parent != null) {
                        parents.add(parent);
                        parent = parent.getParent();
                    }

                    Collections.reverse(parents);
                    for (var parentt : parents) {
                        for (var func : parentt.symbols.values()) {
                            if (func instanceof FunctionSymbol) {
                                var funcSym = (FunctionSymbol) func;
                                if (!type.symbols.containsKey(funcSym.getName())) {
                                    if (funcNames.containsKey(funcSym.getName()))
                                        funcNames.replace(funcSym.getName(), parentt.getName() + "." + funcSym.getName());
                                    else
                                        funcNames.put(funcSym.getName(), parentt.getName() + "." + funcSym.getName());
                                } else {
                                    funcNames.put(funcSym.getName(), type.getName() + "." + funcSym.getName());
                                }
                            }
                        }
                    }
                }

                for (var func : type.symbols.values()) {
                    if (func instanceof FunctionSymbol) {
                        var funcSym = (FunctionSymbol) func;
                        if (!funcNames.containsKey(funcSym.getName()))
                            funcNames.put(funcSym.getName(), type.getName() + "." + funcSym.getName());
                    }
                }

                for (var func : funcNames.values()) {
                    classDispTable.add("e", templates.getInstanceOf("dispTabEntry").add("func", func));
                }

                dispTables.add("e", templates.getInstanceOf("dispTab").add("class", type.getName()).add("funcs", classDispTable));

                nameTables.add("e", "\t.word\t" + addString(type.getName()));

                objTables.add("e", templates.getInstanceOf("objTabEntry").add("class", type.getName()));

                addInt(type.getName().length());
            }
        }

        addBool();

        funcSection.add("e", templates.getInstanceOf("functionInit")
                    .add("class", TypeSymbol.OBJECT.getName())
                    .add("parent", TypeSymbol.OBJECT.getParentName()))
                .add("e", templates.getInstanceOf("functionInit")
                        .add("class", TypeSymbol.INT.getName())
                        .add("parent", TypeSymbol.INT.getParentName()))
                .add("e", templates.getInstanceOf("functionInit")
                        .add("class", TypeSymbol.IO.getName())
                        .add("parent", TypeSymbol.IO.getParentName()))
                .add("e", templates.getInstanceOf("functionInit")
                        .add("class", TypeSymbol.STRING.getName())
                        .add("parent", TypeSymbol.STRING.getParentName()))
                .add("e", templates.getInstanceOf("functionInit")
                        .add("class", TypeSymbol.BOOL.getName())
                        .add("parent", TypeSymbol.BOOL.getParentName()));

        for (var classs : program.classes) {
            classs.accept(this);
        }

        ST tags = templates.getInstanceOf("sequence")
                .add("e", templates.getInstanceOf("tag")
                        .add("class", "int").add("tag", TypeSymbol.INT.tag))
                .add("e", templates.getInstanceOf("tag")
                        .add("class", "string").add("tag", TypeSymbol.STRING.tag))
                .add("e", templates.getInstanceOf("tag")
                        .add("class", "bool").add("tag", TypeSymbol.BOOL.tag));

        return templates.getInstanceOf("program")
                .add("dispTabs", dispTables)
                .add("objTabs", objTables)
                .add("protObjs", protObjects)
                .add("nameTabs", nameTables)
                .add("tags", tags)
                .add("constants", constants)
                .add("functions", funcSection);
    }

    @Override
    public ST visit(Class classs) {
        var symbol = classs.type;

        currentScope = symbol;

        StringBuilder attribs = new StringBuilder();
        for (var feature: classs.features) {
            if (feature instanceof AttributeDefinition) {
                var st = feature.accept(this);
                if (st != null)
                    attribs.append(st.render()).append("\n");
            }
        }

        if (!attribs.isEmpty())
            attribs.deleteCharAt(attribs.length() - 1);

        funcSection.add("e", templates.getInstanceOf("functionInit")
                .add("class", symbol.getName())
                .add("parent", symbol.getParentName())
                .add("attrs", attribs));

        for (var feature : classs.features) {
            if (feature instanceof MethodDefinition) {
                feature.accept(this);
            }
        }

        currentScope = SymbolTable.globals;

        return null;
    }

    @Override
    public ST visit(MethodDefinition methodDefinition) {
        var symbol = methodDefinition.symbol;
        currentScope = symbol;

        var func = templates.getInstanceOf("function")
                .add("name", symbol.getName())
                .add("body", methodDefinition.body.accept(this))
                .add("nr", methodDefinition.formals.size() * 4 + 12)
                .add("class", ((TypeSymbol) currentScope.getParent()).getName());

        funcSection.add("e", func);

        currentScope = currentScope.getParent();
        return func;
    }

    @Override
    public ST visit(Formal formal) {
        return null;
    }

    @Override
    public ST visit(AttributeDefinition attributeDefinition) {
        var body = attributeDefinition.body;
        if (body != null)
            return templates.getInstanceOf("attribInit").add("val", body.accept(this))
                    .add("offset", attributeDefinition.symbol.offset);
        return null;
    }

    @Override
    public ST visit(Assign assign) {
        var name = assign.id.getText();

        var classScope = currentScope;
        while (classScope.getParent() != null && !(classScope instanceof TypeSymbol))
            classScope = classScope.getParent();

        if (classScope.lookup(name) != null && !(classScope.lookup(name) instanceof FunctionSymbol)) {
            if (currentScope.lookup_once(name) != null) {
                var symbol = currentScope.lookup(name);
                return templates.getInstanceOf("storeLocal").add("val", assign.val.accept(this))
                        .add("offset", ((IdSymbol) symbol).offset);
            } else {
                var symbol = classScope.lookup(name);
                return templates.getInstanceOf("storeAttrib").add("val", assign.val.accept(this))
                        .add("offset", ((IdSymbol) symbol).offset);
            }
        } else {
            var symbol = currentScope.lookup(name);
            return templates.getInstanceOf("storeLocal").add("val", assign.val.accept(this))
                    .add("offset", ((IdSymbol) symbol).offset);
        }
    }

    @Override
    public ST visit(ExplicitDispatch explicitDispatch) {
        var params = explicitDispatch.params;
        Collections.reverse(params);

        StringBuilder paramStr = new StringBuilder();
        for (var param : params) {
            var p = param.accept(this);
            paramStr.append(templates.getInstanceOf("param").add("name", p).render()).append("\n");
        }

        if (!paramStr.isEmpty())
            paramStr.deleteCharAt(paramStr.length() - 1);

        var func = explicitDispatch.id.getText();

        var ctx = explicitDispatch.ctx;
        while (!(ctx.getParent() instanceof CoolParser.ProgramContext))
            ctx = ctx.getParent();

        ST explicitDisp = templates.getInstanceOf("explicitDispatch")
                .add("i", k++)
                .add("params", paramStr)
                .add("offset", ((FunctionSymbol) explicitDispatch.symbol.lookup(func)).offset)
                .add("filename", addString(new File(Compiler.fileNames.get(ctx)).getName()))
                .add("line", explicitDispatch.id.getLine());

        var caller = !explicitDispatch.left.ctx.getText().equals("self") ? explicitDispatch.left.accept(this) : null;
        if (caller != null)
            explicitDisp.add("caller", caller);

        var atType = explicitDispatch.type;
        if (atType != null)
            explicitDisp.add("type", atType.getText());

        return explicitDisp;
    }

    @Override
    public ST visit(ImplicitDispatch implicitDispatch) {
        var params = implicitDispatch.params;
        Collections.reverse(params);

        StringBuilder paramStr = new StringBuilder();
        for (var param : params) {
            var p = param.accept(this);
            paramStr.append(templates.getInstanceOf("param").add("name", p).render()).append("\n");
        }

        if (!paramStr.isEmpty())
            paramStr.deleteCharAt(paramStr.length() - 1);

        var func = implicitDispatch.id.getText();

        var ctx = implicitDispatch.ctx;
        while (!(ctx.getParent() instanceof CoolParser.ProgramContext))
            ctx = ctx.getParent();

        return templates.getInstanceOf("implicitDispatch")
                .add("i", k++)
                .add("params", paramStr)
                .add("offset", ((FunctionSymbol) currentScope.lookup(func)).offset)
                .add("filename", addString(new File(Compiler.fileNames.get(ctx)).getName()))
                .add("line", implicitDispatch.id.getLine());
    }

    @Override
    public ST visit(If iff) {
        return templates.getInstanceOf("if")
                .add("cond", iff.cond.accept(this))
                .add("then", iff.thenBranch.accept(this))
                .add("el", iff.elseBranch.accept(this))
                .add("k", ifCount++);
    }

    @Override
    public ST visit(While whilee) {
        return templates.getInstanceOf("while")
                .add("cond", whilee.cond.accept(this))
                .add("body", whilee.body.accept(this))
                .add("k", whileCount++);
    }

    @Override
    public ST visit(Block block) {
        ST blockSt = templates.getInstanceOf("sequence");
        for (var expr : block.lines) {
            var e = expr.accept(this);
            blockSt.add("e", e);
        }

        return blockSt;
    }

    @Override
    public ST visit(Let let) {
        var letSt = templates.getInstanceOf("sequence");
        var letInitSt = templates.getInstanceOf("let").add("count", let.vars.size() * -4);

        letSt.add("e", letInitSt);

        currentScope = let.symbol;

        int i = -4;
        for (var var : let.vars) {
            var.symbol.offset = i;
            letSt.add("e", var.accept(this));
            i -= 4;
        }

        letSt.add("e", let.body.accept(this));

        currentScope = currentScope.getParent();

        var letEndSt = templates.getInstanceOf("let").add("count", let.vars.size() * 4);
        letSt.add("e", letEndSt);

        return letSt;
    }

    @Override
    public ST visit(VarDef varDef) {
        var val = varDef.body;

        if (val == null) {
            var symbol = varDef.symbol.getTypeName();
            if (symbol.equals(TypeSymbol.INT.getName())) {
                return templates.getInstanceOf("storeLocal").add("val", "\tla\t\t$a0 int_const0")
                        .add("offset", varDef.symbol.offset);
            } else if (symbol.equals(TypeSymbol.STRING.getName())) {
                return templates.getInstanceOf("storeLocal").add("val", "\tla\t\t$a0 str_const0")
                        .add("offset", varDef.symbol.offset);
            } else if (symbol.equals(TypeSymbol.BOOL.getName())) {
                return templates.getInstanceOf("storeLocal").add("val", "\tla\t\t$a0 bool_const_false")
                        .add("offset", varDef.symbol.offset);
            } else {
                return templates.getInstanceOf("storeLocal").add("val", "\tli\t\t$a0 0")
                        .add("offset", varDef.symbol.offset);
            }
        } else {
            return templates.getInstanceOf("storeLocal").add("val", val.accept(this).render())
                    .add("offset", varDef.symbol.offset);
        }
    }

    @Override
    public ST visit(Case casee) {
        return null;
    }

    @Override
    public ST visit(Branch branch) {
        return null;
    }

    @Override
    public ST visit(NewType newType) {
        var type = newType.type.getText();

        if (type.equals("SELF_TYPE"))
            return templates.getInstanceOf("newSelf");
        else
            return templates.getInstanceOf("new").add("type", type);
    }

    @Override
    public ST visit(IsVoid isVoid) {
        return templates.getInstanceOf("isVoid")
                .add("expr", isVoid.body.accept(this)).add("k", isVoidCount++);
    }

    @Override
    public ST visit(PlusMinus plusMinus) {
        if (plusMinus.op.getText().equals("+"))
            return templates.getInstanceOf("arithmeticOp")
                    .add("op", "add")
                    .add("left", plusMinus.left.accept(this))
                    .add("right", plusMinus.right.accept(this));
        else
            return templates.getInstanceOf("arithmeticOp")
                    .add("op", "sub")
                    .add("left", plusMinus.left.accept(this))
                    .add("right", plusMinus.right.accept(this));
    }

    @Override
    public ST visit(MulDiv mulDiv) {
        if (mulDiv.op.getText().equals("*"))
            return templates.getInstanceOf("arithmeticOp")
                    .add("op", "mul")
                    .add("left", mulDiv.left.accept(this))
                    .add("right", mulDiv.right.accept(this));
        else
            return templates.getInstanceOf("arithmeticOp")
                    .add("op", "div")
                    .add("left", mulDiv.left.accept(this))
                    .add("right", mulDiv.right.accept(this));
    }

    @Override
    public ST visit(Neg neg) {
        return templates.getInstanceOf("neg")
                .add("expr", neg.body.accept(this));
    }

    @Override
    public ST visit(LessThan lt) {
        return templates.getInstanceOf("lt")
                .add("op", "blt")
                .add("left", lt.left.accept(this))
                .add("right", lt.right.accept(this))
                .add("k", lessThanCount++);
    }

    @Override
    public ST visit(LessThanOrEqual lte) {
        return templates.getInstanceOf("lt")
                .add("op", "ble")
                .add("left", lte.left.accept(this))
                .add("right", lte.right.accept(this))
                .add("k", lessThanCount++);
    }

    @Override
    public ST visit(Equal equal) {
        return templates.getInstanceOf("eq")
                .add("left", equal.left.accept(this))
                .add("right", equal.right.accept(this))
                .add("k", equalCount++);
    }

    @Override
    public ST visit(Not not) {
        return templates.getInstanceOf("not")
                .add("expr", not.body.accept(this)).add("k", notCount++);
    }

    @Override
    public ST visit(Paren paren) {
        return paren.body.accept(this);
    }

    @Override
    public ST visit(Id id) {
        var name = id.token.getText();
        if (name.equals("self"))
            return templates.getInstanceOf("self");
        else {
            var classScope = currentScope;
            while (classScope.getParent() != null && !(classScope instanceof TypeSymbol))
                classScope = classScope.getParent();

            if (classScope.lookup(name) != null && !(classScope.lookup(name) instanceof FunctionSymbol)) {
                if (currentScope.lookup_once(name) != null) {
                    var symbol = currentScope.lookup(name);
                    return templates.getInstanceOf("local").add("offset", ((IdSymbol) symbol).offset);
                } else {
                    var symbol = classScope.lookup(name);
                    return templates.getInstanceOf("attribute").add("offset", ((IdSymbol) symbol).offset);
                }
            } else {
                var symbol = currentScope.lookup(name);
                return templates.getInstanceOf("local").add("offset", ((IdSymbol) symbol).offset);
            }
        }
    }

    @Override
    public ST visit(Int intt) {
        return templates.getInstanceOf("literal")
                .add("addr", addInt(Integer.parseInt(intt.token.getText())));
    }

    @Override
    public ST visit(Str str) {
        return templates.getInstanceOf("literal")
                .add("addr", addString(str.token.getText()));
    }

    @Override
    public ST visit(Bool bool) {
        return templates.getInstanceOf("literal")
                .add("addr", bool.token.getText().equals("true") ? "bool_const_true" : "bool_const_false");
    }
}
