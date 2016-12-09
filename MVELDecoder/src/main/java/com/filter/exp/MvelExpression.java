package com.filter.exp;

import org.mvel2.MVEL;
import java.io.Serializable;
import java.util.Map;

public class MvelExpression extends FilterExpression {

    private final Serializable compiled;

    public MvelExpression(String expression) {
        compiled = MVEL.compileExpression(expression);
    }

    @Override
    public boolean isInteresting(Map<String, String> tags) throws NullPointerException{
        try {
            return (Boolean) MVEL.executeExpression(compiled, tags);
        }catch (Exception e){
            throw new NullPointerException();
        }
    }
}
