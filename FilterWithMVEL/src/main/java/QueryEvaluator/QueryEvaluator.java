package QueryEvaluator;

import QueryEvaluator.ast.StringExpression;
import QueryEvaluator.lexer.Lexer;
import QueryEvaluator.parser.RecursiveDescentParser;

import java.io.ByteArrayInputStream;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class
        QueryEvaluator {
	public static void main(String[] args) throws InterruptedException {
        // String to be scanned to find the pattern.
        //String line = "hey~@#$%^&*(()_+`';\"/><.,:This \\n | {}()$&+,:;=?@#|[](){}'<>.^*()%!-order was! placed for _ QT@3000! OK?~@#$%^&*(()_+`';\"/><.,:This | {}()$&+,:;=?@#|[](){}'<>.^*()%!";
        //System.out.println(line.matches(".*"));

        //Pattern pattern = Pattern.compile("[^A-Za-z0-9]*\\w.*", Pattern.CASE_INSENSITIVE);
        //Matcher matcher = pattern.matcher(line);
        // using Matcher find(), group(), start() and end() methods
//        while (matcher.find()) {
//            System.out.println("Found the text " + matcher.group());
//            if(matcher.group()==line)System.out.println("true");
//        }


        Scanner sc = new Scanner((System.in));
		String expression = "";
		if(args.length > 0 && args[0].equals("-f")) {
			while(sc.hasNextLine()) expression += sc.nextLine(); System.out.println(expression);
		} else {
			System.out.println("Insert an expression:");
			expression = sc.nextLine();
		}

		Lexer lexer = new Lexer(new ByteArrayInputStream(expression.getBytes()));
		RecursiveDescentParser parser = new RecursiveDescentParser(lexer);
	    StringExpression ast = parser.build();
		System.out.println(String.format("AST: %s", ast));
		System.out.println(String.format("RES: %s", ast.interpret()));
        System.out.println(String.format("RES: %s", ast.evaluate("america is good with china")));
    }
}
