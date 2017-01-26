package QueryEvaluator.ast.terminal;

import QueryEvaluator.ast.Terminal;
import org.ahocorasick.trie.Trie;

import java.util.ArrayList;
import java.util.Map;

import static com.filter.clients.ContentFilter.buildInterestTrie;
import static com.filter.clients.ContentFilter.orMatch;

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

    public Map<Integer,Trie> generateTrieList(Map<Integer, Trie> list){
        ArrayList<String> interests = new ArrayList<String>(1);
        interests.add(this.sentence);
        Trie trie = buildInterestTrie(interests);
        int key = this.toString().hashCode();
        list.put(key,trie);
        return list;
    }

    public String interpret() {
        return sentence;
    }

    public boolean evaluate(String msg, Map<Integer, Trie> list){
        return orMatch(list.get(this.toString().hashCode()), msg);
    }
}
