package QueryEvaluator.ast.nonterminal;

import QueryEvaluator.ast.NonTerminal;
import QueryEvaluator.ast.Terminal;
import org.ahocorasick.trie.Trie;

import java.util.Map;

import static com.filter.clients.ContentFilter.*;

public class And extends NonTerminal {
    public String interpret() {
        return  "( " +left.interpret() + " && " + right.interpret() + " )";
    }

    public boolean evaluate(String message, Map<Integer, Trie> list){

        boolean lt=false,rt=false;

        // non terminals , terminals mixed
        if((left instanceof NonTerminal) || (right instanceof NonTerminal)) {
            if((left instanceof NonTerminal) && (right instanceof NonTerminal)) {
                lt=left.evaluate(message, list);
                if(!lt)return false;
                rt=right.evaluate(message, list);
            }
            else if((left instanceof Terminal) && (right instanceof NonTerminal))  {
                lt = orMatch(list.get(this.toString().hashCode()), message);
                if(!lt)return false;
                rt = right.evaluate(message, list);

            }
            else if((left instanceof NonTerminal)&& (right instanceof Terminal))  {
                lt=left.evaluate(message, list);
                if(!lt)return false;
                rt =    orMatch(list.get(this.toString().hashCode()), message);
            }
        }
        // terminals only
        else{
            boolean b = andMatch(list.get(this.toString().hashCode()), message);
            return b;
        }
        return (lt && rt);
    }


    public String toString() {
		return String.format("(%s & %s)", left, right);
	}
}
