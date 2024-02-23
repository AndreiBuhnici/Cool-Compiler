// Generated from C:/Users/buhni/Desktop/COMP/Tema2/src/cool/lexer/CoolLexer.g4 by ANTLR 4.13.1

    package cool.lexer;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class CoolLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		ERROR=1, IF=2, THEN=3, ELSE=4, FI=5, BOOL=6, NEW=7, CLASS=8, INHERITS=9, 
		WHILE=10, LOOP=11, POOL=12, CASE=13, OF=14, ESAC=15, LET=16, IN=17, ISVOID=18, 
		NOT=19, AT=20, PLUS=21, MINUS=22, MUL=23, DIV=24, EQ=25, LT=26, LTE=27, 
		ASSIGN=28, RESULT=29, LPAREN=30, RPAREN=31, LBRACE=32, RBRACE=33, SEMICOLON=34, 
		COLON=35, COMMA=36, DOT=37, NEG=38, INT=39, ID=40, TYPE=41, STRING=42, 
		LINE_COMMENT=43, MULTILINE_COMMENT=44, UNMATCHED_COMMENT_END=45, WS=46, 
		INVALID=47;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"IF", "THEN", "ELSE", "FI", "BOOL", "NEW", "CLASS", "INHERITS", "WHILE", 
			"LOOP", "POOL", "CASE", "OF", "ESAC", "LET", "IN", "ISVOID", "NOT", "AT", 
			"PLUS", "MINUS", "MUL", "DIV", "EQ", "LT", "LTE", "ASSIGN", "RESULT", 
			"LPAREN", "RPAREN", "LBRACE", "RBRACE", "SEMICOLON", "COLON", "COMMA", 
			"DOT", "NEG", "DIGIT", "INT", "LETTER", "ID", "TYPE", "STRING", "LINE_COMMENT", 
			"MULTILINE_COMMENT", "UNMATCHED_COMMENT_END", "WS", "INVALID"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, "'if'", "'then'", "'else'", "'fi'", null, "'new'", "'class'", 
			"'inherits'", "'while'", "'loop'", "'pool'", "'case'", "'of'", "'esac'", 
			"'let'", "'in'", "'isvoid'", "'not'", "'@'", "'+'", "'-'", "'*'", "'/'", 
			"'='", "'<'", "'<='", "'<-'", "'=>'", "'('", "')'", "'{'", "'}'", "';'", 
			"':'", "','", "'.'", "'~'", null, null, null, null, null, null, "'*)'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "ERROR", "IF", "THEN", "ELSE", "FI", "BOOL", "NEW", "CLASS", "INHERITS", 
			"WHILE", "LOOP", "POOL", "CASE", "OF", "ESAC", "LET", "IN", "ISVOID", 
			"NOT", "AT", "PLUS", "MINUS", "MUL", "DIV", "EQ", "LT", "LTE", "ASSIGN", 
			"RESULT", "LPAREN", "RPAREN", "LBRACE", "RBRACE", "SEMICOLON", "COLON", 
			"COMMA", "DOT", "NEG", "INT", "ID", "TYPE", "STRING", "LINE_COMMENT", 
			"MULTILINE_COMMENT", "UNMATCHED_COMMENT_END", "WS", "INVALID"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	    private void raiseError(String msg) {
	        setText(msg);
	        setType(ERROR);
	    }


	public CoolLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "CoolLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 42:
			STRING_action((RuleContext)_localctx, actionIndex);
			break;
		case 44:
			MULTILINE_COMMENT_action((RuleContext)_localctx, actionIndex);
			break;
		case 45:
			UNMATCHED_COMMENT_END_action((RuleContext)_localctx, actionIndex);
			break;
		case 47:
			INVALID_action((RuleContext)_localctx, actionIndex);
			break;
		}
	}
	private void STRING_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:
			 raiseError("Unterminated string constant"); 
			break;
		case 1:
			 raiseError("EOF in string constant"); 
			break;
		case 2:

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
			    
			break;
		}
	}
	private void MULTILINE_COMMENT_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 3:
			 raiseError("EOF in comment"); 
			break;
		case 4:
			 skip(); 
			break;
		}
	}
	private void UNMATCHED_COMMENT_END_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 5:
			 raiseError("Unmatched *)"); 
			break;
		}
	}
	private void INVALID_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 6:
			 raiseError("Invalid character: " + getText()); 
			break;
		}
	}

	public static final String _serializedATN =
		"\u0004\u0000/\u014d\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001"+
		"\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004"+
		"\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007"+
		"\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b"+
		"\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002"+
		"\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002"+
		"\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002"+
		"\u0015\u0007\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002"+
		"\u0018\u0007\u0018\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002"+
		"\u001b\u0007\u001b\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002"+
		"\u001e\u0007\u001e\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007"+
		"!\u0002\"\u0007\"\u0002#\u0007#\u0002$\u0007$\u0002%\u0007%\u0002&\u0007"+
		"&\u0002\'\u0007\'\u0002(\u0007(\u0002)\u0007)\u0002*\u0007*\u0002+\u0007"+
		"+\u0002,\u0007,\u0002-\u0007-\u0002.\u0007.\u0002/\u0007/\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0003\u0004{\b\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\b\u0001\b\u0001"+
		"\b\u0001\b\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\f\u0001\f\u0001\f\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r"+
		"\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011"+
		"\u0001\u0012\u0001\u0012\u0001\u0013\u0001\u0013\u0001\u0014\u0001\u0014"+
		"\u0001\u0015\u0001\u0015\u0001\u0016\u0001\u0016\u0001\u0017\u0001\u0017"+
		"\u0001\u0018\u0001\u0018\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001c"+
		"\u0001\u001c\u0001\u001d\u0001\u001d\u0001\u001e\u0001\u001e\u0001\u001f"+
		"\u0001\u001f\u0001 \u0001 \u0001!\u0001!\u0001\"\u0001\"\u0001#\u0001"+
		"#\u0001$\u0001$\u0001%\u0001%\u0001&\u0004&\u00eb\b&\u000b&\f&\u00ec\u0001"+
		"\'\u0001\'\u0001(\u0001(\u0001(\u0001(\u0005(\u00f5\b(\n(\f(\u00f8\t("+
		"\u0001)\u0001)\u0001)\u0001)\u0005)\u00fe\b)\n)\f)\u0101\t)\u0001*\u0001"+
		"*\u0001*\u0001*\u0001*\u0001*\u0001*\u0001*\u0001*\u0005*\u010c\b*\n*"+
		"\f*\u010f\t*\u0001*\u0001*\u0001*\u0003*\u0114\b*\u0001*\u0001*\u0001"+
		"*\u0001*\u0001*\u0003*\u011b\b*\u0001+\u0001+\u0001+\u0001+\u0005+\u0121"+
		"\b+\n+\f+\u0124\t+\u0001+\u0001+\u0001+\u0003+\u0129\b+\u0001+\u0001+"+
		"\u0001,\u0001,\u0001,\u0001,\u0001,\u0005,\u0132\b,\n,\f,\u0135\t,\u0001"+
		",\u0001,\u0001,\u0001,\u0001,\u0001,\u0003,\u013d\b,\u0001-\u0001-\u0001"+
		"-\u0001-\u0001-\u0001.\u0004.\u0145\b.\u000b.\f.\u0146\u0001.\u0001.\u0001"+
		"/\u0001/\u0001/\u0003\u010d\u0122\u0133\u00000\u0001\u0002\u0003\u0003"+
		"\u0005\u0004\u0007\u0005\t\u0006\u000b\u0007\r\b\u000f\t\u0011\n\u0013"+
		"\u000b\u0015\f\u0017\r\u0019\u000e\u001b\u000f\u001d\u0010\u001f\u0011"+
		"!\u0012#\u0013%\u0014\'\u0015)\u0016+\u0017-\u0018/\u00191\u001a3\u001b"+
		"5\u001c7\u001d9\u001e;\u001f= ?!A\"C#E$G%I&K\u0000M\'O\u0000Q(S)U*W+Y"+
		",[-]._/\u0001\u0000\u0005\u0001\u000009\u0002\u0000AZaz\u0001\u0000az"+
		"\u0001\u0000AZ\u0003\u0000\t\n\f\r  \u015f\u0000\u0001\u0001\u0000\u0000"+
		"\u0000\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000"+
		"\u0000\u0000\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000"+
		"\u0000\u000b\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000"+
		"\u000f\u0001\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000\u0000\u0000"+
		"\u0013\u0001\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000\u0000\u0000"+
		"\u0017\u0001\u0000\u0000\u0000\u0000\u0019\u0001\u0000\u0000\u0000\u0000"+
		"\u001b\u0001\u0000\u0000\u0000\u0000\u001d\u0001\u0000\u0000\u0000\u0000"+
		"\u001f\u0001\u0000\u0000\u0000\u0000!\u0001\u0000\u0000\u0000\u0000#\u0001"+
		"\u0000\u0000\u0000\u0000%\u0001\u0000\u0000\u0000\u0000\'\u0001\u0000"+
		"\u0000\u0000\u0000)\u0001\u0000\u0000\u0000\u0000+\u0001\u0000\u0000\u0000"+
		"\u0000-\u0001\u0000\u0000\u0000\u0000/\u0001\u0000\u0000\u0000\u00001"+
		"\u0001\u0000\u0000\u0000\u00003\u0001\u0000\u0000\u0000\u00005\u0001\u0000"+
		"\u0000\u0000\u00007\u0001\u0000\u0000\u0000\u00009\u0001\u0000\u0000\u0000"+
		"\u0000;\u0001\u0000\u0000\u0000\u0000=\u0001\u0000\u0000\u0000\u0000?"+
		"\u0001\u0000\u0000\u0000\u0000A\u0001\u0000\u0000\u0000\u0000C\u0001\u0000"+
		"\u0000\u0000\u0000E\u0001\u0000\u0000\u0000\u0000G\u0001\u0000\u0000\u0000"+
		"\u0000I\u0001\u0000\u0000\u0000\u0000M\u0001\u0000\u0000\u0000\u0000Q"+
		"\u0001\u0000\u0000\u0000\u0000S\u0001\u0000\u0000\u0000\u0000U\u0001\u0000"+
		"\u0000\u0000\u0000W\u0001\u0000\u0000\u0000\u0000Y\u0001\u0000\u0000\u0000"+
		"\u0000[\u0001\u0000\u0000\u0000\u0000]\u0001\u0000\u0000\u0000\u0000_"+
		"\u0001\u0000\u0000\u0000\u0001a\u0001\u0000\u0000\u0000\u0003d\u0001\u0000"+
		"\u0000\u0000\u0005i\u0001\u0000\u0000\u0000\u0007n\u0001\u0000\u0000\u0000"+
		"\tz\u0001\u0000\u0000\u0000\u000b|\u0001\u0000\u0000\u0000\r\u0080\u0001"+
		"\u0000\u0000\u0000\u000f\u0086\u0001\u0000\u0000\u0000\u0011\u008f\u0001"+
		"\u0000\u0000\u0000\u0013\u0095\u0001\u0000\u0000\u0000\u0015\u009a\u0001"+
		"\u0000\u0000\u0000\u0017\u009f\u0001\u0000\u0000\u0000\u0019\u00a4\u0001"+
		"\u0000\u0000\u0000\u001b\u00a7\u0001\u0000\u0000\u0000\u001d\u00ac\u0001"+
		"\u0000\u0000\u0000\u001f\u00b0\u0001\u0000\u0000\u0000!\u00b3\u0001\u0000"+
		"\u0000\u0000#\u00ba\u0001\u0000\u0000\u0000%\u00be\u0001\u0000\u0000\u0000"+
		"\'\u00c0\u0001\u0000\u0000\u0000)\u00c2\u0001\u0000\u0000\u0000+\u00c4"+
		"\u0001\u0000\u0000\u0000-\u00c6\u0001\u0000\u0000\u0000/\u00c8\u0001\u0000"+
		"\u0000\u00001\u00ca\u0001\u0000\u0000\u00003\u00cc\u0001\u0000\u0000\u0000"+
		"5\u00cf\u0001\u0000\u0000\u00007\u00d2\u0001\u0000\u0000\u00009\u00d5"+
		"\u0001\u0000\u0000\u0000;\u00d7\u0001\u0000\u0000\u0000=\u00d9\u0001\u0000"+
		"\u0000\u0000?\u00db\u0001\u0000\u0000\u0000A\u00dd\u0001\u0000\u0000\u0000"+
		"C\u00df\u0001\u0000\u0000\u0000E\u00e1\u0001\u0000\u0000\u0000G\u00e3"+
		"\u0001\u0000\u0000\u0000I\u00e5\u0001\u0000\u0000\u0000K\u00e7\u0001\u0000"+
		"\u0000\u0000M\u00ea\u0001\u0000\u0000\u0000O\u00ee\u0001\u0000\u0000\u0000"+
		"Q\u00f0\u0001\u0000\u0000\u0000S\u00f9\u0001\u0000\u0000\u0000U\u0102"+
		"\u0001\u0000\u0000\u0000W\u011c\u0001\u0000\u0000\u0000Y\u012c\u0001\u0000"+
		"\u0000\u0000[\u013e\u0001\u0000\u0000\u0000]\u0144\u0001\u0000\u0000\u0000"+
		"_\u014a\u0001\u0000\u0000\u0000ab\u0005i\u0000\u0000bc\u0005f\u0000\u0000"+
		"c\u0002\u0001\u0000\u0000\u0000de\u0005t\u0000\u0000ef\u0005h\u0000\u0000"+
		"fg\u0005e\u0000\u0000gh\u0005n\u0000\u0000h\u0004\u0001\u0000\u0000\u0000"+
		"ij\u0005e\u0000\u0000jk\u0005l\u0000\u0000kl\u0005s\u0000\u0000lm\u0005"+
		"e\u0000\u0000m\u0006\u0001\u0000\u0000\u0000no\u0005f\u0000\u0000op\u0005"+
		"i\u0000\u0000p\b\u0001\u0000\u0000\u0000qr\u0005t\u0000\u0000rs\u0005"+
		"r\u0000\u0000st\u0005u\u0000\u0000t{\u0005e\u0000\u0000uv\u0005f\u0000"+
		"\u0000vw\u0005a\u0000\u0000wx\u0005l\u0000\u0000xy\u0005s\u0000\u0000"+
		"y{\u0005e\u0000\u0000zq\u0001\u0000\u0000\u0000zu\u0001\u0000\u0000\u0000"+
		"{\n\u0001\u0000\u0000\u0000|}\u0005n\u0000\u0000}~\u0005e\u0000\u0000"+
		"~\u007f\u0005w\u0000\u0000\u007f\f\u0001\u0000\u0000\u0000\u0080\u0081"+
		"\u0005c\u0000\u0000\u0081\u0082\u0005l\u0000\u0000\u0082\u0083\u0005a"+
		"\u0000\u0000\u0083\u0084\u0005s\u0000\u0000\u0084\u0085\u0005s\u0000\u0000"+
		"\u0085\u000e\u0001\u0000\u0000\u0000\u0086\u0087\u0005i\u0000\u0000\u0087"+
		"\u0088\u0005n\u0000\u0000\u0088\u0089\u0005h\u0000\u0000\u0089\u008a\u0005"+
		"e\u0000\u0000\u008a\u008b\u0005r\u0000\u0000\u008b\u008c\u0005i\u0000"+
		"\u0000\u008c\u008d\u0005t\u0000\u0000\u008d\u008e\u0005s\u0000\u0000\u008e"+
		"\u0010\u0001\u0000\u0000\u0000\u008f\u0090\u0005w\u0000\u0000\u0090\u0091"+
		"\u0005h\u0000\u0000\u0091\u0092\u0005i\u0000\u0000\u0092\u0093\u0005l"+
		"\u0000\u0000\u0093\u0094\u0005e\u0000\u0000\u0094\u0012\u0001\u0000\u0000"+
		"\u0000\u0095\u0096\u0005l\u0000\u0000\u0096\u0097\u0005o\u0000\u0000\u0097"+
		"\u0098\u0005o\u0000\u0000\u0098\u0099\u0005p\u0000\u0000\u0099\u0014\u0001"+
		"\u0000\u0000\u0000\u009a\u009b\u0005p\u0000\u0000\u009b\u009c\u0005o\u0000"+
		"\u0000\u009c\u009d\u0005o\u0000\u0000\u009d\u009e\u0005l\u0000\u0000\u009e"+
		"\u0016\u0001\u0000\u0000\u0000\u009f\u00a0\u0005c\u0000\u0000\u00a0\u00a1"+
		"\u0005a\u0000\u0000\u00a1\u00a2\u0005s\u0000\u0000\u00a2\u00a3\u0005e"+
		"\u0000\u0000\u00a3\u0018\u0001\u0000\u0000\u0000\u00a4\u00a5\u0005o\u0000"+
		"\u0000\u00a5\u00a6\u0005f\u0000\u0000\u00a6\u001a\u0001\u0000\u0000\u0000"+
		"\u00a7\u00a8\u0005e\u0000\u0000\u00a8\u00a9\u0005s\u0000\u0000\u00a9\u00aa"+
		"\u0005a\u0000\u0000\u00aa\u00ab\u0005c\u0000\u0000\u00ab\u001c\u0001\u0000"+
		"\u0000\u0000\u00ac\u00ad\u0005l\u0000\u0000\u00ad\u00ae\u0005e\u0000\u0000"+
		"\u00ae\u00af\u0005t\u0000\u0000\u00af\u001e\u0001\u0000\u0000\u0000\u00b0"+
		"\u00b1\u0005i\u0000\u0000\u00b1\u00b2\u0005n\u0000\u0000\u00b2 \u0001"+
		"\u0000\u0000\u0000\u00b3\u00b4\u0005i\u0000\u0000\u00b4\u00b5\u0005s\u0000"+
		"\u0000\u00b5\u00b6\u0005v\u0000\u0000\u00b6\u00b7\u0005o\u0000\u0000\u00b7"+
		"\u00b8\u0005i\u0000\u0000\u00b8\u00b9\u0005d\u0000\u0000\u00b9\"\u0001"+
		"\u0000\u0000\u0000\u00ba\u00bb\u0005n\u0000\u0000\u00bb\u00bc\u0005o\u0000"+
		"\u0000\u00bc\u00bd\u0005t\u0000\u0000\u00bd$\u0001\u0000\u0000\u0000\u00be"+
		"\u00bf\u0005@\u0000\u0000\u00bf&\u0001\u0000\u0000\u0000\u00c0\u00c1\u0005"+
		"+\u0000\u0000\u00c1(\u0001\u0000\u0000\u0000\u00c2\u00c3\u0005-\u0000"+
		"\u0000\u00c3*\u0001\u0000\u0000\u0000\u00c4\u00c5\u0005*\u0000\u0000\u00c5"+
		",\u0001\u0000\u0000\u0000\u00c6\u00c7\u0005/\u0000\u0000\u00c7.\u0001"+
		"\u0000\u0000\u0000\u00c8\u00c9\u0005=\u0000\u0000\u00c90\u0001\u0000\u0000"+
		"\u0000\u00ca\u00cb\u0005<\u0000\u0000\u00cb2\u0001\u0000\u0000\u0000\u00cc"+
		"\u00cd\u0005<\u0000\u0000\u00cd\u00ce\u0005=\u0000\u0000\u00ce4\u0001"+
		"\u0000\u0000\u0000\u00cf\u00d0\u0005<\u0000\u0000\u00d0\u00d1\u0005-\u0000"+
		"\u0000\u00d16\u0001\u0000\u0000\u0000\u00d2\u00d3\u0005=\u0000\u0000\u00d3"+
		"\u00d4\u0005>\u0000\u0000\u00d48\u0001\u0000\u0000\u0000\u00d5\u00d6\u0005"+
		"(\u0000\u0000\u00d6:\u0001\u0000\u0000\u0000\u00d7\u00d8\u0005)\u0000"+
		"\u0000\u00d8<\u0001\u0000\u0000\u0000\u00d9\u00da\u0005{\u0000\u0000\u00da"+
		">\u0001\u0000\u0000\u0000\u00db\u00dc\u0005}\u0000\u0000\u00dc@\u0001"+
		"\u0000\u0000\u0000\u00dd\u00de\u0005;\u0000\u0000\u00deB\u0001\u0000\u0000"+
		"\u0000\u00df\u00e0\u0005:\u0000\u0000\u00e0D\u0001\u0000\u0000\u0000\u00e1"+
		"\u00e2\u0005,\u0000\u0000\u00e2F\u0001\u0000\u0000\u0000\u00e3\u00e4\u0005"+
		".\u0000\u0000\u00e4H\u0001\u0000\u0000\u0000\u00e5\u00e6\u0005~\u0000"+
		"\u0000\u00e6J\u0001\u0000\u0000\u0000\u00e7\u00e8\u0007\u0000\u0000\u0000"+
		"\u00e8L\u0001\u0000\u0000\u0000\u00e9\u00eb\u0003K%\u0000\u00ea\u00e9"+
		"\u0001\u0000\u0000\u0000\u00eb\u00ec\u0001\u0000\u0000\u0000\u00ec\u00ea"+
		"\u0001\u0000\u0000\u0000\u00ec\u00ed\u0001\u0000\u0000\u0000\u00edN\u0001"+
		"\u0000\u0000\u0000\u00ee\u00ef\u0007\u0001\u0000\u0000\u00efP\u0001\u0000"+
		"\u0000\u0000\u00f0\u00f6\u0007\u0002\u0000\u0000\u00f1\u00f5\u0003O\'"+
		"\u0000\u00f2\u00f5\u0005_\u0000\u0000\u00f3\u00f5\u0003K%\u0000\u00f4"+
		"\u00f1\u0001\u0000\u0000\u0000\u00f4\u00f2\u0001\u0000\u0000\u0000\u00f4"+
		"\u00f3\u0001\u0000\u0000\u0000\u00f5\u00f8\u0001\u0000\u0000\u0000\u00f6"+
		"\u00f4\u0001\u0000\u0000\u0000\u00f6\u00f7\u0001\u0000\u0000\u0000\u00f7"+
		"R\u0001\u0000\u0000\u0000\u00f8\u00f6\u0001\u0000\u0000\u0000\u00f9\u00ff"+
		"\u0007\u0003\u0000\u0000\u00fa\u00fe\u0003O\'\u0000\u00fb\u00fe\u0005"+
		"_\u0000\u0000\u00fc\u00fe\u0003K%\u0000\u00fd\u00fa\u0001\u0000\u0000"+
		"\u0000\u00fd\u00fb\u0001\u0000\u0000\u0000\u00fd\u00fc\u0001\u0000\u0000"+
		"\u0000\u00fe\u0101\u0001\u0000\u0000\u0000\u00ff\u00fd\u0001\u0000\u0000"+
		"\u0000\u00ff\u0100\u0001\u0000\u0000\u0000\u0100T\u0001\u0000\u0000\u0000"+
		"\u0101\u00ff\u0001\u0000\u0000\u0000\u0102\u010d\u0005\"\u0000\u0000\u0103"+
		"\u0104\u0005\\\u0000\u0000\u0104\u010c\u0005\"\u0000\u0000\u0105\u0106"+
		"\u0005\\\u0000\u0000\u0106\u0107\u0005\r\u0000\u0000\u0107\u010c\u0005"+
		"\n\u0000\u0000\u0108\u0109\u0005\\\u0000\u0000\u0109\u010c\u0005\n\u0000"+
		"\u0000\u010a\u010c\t\u0000\u0000\u0000\u010b\u0103\u0001\u0000\u0000\u0000"+
		"\u010b\u0105\u0001\u0000\u0000\u0000\u010b\u0108\u0001\u0000\u0000\u0000"+
		"\u010b\u010a\u0001\u0000\u0000\u0000\u010c\u010f\u0001\u0000\u0000\u0000"+
		"\u010d\u010e\u0001\u0000\u0000\u0000\u010d\u010b\u0001\u0000\u0000\u0000"+
		"\u010e\u011a\u0001\u0000\u0000\u0000\u010f\u010d\u0001\u0000\u0000\u0000"+
		"\u0110\u0111\u0005\r\u0000\u0000\u0111\u0114\u0005\n\u0000\u0000\u0112"+
		"\u0114\u0005\n\u0000\u0000\u0113\u0110\u0001\u0000\u0000\u0000\u0113\u0112"+
		"\u0001\u0000\u0000\u0000\u0114\u0115\u0001\u0000\u0000\u0000\u0115\u011b"+
		"\u0006*\u0000\u0000\u0116\u0117\u0005\u0000\u0000\u0001\u0117\u011b\u0006"+
		"*\u0001\u0000\u0118\u0119\u0005\"\u0000\u0000\u0119\u011b\u0006*\u0002"+
		"\u0000\u011a\u0113\u0001\u0000\u0000\u0000\u011a\u0116\u0001\u0000\u0000"+
		"\u0000\u011a\u0118\u0001\u0000\u0000\u0000\u011bV\u0001\u0000\u0000\u0000"+
		"\u011c\u011d\u0005-\u0000\u0000\u011d\u011e\u0005-\u0000\u0000\u011e\u0122"+
		"\u0001\u0000\u0000\u0000\u011f\u0121\t\u0000\u0000\u0000\u0120\u011f\u0001"+
		"\u0000\u0000\u0000\u0121\u0124\u0001\u0000\u0000\u0000\u0122\u0123\u0001"+
		"\u0000\u0000\u0000\u0122\u0120\u0001\u0000\u0000\u0000\u0123\u0128\u0001"+
		"\u0000\u0000\u0000\u0124\u0122\u0001\u0000\u0000\u0000\u0125\u0126\u0005"+
		"\r\u0000\u0000\u0126\u0129\u0005\n\u0000\u0000\u0127\u0129\u0005\n\u0000"+
		"\u0000\u0128\u0125\u0001\u0000\u0000\u0000\u0128\u0127\u0001\u0000\u0000"+
		"\u0000\u0129\u012a\u0001\u0000\u0000\u0000\u012a\u012b\u0006+\u0003\u0000"+
		"\u012bX\u0001\u0000\u0000\u0000\u012c\u012d\u0005(\u0000\u0000\u012d\u012e"+
		"\u0005*\u0000\u0000\u012e\u0133\u0001\u0000\u0000\u0000\u012f\u0132\u0003"+
		"Y,\u0000\u0130\u0132\t\u0000\u0000\u0000\u0131\u012f\u0001\u0000\u0000"+
		"\u0000\u0131\u0130\u0001\u0000\u0000\u0000\u0132\u0135\u0001\u0000\u0000"+
		"\u0000\u0133\u0134\u0001\u0000\u0000\u0000\u0133\u0131\u0001\u0000\u0000"+
		"\u0000\u0134\u013c\u0001\u0000\u0000\u0000\u0135\u0133\u0001\u0000\u0000"+
		"\u0000\u0136\u0137\u0005\u0000\u0000\u0001\u0137\u013d\u0006,\u0004\u0000"+
		"\u0138\u0139\u0005*\u0000\u0000\u0139\u013a\u0005)\u0000\u0000\u013a\u013b"+
		"\u0001\u0000\u0000\u0000\u013b\u013d\u0006,\u0005\u0000\u013c\u0136\u0001"+
		"\u0000\u0000\u0000\u013c\u0138\u0001\u0000\u0000\u0000\u013dZ\u0001\u0000"+
		"\u0000\u0000\u013e\u013f\u0005*\u0000\u0000\u013f\u0140\u0005)\u0000\u0000"+
		"\u0140\u0141\u0001\u0000\u0000\u0000\u0141\u0142\u0006-\u0006\u0000\u0142"+
		"\\\u0001\u0000\u0000\u0000\u0143\u0145\u0007\u0004\u0000\u0000\u0144\u0143"+
		"\u0001\u0000\u0000\u0000\u0145\u0146\u0001\u0000\u0000\u0000\u0146\u0144"+
		"\u0001\u0000\u0000\u0000\u0146\u0147\u0001\u0000\u0000\u0000\u0147\u0148"+
		"\u0001\u0000\u0000\u0000\u0148\u0149\u0006.\u0003\u0000\u0149^\u0001\u0000"+
		"\u0000\u0000\u014a\u014b\t\u0000\u0000\u0000\u014b\u014c\u0006/\u0007"+
		"\u0000\u014c`\u0001\u0000\u0000\u0000\u0011\u0000z\u00ec\u00f4\u00f6\u00fd"+
		"\u00ff\u010b\u010d\u0113\u011a\u0122\u0128\u0131\u0133\u013c\u0146\b\u0001"+
		"*\u0000\u0001*\u0001\u0001*\u0002\u0006\u0000\u0000\u0001,\u0003\u0001"+
		",\u0004\u0001-\u0005\u0001/\u0006";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}