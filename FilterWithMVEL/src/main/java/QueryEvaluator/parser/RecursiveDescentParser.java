package QueryEvaluator.parser;

import QueryEvaluator.ast.*;
import QueryEvaluator.ast.nonterminal.And;
import QueryEvaluator.ast.nonterminal.Not;
import QueryEvaluator.ast.nonterminal.Or;
import QueryEvaluator.ast.terminal.Sentence;
import QueryEvaluator.lexer.Lexer;

public class RecursiveDescentParser {
	private Lexer lexer;
	private int symbol;
    private StringExpression root;

	public RecursiveDescentParser(Lexer lexer) {
		this.lexer = lexer;
	}

    public StringExpression build() {
        expression();
        return root;
    }

	private void expression() {
		term();
		while (symbol == Lexer.OR) {
			Or or = new Or();
			or.setLeft(root);
			term();
			or.setRight(root);
			root = or;
		}
	}

	private void term() {
		factor();
		while (symbol == Lexer.AND) {
			And and = new And();
			and.setLeft(root);
			factor();
			and.setRight(root);
			root = and;
		}
	}

	private void factor() {
		symbol = lexer.nextSymbol();
        if(symbol == lexer.KEYWORD){
            Sentence s = new Sentence(lexer.getKeyword());
            root = s;
            symbol = lexer.nextSymbol();
        }
        else if (symbol == Lexer.NOT) {
			Not not = new Not();
			factor();
			not.setChild(root);
			root = not;
		} else if (symbol == Lexer.LEFT) {
			expression();
			symbol = lexer.nextSymbol(); // we don't care about ')'
		} else {
			throw new RuntimeException("Expression Malformed");
		}
	}
}
