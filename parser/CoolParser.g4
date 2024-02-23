parser grammar CoolParser;

options {
    tokenVocab = CoolLexer;
}

@header{
    package cool.parser;
}

program: (classes+=class SEMICOLON)+;

class: CLASS childType=TYPE (INHERITS parentType=TYPE)? LBRACE (features+=feature SEMICOLON)* RBRACE;

feature: ID LPAREN (formals+=formal (COMMA formals+=formal)*)? RPAREN COLON TYPE LBRACE body=expr RBRACE      #methodDefinion
       | ID COLON TYPE (ASSIGN body=expr)?                                                                    #attributeDefinition
       ;

formal: ID COLON TYPE;

varDef: ID COLON TYPE (ASSIGN body=expr)?;

branch: ID COLON TYPE RESULT body=expr SEMICOLON;

expr
    : left=expr (AT TYPE)? DOT ID LPAREN (params+=expr(COMMA params+=expr)*)? RPAREN    #explicitDispatch
    | ID LPAREN (params+=expr (COMMA params+=expr)*)? RPAREN                            #implicitDispatch
    | IF cond=expr THEN then=expr ELSE else=expr FI                                     #if
    | WHILE cond=expr LOOP body=expr POOL                                               #while
    | LBRACE (lines+=expr SEMICOLON)+ RBRACE                                            #block
    | LET vars+=varDef (COMMA vars+=varDef)* IN body=expr                               #let
    | CASE cond=expr OF (branches+=branch)+ ESAC                                        #case
    | NEW TYPE                                                                          #new
    | ISVOID body=expr                                                                  #isvoid
    | NEG body=expr                                                                     #neg
    | left=expr op=(MUL|DIV) right=expr                                                 #mulDiv
    | left=expr op=(PLUS|MINUS) right=expr                                              #plusMinus
    | left=expr LT right=expr                                                           #lt
    | left=expr LTE right=expr                                                          #lte
    | left=expr EQ right=expr                                                           #eq
    | NOT body=expr                                                                     #not
    | LPAREN body=expr RPAREN                                                           #paren
    | ID                                                                                #id
    | INT                                                                               #int
    | STRING                                                                            #string
    | BOOL                                                                              #bool
    | ID ASSIGN val=expr                                                                #assign
    ;