package QueryEvaluator;

import QueryEvaluator.ast.StringExpression;
import QueryEvaluator.ast.nonterminal.And;
import QueryEvaluator.ast.nonterminal.Not;
import QueryEvaluator.ast.nonterminal.Or;
import QueryEvaluator.ast.terminal.Sentence;
import QueryEvaluator.lexer.Lexer;
import QueryEvaluator.parser.RecursiveDescentParser;
import org.ahocorasick.trie.Trie;
import org.junit.Test;

import static com.filter.clients.ContentFilter.buildInterestTrie;
import static junit.framework.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
* Created with IntelliJ IDEA.
* User: Nish
* Date: 12/9/16
* Time: 2:13 PM
* To change this template use File | Settings | File Templates.
*/
public class QueryParserTest {

    @Test
    public void QueryEvaluate(){
        // Assign
        String expression = "america&((with china|sport)&!(cricket&italy))";
        Lexer lexer = new Lexer(new ByteArrayInputStream(expression.getBytes()));
        RecursiveDescentParser parser = new RecursiveDescentParser(lexer);

        final QueryEvaluator qa = new QueryEvaluator();
        String message  = "america is good with china 11/01/2011 HIV/AIDS 'Mein Kampf'";
        boolean actualPass = false;
        boolean expectedPass = true;
        String astExpected = "(america & ((with china | sport) & !(cricket & italy)))";
        String astActual;
        String interpretExpected = "( america && ( ( with china || sport ) && !( cricket && italy ) ) )";
        String interpretActual;


        // Action
        StringExpression astActualStrExp = parser.build();
        interpretActual = astActualStrExp.interpret();
        qa.TList = astActualStrExp.generateTrieList(qa.TList);
        actualPass = astActualStrExp.evaluate(message, qa.TList);
        System.out.println(String.format("AST: %s", astActualStrExp));
        System.out.println(String.format("INP: %s", interpretActual));
        System.out.println(String.format("RES: %s : %b",message,actualPass));

        // Assert
        astActual  = astActualStrExp.toString();
        assertEquals(astExpected,astActual);
        assertEquals(interpretExpected,interpretActual );
        assertEquals(expectedPass,actualPass);
    }

    @Test
    public void parseExpressionCorrect(){
        // Assign
        String expression = "(america&(with china|!sport))";
        Sentence word1 = new Sentence("america");
        Sentence word2 = new Sentence("with china");
        Sentence word3 = new Sentence("sport");

        StringExpression expected;
        StringExpression actual;

        Not not = new Not();
        not.setChild(word3);

        Or or = new Or();
        or.setLeft(word2);
        or.setRight(not);

        And and = new And();
        and.setLeft(word1);
        and.setRight(or);

        expected = and;

        // Action
        actual= QueryEvaluator.parseExpression(expression);

        // Assert
        assertEquals(expected.toString(),actual.toString());
    }

    @Test
    public void generateTriesCorrect(){
        // Assign
        String expression = "(america&(with china|!sport))";
        Collection<Trie> ExpectedTrieList = new ArrayList<Trie>();
        ArrayList<String> interests = new ArrayList<String>(1);
        Trie trie;
        StringExpression ast;
        Map<Integer,Trie> actualTrieList;

        interests.add("america");
        trie = buildInterestTrie(interests);
        ExpectedTrieList.add(trie);

        interests.clear();
        interests.add("with china");
        trie = buildInterestTrie(interests);
        ExpectedTrieList.add(trie);

        interests.clear();
        interests.add("sport");
        trie = buildInterestTrie(interests);
        ExpectedTrieList.add(trie);

        // Action
        ast = QueryEvaluator.parseExpression(expression);
        actualTrieList = QueryEvaluator.generateTries(ast);

        System.out.println(ExpectedTrieList);
        System.out.println(actualTrieList.values());

        // Assert
        assertEquals(ExpectedTrieList.size(),actualTrieList.values().size());

    }

    @Test
    public void filterPass(){
        // Assign
        String message  = "america is good with china 11/01/2011 HIV/AIDS 'Mein Kampf'";
        String expression = "(america&(with china|!sport))";
        StringExpression ast = QueryEvaluator.parseExpression(expression);
        Map<Integer,Trie> tries = QueryEvaluator.generateTries(ast);
        boolean expectedPass = true;
        boolean actualPass;

        // Action
        actualPass = QueryEvaluator.filter(message,ast,tries);

        // Assert
        assertEquals(expectedPass,actualPass);
    }


    @Test
    public void filterFail(){
        // Assign
        String message  = "america is good with china 11/01/2011 HIV/AIDS 'Mein Kampf'";
        String expression = "(!america&(with china|!sport))";
        StringExpression ast = QueryEvaluator.parseExpression(expression);
        Map<Integer,Trie> tries = QueryEvaluator.generateTries(ast);
        boolean expectedPass = false;
        boolean actualPass;

        // Action
        actualPass = QueryEvaluator.filter(message,ast,tries);

        // Assert
        assertEquals(expectedPass,actualPass);
    }

	public static void main(String[] args) {
        String message = "(america & (with china | !sport))";
        Sentence word1 = new Sentence("america");
        Sentence word2 = new Sentence("with china");
        Sentence word3 = new Sentence("sport");

        Not not = new Not();
        not.setChild(word3);

        Or or = new Or();
        or.setLeft(word2);
        or.setRight(not);

        And and = new And();
        and.setLeft(word1);
        and.setRight(or);

        StringExpression root = and;
        System.out.println(root);
        System.out.println(root.interpret());
	}

}
