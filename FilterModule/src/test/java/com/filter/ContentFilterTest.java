package com.filter;

import com.filter.clients.ContentFilter;
import org.ahocorasick.trie.Trie;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: Nish
 * Date: 12/26/16
 * Time: 2:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContentFilterTest {

    @Test
    public void buildInterestTrieCorrect(){
        // Assign
        String message  = "America is good with China. Reported a good news";
        ArrayList<String> contentArray = new ArrayList<String>();
        contentArray.add("america");
        contentArray.add("china");
        contentArray.add("chinese");
        Trie generatedTrie;
        boolean expected;
        boolean actual;

        // Action
        Trie.TrieBuilder trieBuilder =  new Trie.TrieBuilder();
        Trie manualTrie =
                trieBuilder
                .onlyWholeWords()
                .caseInsensitive()
                .patternCount(contentArray.size())
                .addKeyword("america")
                .addKeyword("china")
                .addKeyword("chinese")
                .build();

        generatedTrie = ContentFilter.buildInterestTrie(contentArray);

        expected = ContentFilter.andMatch(manualTrie,message);
        actual = ContentFilter.andMatch(generatedTrie,message);

        // Assert
        assertEquals(expected,actual);
    }

    @Test
    public void andMatchPass(){
            // Assign
            String message  = "America is good with China. Reported a good news";
            ArrayList<String> interests = new ArrayList<String>();
            interests.add("america");
            interests.add("china");
            boolean actual,expected = true;

            // Action
            Trie trie = ContentFilter.buildInterestTrie(interests);
            actual = ContentFilter.andMatch(trie, message);

            // Assert
            assertEquals(expected,actual);
    }

    @Test
    public void andMatchFail(){
        // Assign
        String message  = "America is good with China. Reported a good news";
        ArrayList<String> interests = new ArrayList<String>();
        interests.add("america");
        interests.add("sport");
        boolean actual,expected = false;

        // Action
        Trie trie = ContentFilter.buildInterestTrie(interests);
        actual = ContentFilter.andMatch(trie, message);

        // Assert
        assertEquals(expected,actual);

    }

    @Test
    public void orMatchPass(){
        // Assign
        String message  = "America is good with China. Reported a good news";
        ArrayList<String> interests = new ArrayList<String>();
        interests.add("america");
        interests.add("sport");
        boolean actual,expected = true;

        // Action
        Trie trie = ContentFilter.buildInterestTrie(interests);
        actual = ContentFilter.orMatch(trie, message);

        // Assert
        assertEquals(expected,actual);

    }

    @Test
    public void orMatchFail(){
        // Assign
        String message  = "America is good with China. Reported a good news";
        ArrayList<String> interests = new ArrayList<String>();
        interests.add("italy");
        interests.add("brazil");
        boolean actual,expected = false;

        // Action
        Trie trie = ContentFilter.buildInterestTrie(interests);
        actual = ContentFilter.orMatch(trie, message);

        // Assert
        assertEquals(expected,actual);

    }

    @Test
    public void notMatchPass(){
        // Assign
        String message  = "America is good with China. Reported a good news";
        ArrayList<String> interests = new ArrayList<String>();
        interests.add("brazil");
        interests.add("italy");
        boolean actual,expected = true;

        // Action
        Trie trie = ContentFilter.buildInterestTrie(interests);
        actual = ContentFilter.notMatch(trie, message);

        // Assert
        assertEquals(expected,actual);
    }

    @Test
    public void notMatchFail(){
        // Assign
        String message  = "America is good with China. Reported a good news";
        ArrayList<String> interests = new ArrayList<String>();
        interests.add("america");
        interests.add("italy");
        boolean actual,expected = false;

        // Action
        Trie trie = ContentFilter.buildInterestTrie(interests);
        actual = ContentFilter.notMatch(trie, message);

        // Assert
        assertEquals(expected,actual);

    }
}

