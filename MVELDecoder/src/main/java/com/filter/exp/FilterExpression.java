package main.java.com.filter.exp;

import main.java.com.filter.model.FilterWrapper;

import java.util.Map;

public abstract class FilterExpression {

    public abstract boolean isInteresting(Map<String, String> tags);

    public final boolean isInteresting(FilterWrapper filterWrapper) {
        return isInteresting(filterWrapper.getTags());
    }

}
