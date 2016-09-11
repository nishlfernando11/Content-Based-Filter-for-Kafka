package main.java.com.filter.exp;

import org.mvel2.MVEL;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by pradeep on 22/06/15.
 */
public class MvelExpression extends FilterExpression {

    private final Serializable compiled;

    public MvelExpression(String expression) {
        compiled = MVEL.compileExpression(expression);
    }

    @Override
    public boolean isInteresting(Map<String, String> tags) {
        return (Boolean) MVEL.executeExpression(compiled, tags);
    }
}
