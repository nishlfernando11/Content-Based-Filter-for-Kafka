package QueryEvaluator.ast.terminal;

import QueryEvaluator.ast.Terminal;
import org.ahocorasick.trie.Trie;

import java.util.ArrayList;

import static com.filter.clients.FilterConsumerAlgo.buildInterestTrie;
import static com.filter.clients.FilterConsumerAlgo.orMatch;

/**
 * Created with IntelliJ IDEA.
 * User: Nish
 * Date: 12/8/16
 * Time: 7:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class Sentence extends Terminal {
    public Sentence(String sentence) {
        super(sentence);
    }

    public String interpret() {
        return sentence;
    }

    public boolean evaluate(String msg){
        ArrayList<String> list = new ArrayList<String>(1);
        list.add(sentence);
        Trie trie = buildInterestTrie(list);
        return orMatch(trie, msg);
    }
}
