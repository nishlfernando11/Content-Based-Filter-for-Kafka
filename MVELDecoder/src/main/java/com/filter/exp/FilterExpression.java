package com.filter.exp;

import com.filter.model.FilterWrapper;
import java.util.Map;

public abstract class FilterExpression {

    public abstract boolean isInteresting(Map<String, String> tags)throws NullPointerException;

    public final boolean isInteresting(FilterWrapper filterWrapper) {
        return isInteresting(filterWrapper.getTags());
    }

}
