package cool.structures;

import java.io.File;

import org.antlr.v4.runtime.*;

import cool.compiler.Compiler;
import cool.parser.CoolParser;

public class SymbolTable {
    public static Scope globals;
    
    private static boolean semanticErrors;
    
    public static void defineBasicClasses() {
        globals = new DefaultScope(null);
        semanticErrors = false;

        globals.add(TypeSymbol.OBJECT);
        globals.add(TypeSymbol.IO);
        globals.add(TypeSymbol.INT);
        globals.add(TypeSymbol.STRING);
        globals.add(TypeSymbol.BOOL);
        globals.add(TypeSymbol.SELF_TYPE);

        var abortFunc = new FunctionSymbol(TypeSymbol.OBJECT, "abort");
        abortFunc.setReturnType(TypeSymbol.OBJECT.name);
        abortFunc.offset = 0;
        var typeNameFunc = new FunctionSymbol(TypeSymbol.OBJECT, "type_name");
        typeNameFunc.setReturnType(TypeSymbol.STRING.name);
        typeNameFunc.offset = 4;
        var copyFunc = new FunctionSymbol(TypeSymbol.OBJECT, "copy");
        copyFunc.setReturnType(TypeSymbol.SELF_TYPE.name);
        copyFunc.offset = 8;

        var lengthFunc = new FunctionSymbol(TypeSymbol.STRING, "length");
        lengthFunc.setReturnType(TypeSymbol.INT.name);
        lengthFunc.offset = 12;
        var concatFunc = new FunctionSymbol(TypeSymbol.STRING, "concat");
        concatFunc.setReturnType(TypeSymbol.STRING.name);
        var concatParam = new IdSymbol("str");
        concatParam.setTypeName(TypeSymbol.STRING.name);
        concatFunc.add(concatParam);
        concatFunc.offset = 16;
        var substrFunc = new FunctionSymbol(TypeSymbol.STRING, "substr");
        substrFunc.setReturnType(TypeSymbol.STRING.name);
        var indexParam = new IdSymbol("index");
        indexParam.setTypeName(TypeSymbol.INT.name);
        substrFunc.add(indexParam);
        substrFunc.offset = 20;
        var lengthParam = new IdSymbol("l");
        lengthParam.setTypeName(TypeSymbol.INT.name);
        substrFunc.add(lengthParam);

        var outStringFunc = new FunctionSymbol(TypeSymbol.IO, "out_string");
        outStringFunc.setReturnType(TypeSymbol.SELF_TYPE.name);
        var outStringParam = new IdSymbol("str");
        outStringParam.setTypeName(TypeSymbol.STRING.name);
        outStringFunc.add(outStringParam);
        outStringFunc.offset = 12;
        var inStringFunc = new FunctionSymbol(TypeSymbol.IO, "in_string");
        inStringFunc.setReturnType(TypeSymbol.STRING.name);
        inStringFunc.offset = 20;

        var outIntFunc = new FunctionSymbol(TypeSymbol.IO, "out_int");
        outIntFunc.setReturnType(TypeSymbol.SELF_TYPE.name);
        var outIntParam = new IdSymbol("n");
        outIntParam.setTypeName(TypeSymbol.INT.name);
        outIntFunc.add(outIntParam);
        outIntFunc.offset = 16;
        var inIntFunc = new FunctionSymbol(TypeSymbol.IO, "in_int");
        inIntFunc.setReturnType(TypeSymbol.INT.name);
        inIntFunc.offset = 24;

        TypeSymbol.OBJECT.add(abortFunc);
        TypeSymbol.OBJECT.add(typeNameFunc);
        TypeSymbol.OBJECT.add(copyFunc);

        TypeSymbol.STRING.add(lengthFunc);
        TypeSymbol.STRING.add(concatFunc);
        TypeSymbol.STRING.add(substrFunc);

        TypeSymbol.IO.add(outStringFunc);
        TypeSymbol.IO.add(outIntFunc);
        TypeSymbol.IO.add(inStringFunc);
        TypeSymbol.IO.add(inIntFunc);

        TypeSymbol.INT.add(new IdSymbol("value"));
        TypeSymbol.BOOL.add(new IdSymbol("value"));
    }
    
    /**
     * Displays a semantic error message.
     * 
     * @param ctx Used to determine the enclosing class context of this error,
     *            which knows the file name in which the class was defined.
     * @param info Used for line and column information.
     * @param str The error message.
     */
    public static void error(ParserRuleContext ctx, Token info, String str) {
        while (! (ctx.getParent() instanceof CoolParser.ProgramContext))
            ctx = ctx.getParent();
        
        String message = "\"" + new File(Compiler.fileNames.get(ctx)).getName()
                + "\", line " + info.getLine()
                + ":" + (info.getCharPositionInLine() + 1)
                + ", Semantic error: " + str;
        
        System.err.println(message);
        
        semanticErrors = true;
    }
    
    public static void error(String str) {
        String message = "Semantic error: " + str;
        
        System.err.println(message);
        
        semanticErrors = true;
    }
    
    public static boolean hasSemanticErrors() {
        return semanticErrors;
    }
}
