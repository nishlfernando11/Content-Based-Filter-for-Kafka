package QueryEvaluator.ast.nonterminal;

import QueryEvaluator.ast.NonTerminal;
import QueryEvaluator.ast.Terminal;
import QueryEvaluator.ast.terminal.Sentence;
import org.ahocorasick.trie.Trie;

import java.util.ArrayList;

import static com.filter.clients.FilterConsumerAlgo.buildInterestTrie;
import static com.filter.clients.FilterConsumerAlgo.notMatch;
import static com.filter.clients.FilterConsumerAlgo.orMatch;

public class Or extends NonTerminal {
	public String interpret() {
       return  "( " +left.interpret() + " || " + right.interpret()+ " )";

        //"attach or evaluator trie";
                //left.interpret() || right.interpret();
	}

    public boolean evaluate(String message){
        boolean lt=false,rt=false;
        //base: both children terminals
//        if((left instanceof Sentence) &&(right instanceof Sentence)) {
//            ArrayList<String> list = new ArrayList<String>(2);
//            list.add(left.interpret());
//            list.add(right.interpret());
//            Trie trie = buildInterestTrie(list);
//            return orMatch(trie, message);
//        }
        // non terminals
        if((left instanceof NonTerminal) || (right instanceof NonTerminal)) {
            if(right instanceof NonTerminal)  {
                rt=right.evaluate(message);
            }
            if(left instanceof NonTerminal)  {
                lt=left.evaluate(message);
            }
        }
        return rt || lt;
    }

	public String toString() {
		return String.format("(%s | %s)", left, right);
	}
}
