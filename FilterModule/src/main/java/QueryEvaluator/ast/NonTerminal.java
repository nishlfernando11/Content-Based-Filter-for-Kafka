package QueryEvaluator.ast;

import org.ahocorasick.trie.Trie;

import java.util.ArrayList;
import java.util.Map;

import static com.filter.clients.ContentFilter.buildInterestTrie;

public abstract class NonTerminal implements StringExpression{
    protected StringExpression left,right;

	public void setLeft(StringExpression left){
		this.left = left;
	}

	public void setRight(StringExpression right){
		this.right = right;
	}

    public Map<Integer,Trie> generateTrieList(Map<Integer, Trie> list){

        if((left instanceof NonTerminal) || (right instanceof NonTerminal)) {
            if((left instanceof NonTerminal) && (right instanceof NonTerminal)) {
                left.generateTrieList(list);
                right.generateTrieList(list);
            }
            else if((left instanceof Terminal) && (right instanceof NonTerminal))  {
                ArrayList<String> interests = new ArrayList<String>(1);
                interests.add(left.interpret());
                Trie trie = buildInterestTrie(interests);
                int key = this.toString().hashCode();
                list.put(key,trie);

                right.generateTrieList(list);

            }
            else if((left instanceof NonTerminal)&& (right instanceof Terminal))  {
                left.generateTrieList(list);

                ArrayList<String> interests = new ArrayList<String>(1);
                interests.add(right.interpret());
                Trie trie = buildInterestTrie(interests);
                int key = this.toString().hashCode();
                list.put(key,trie);
            }
        }
        else{
            ArrayList<String> interests = new ArrayList<String>(2);
            interests.add(left.interpret());
            interests.add(right.interpret());
            Trie trie = buildInterestTrie(interests);
            int key = this.toString().hashCode();
            list.put(key,trie);
        }
        return list;
    }
}
