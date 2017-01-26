package QueryEvaluator.lexer;

import java.io.*;

public class Lexer {
	private StreamTokenizer input;

	private int symbol = NONE;
	public static final int EOL = -3;
	public static final int EOF = -2;
	public static final int INVALID = -1;

	public static final int NONE = 0;

	public static final int OR = 1;
	public static final int AND = 2;
	public static final int NOT = 3;

	public static final int TRUE = 4;
	public static final int FALSE = 5;

	public static final int LEFT = 6;
	public static final int RIGHT = 7;
    public static final int KEYWORD  = 8;
    public static final int SPACE  = 9;

    public static final String TRUE_LITERAL = "true";
	public static final String FALSE_LITERAL = "false";

    public static final String PATTERN =  ".*" ;
            //"[^A-Za-z0-9]*\\w.*";

	public Lexer(InputStream in) {
		Reader r = new BufferedReader(new InputStreamReader(in));
		input = new StreamTokenizer(r);

		input.resetSyntax();
		input.wordChars('a', 'z');
		input.wordChars('A', 'Z');
        input.wordChars('0', '9');

        /// add symbols eligible to string expression
        input.wordChars('@', '@');
        input.wordChars('#', '#');
        input.wordChars('$', '$');
        input.wordChars('*', '*');
        input.wordChars(',', ',');
        input.wordChars('.', '.');
        input.wordChars('?', '?');
        input.wordChars('/', '/');
        input.wordChars(';', ';');
        input.wordChars(':', ':');
        input.wordChars('~', '~');
        input.wordChars('`', '`');
        input.wordChars('%', '%');
        input.wordChars('^', '^');
        input.wordChars('+', '+');
        input.wordChars('-', '-');
        input.wordChars('=', '=');
        input.wordChars('|', '|');
        input.wordChars('\\', '\\');
        input.wordChars('<', '<');
        input.wordChars('>', '>');
        input.wordChars('"', '"');
        input.wordChars('\'', '\'');
        input.wordChars('_', '_');
        input.wordChars('\u0000', ' ');

        //input.whitespaceChars('\u0000', ' ');
		input.whitespaceChars('\n', '\t');

		input.ordinaryChar('(');
		input.ordinaryChar(')');
		input.ordinaryChar('&');
		input.ordinaryChar('|');
		input.ordinaryChar('!');
	}

	public int nextSymbol() {
		try {

			switch (input.nextToken()) {
				case StreamTokenizer.TT_EOL:
					symbol = EOL;
					break;
				case StreamTokenizer.TT_EOF:
					symbol = EOF;
					break;
				case StreamTokenizer.TT_WORD: {
					if (input.sval.equalsIgnoreCase(TRUE_LITERAL)) symbol = TRUE;
					else if (input.sval.equalsIgnoreCase(FALSE_LITERAL)) symbol = FALSE;
                    else if (input.sval.matches(PATTERN)) symbol = KEYWORD;
                    break;
				}
				case '(':
					symbol = LEFT;
					break;
				case ')':
					symbol = RIGHT;
					break;
				case '&':
                    symbol = AND;
					break;
				case '|':
				    symbol = OR;
					break;
				case '!':
				    symbol = NOT;
					break;
				default:
					symbol = INVALID;
			}
		} catch (IOException e) {
			symbol = EOF;
		}

		return symbol;
	}

	public String toString() {
		return input.toString();
	}
    public String getKeyword() {
        return input.sval;
    }


    public static void main(String[] args) {
		String expression = "america & ((china | sport) & !(cricket & italy))";
//        String expression = "HIV/AIDS&'mein kampf'&01/2011";
        // america & ((china | sport) & !(cricket & italy)) & !bad & ((china|sport) & !(cricket | italy))
		Lexer l = new Lexer(new ByteArrayInputStream(expression.getBytes()));
		int s;
		while ( (s = l.nextSymbol()) != Lexer.EOF)
            if(s != EOL)
                System.out.printf("%s -> %s\n", l, s);
	}
}
