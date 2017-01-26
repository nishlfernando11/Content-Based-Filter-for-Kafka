package QueryEvaluator.parser;

import QueryEvaluator.ast.*;
import QueryEvaluator.ast.nonterminal.And;
import QueryEvaluator.ast.nonterminal.Not;
import QueryEvaluator.ast.nonterminal.Or;
import QueryEvaluator.ast.terminal.Sentence;
import QueryEvaluator.lexer.Lexer;
import org.ahocorasick.trie.Trie;

import java.util.ArrayList;

public class RecursiveDescentParser {
	private Lexer lexer;
	private int symbol;
    private StringExpression root;
    public ArrayList<Trie> interestTrieList;


    public RecursiveDescentParser(Lexer lexer) {
		this.lexer = lexer;
        this.interestTrieList = new   ArrayList<Trie>();
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
        if(symbol == Lexer.KEYWORD){
            root = new Sentence(lexer.getKeyword());
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
            root=null;
//            throw new RuntimeException("Expression Malformed");
		}
	}
}
