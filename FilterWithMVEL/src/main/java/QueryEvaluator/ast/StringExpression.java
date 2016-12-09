package QueryEvaluator.ast;

/**
 * Created with IntelliJ IDEA.
 * User: Nish
 * Date: 12/9/16
 * Time: 7:36 AM
 * To change this template use File | Settings | File Templates.
 */

/**
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
    public boolean evaluate(String msg);
}
