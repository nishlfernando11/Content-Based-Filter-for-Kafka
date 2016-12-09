package QueryEvaluator.ast.nonterminal;

import QueryEvaluator.ast.NonTerminal;
import QueryEvaluator.ast.StringExpression;
import QueryEvaluator.ast.terminal.Sentence;
import org.ahocorasick.trie.Trie;

import java.util.ArrayList;

import static com.filter.clients.FilterConsumerAlgo.buildInterestTrie;
import static com.filter.clients.FilterConsumerAlgo.notMatch;
import static com.filter.clients.FilterConsumerAlgo.orMatch;

public class Not extends NonTerminal {
	public void setChild(StringExpression child) {
		setLeft(child);
	}

	public void setRight(StringExpression right){
		throw new UnsupportedOperationException();
	}

	public String interpret() {
		return  "!" +left.interpret();
                //"attach not evaluator trie";
                //!left.interpret();
	}
    public boolean evaluate(String message){
        //base: both children terminals
//        if(left instanceof Sentence) {
//            ArrayList<String> list = new ArrayList<String>(1);
//            list.add(left.interpret());
//            Trie trie = buildInterestTrie(list);
//            return notMatch(trie, message);
//        }
        // non terminals
        if(left instanceof NonTerminal) {
            return left.evaluate(message);
        }
        return false;
    }

	public String toString() {
		return String.format("!%s", left);
	}
}
