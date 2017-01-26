package QueryEvaluator;

import QueryEvaluator.ast.StringExpression;
import QueryEvaluator.lexer.Lexer;
import QueryEvaluator.parser.RecursiveDescentParser;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.*;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: Nish
 * Date: 1/7/17
 * Time: 12:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class ParsingTest {

    @Test
    public void correctParse(){
        String expression="";
        BufferedReader br =null;

        try{
            br =  new BufferedReader(new FileReader("C:\\Users\\user\\Documents\\GitHub\\Content-Based-Filter-for-Kafka\\FilterModule\\src\\test\\java\\QueryEvaluator\\test data\\parsing test cor.txt"));
            while ((expression = br.readLine()) != null) {
                try {
                    Lexer lexer = new Lexer(new ByteArrayInputStream(expression.getBytes()));
                    RecursiveDescentParser parser = new RecursiveDescentParser(lexer);
                    Object ast = parser.build();
                    assertTrue((ast instanceof StringExpression));
                    System.out.println(expression);
                }  catch (Exception e) {
                e.printStackTrace();
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void incorrectParse(){
        String expression="";
        BufferedReader br =null;

        try{
            br =  new BufferedReader(new FileReader("C:\\Users\\user\\Documents\\GitHub\\Content-Based-Filter-for-Kafka\\FilterModule\\src\\test\\java\\QueryEvaluator\\test data\\parsing test incor.txt"));
            while ((expression = br.readLine()) != null) {
                try {
                    Lexer lexer = new Lexer(new ByteArrayInputStream(expression.getBytes()));
                    RecursiveDescentParser parser = new RecursiveDescentParser(lexer);
//                    Object ast = parser.build() ;
                    try{
                        parser.build();
                    }  catch (Exception e){
                        System.out.println(expression+" null");
                    }
//                    assertNull(parser.build());
//                    assertFalse((ast instanceof StringExpression));
                }  catch (Exception e) {
//                    e.printStackTrace();
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
