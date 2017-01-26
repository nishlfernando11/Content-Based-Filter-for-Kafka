package QueryEvaluator.ast;

/**
 * Created with IntelliJ IDEA.
 * User: Nish
 * Date: 12/9/16
 * Time: 7:36 AM
 * To change this template use File | Settings | File Templates.
 */

import org.ahocorasick.trie.Trie;

import java.util.ArrayList;
import java.util.Map;

/**
 * CFG in EBNF Grammar
 * <expression>::=<term>{<or><term>}
 * <term>::=<factor>{<and><factor>}
 * <factor>::=<constant>|<not><factor>|(<expression>)
 * <constant>::= string
 * <or>::='|'
 * <and>::='&'
 * <not>::='!'
 */

public interface StringExpression {
    public String interpret();
    public Map<Integer,Trie> generateTrieList(Map<Integer, Trie> list);
    public boolean evaluate(String msg, Map<Integer, Trie> list);

}
