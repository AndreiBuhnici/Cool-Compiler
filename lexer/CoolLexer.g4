lexer grammar CoolLexer;

tokens { ERROR }

@header{
    package cool.lexer;
}

@members{
    private void raiseError(String msg) {
        setText(msg);
        setType(ERROR);
    }
}

IF: 'if';
THEN: 'then';
ELSE: 'else';
FI: 'fi';

BOOL: 'true' | 'false';

NEW: 'new';
CLASS: 'class';
INHERITS: 'inherits';

WHILE: 'while';
LOOP: 'loop';
POOL: 'pool';

CASE: 'case';
OF: 'of';
ESAC: 'esac';

LET: 'let';
IN: 'in';

ISVOID: 'isvoid';
NOT: 'not';
AT: '@';

PLUS: '+';
MINUS: '-';
MUL: '*';
DIV: '/';

EQ: '=';
LT: '<';
LTE: '<=';

ASSIGN: '<-';

RESULT: '=>';

LPAREN: '(';
RPAREN: ')';
LBRACE: '{';
RBRACE: '}';

SEMICOLON: ';';
COLON: ':';
COMMA: ',';
DOT: '.';

NEG: '~';

fragment DIGIT: [0-9];
INT: DIGIT+;

fragment LETTER: [a-zA-Z];
ID: [a-z](LETTER | '_' | DIGIT)*;
TYPE: [A-Z](LETTER | '_' | DIGIT)*;

STRING: '"' ('\\"' | '\\\r\n' | '\\\n' |  .)*?
(
    ('\r\n' | '\n') { raiseError("Unterminated string constant"); }
    | EOF { raiseError("EOF in string constant"); }
    | '"' {
        String text = getText().substring(1, getText().length() - 1);
        text = text.replace("\\\n", "\n")
                   .replace("\\n", "\n")
                   .replace("\\\r\n", "\n")
                   .replace("\\t", "\t");
        text = text.replaceAll("\\\\(.)", "$1");

        if (text.length() <= 1024)
            setText(text);
        else
            raiseError("String constant too long");

        if (text.contains("\0"))
            raiseError("String contains null character");
    }
);

LINE_COMMENT: '--' .*? ('\r\n' | '\n') -> skip;

MULTILINE_COMMENT: '(*' (MULTILINE_COMMENT | .)*? (
                EOF { raiseError("EOF in comment"); }
                | '*)' { skip(); }
);

UNMATCHED_COMMENT_END: '*)' { raiseError("Unmatched *)"); };

WS: [ \n\f\r\t]+ -> skip;

INVALID: . { raiseError("Invalid character: " + getText()); };