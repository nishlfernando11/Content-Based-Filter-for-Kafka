package com.filter.clients;

/**
 * Created with IntelliJ IDEA.
 * User: Nish
 * Date: 10/20/16
 * Time: 12:09 PM
 * To change this template use File | Settings | File Templates.
 */

import java.io.*;
import java.lang.Object;
import java.sql.Time;
import java.util.*;

import com.eaio.stringsearch.BoyerMooreHorspoolRaita;
import com.eaio.stringsearch.StringSearch;
import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;

/**
 * Content Filter function for consumer
 */
public class FilterConsumerAlgo {
    public static void main(String[] args){
        String[] values = { "hot","hot chocolate"};
        Collection<String> contentArray = new ArrayList<String>();
        Collections.addAll(contentArray,values);

        //String text2 = "hot as hot choco want hot chocolate hello hot we are hot chocollate";
        String text2 = "hot chocolate green hott chocolate";

        long curtime = System.currentTimeMillis();
        long endtime = 0;
//
//        Trie trie = Trie.builder()
//                .removeOverlaps()
//                .caseInsensitive()
//                .onlyWholeWords()
//                .patternCount(values.length)
//                .addKeyword("hot")
//                .addKeyword("hot chocolate")
//                .build();

        endtime = System.currentTimeMillis();
        System.out.println(endtime - curtime + " millisecs");

        Trie mytrie = buildInterestTrie((ArrayList)contentArray);

        // or match applied
        boolean match = orMatch(mytrie,text2);

        if(match){
            System.out.println("matched :" + text2 );
        }
    }

    /**
     * Function that builds Interest Trie for positive-only or negative-only interests
     * positive-only interests represent a WhiteList which requests messages which satisfy given interests
     * negative-only interests represent a BlackList which requests messages which does not contain given interests
     * @param contentArray List of interests specified by the consumer
     * @return interest Trie for the given set of interests
     */
    public static Trie buildInterestTrie(ArrayList<String> contentArray){
        Trie.TrieBuilder trieBuilder =  new Trie.TrieBuilder();
        trieBuilder.removeOverlaps().onlyWholeWords().caseInsensitive().patternCount(contentArray.size());

        // build the Trie for each consumer interests
        for(int x = 0; x < contentArray.size(); x++){
            System.out.println(contentArray.get(x)) ;
            trieBuilder.addKeyword(contentArray.get(x));
        }
        return trieBuilder.build();
    }

    /**
     * Find the first match of at least one of given interests
     * WhiteList match and filter only expected interests specified by the consumer
     * @param trie Trie built for the interests
     * @param text message to be scanned
     * @return true if one match found, otherwise false
     */
    public static boolean orMatch(Trie trie, CharSequence text){
        return trie.containsMatch(text);
    }

    /**
     * Find matches for all submitted interests
     * @param trie Trie built for the interests
     * @param text message to be scanned
     * @return true if all matches found, otherwise false
     */
    public static boolean andMatch(Trie trie, CharSequence text){
        return trie.parseTextforAnd(text);
    }

    /**
     * Find messages that does not satisfy given interests
     * BlackList filter unexpected interests specified by the consumer
     * @param trie Trie built for the interests
     * @param text message to be scanned
     * @return true if given matches are not, otherwise false
     */
    public static boolean notMatch(Trie trie, CharSequence text){
        boolean isMatched = orMatch(trie,text);
        if(!isMatched)
            return true;
        else
            return false;
    }


    /**
     * String matching function that matches a given text against a given pattern
     * @param text Text to be scanned for the pattern
     * @param pattern Pattern to be searched in the given Text
     * @return return True if pattern was found in the text,otherwise, False
     */
    @Deprecated
    public  static boolean match(String text, String pattern)
    {
        //String pat = "US";

        BoyerMooreHorspoolRaita s = new BoyerMooreHorspoolRaita();

        //long curtime = System.currentTimeMillis();
        //long endtime = 0;
        //while ((line = bufferedReader.readLine()) != null) {
        if (s.searchString(text, pattern) > 0)
        {
            //System.out.println(text);
            return true;
        }
        //}
        //endtime = System.currentTimeMillis();
        //System.out.println(endtime - curtime + " millisecs");
        return false;

    }
}
